import "./index.css";

import { useCallback, useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  BTOGGLE,
  CLOSE_ALERT_TOGGLE,
  CAM_OPEN_TOGGLE,
  PARTNERNICK,
} from "../../../redux/reducers/MToggle";
import { io } from "socket.io-client";
import { useNavigate } from "react-router-dom";

import SettingModal from "../../../components/Meeting/SettingModal";
import ProgressBar from "../../../components/Meeting/ProgressBar";
import BlockModal from "../../../components/Meeting/BlockModal";
import Alert from "../../../components/Start/Alert";
import Imoticon from "../../../components/Meeting/Imoticon";
import LightTag from "../../../components/Meeting/LightTag";
import MyCamSubDiv from "../../../components/Meeting/MyCamSubDiv";

let socket = io("https://blurblur.kr", {
  path: "/socket",
  transports: ["websocket", "polling"],
  secure: true,
});
console.log(`socket: `, socket);
let roomName;
let myPeerConnection;
let myStream;
let firstRendering = false;
let meetingInTmp = 0;
// console.log("MeetingIn 페이지 렌더링");
function MeetingIn() {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const [lightToggle, setLightToggle] = useState(false);
  const [smileToggle, setSmileToggle] = useState(false);
  const [camToggle, setCamToggle] = useState(true);
  const [myMicToggle, setMyMicToggle] = useState(true);
  const [partnerMicToggle, setPartnerMicToggle] = useState(true);
  const [mysoundToggle, setMySoundToggle] = useState(false);
  const [partnerSoundToggle, setPartnerSoundToggle] = useState(false);
  const [mySoundVal, setMySoundVal] = useState(50);
  const [partnerSoundVal, setPartnerSoundVal] = useState(50);
  const [blockToggle, setBlockToggle] = useState(false);

  const isShowBlockModal = useSelector((state) => state.mt.isShowBlockModal);
  const closeAlertToggle = useSelector((state) => state.mt.closeAlertToggle);
  const camOpenToggle = useSelector((state) => state.mt.camOpenToggle);
  const sendRoomName = useSelector((state) => state.mt.roomNumber);
  const partnerInterests = useSelector((state) => state.mt.partnerInterests);
  const partnerNick = useSelector((state) => state.mt.partnerNick);

  // 컴퓨터와 연결되어있는 모든 장치를 가져옴
  const getCameras = async () => {
    if (meetingInTmp === 0) {
      meetingInTmp = 1;
      try {
        const devices = await navigator.mediaDevices.enumerateDevices();
        const cameras = devices.filter((device) => device.kind === "videoinput");
        // videoDevices = cameras;
        const currentCamera = myStream.getVideoTracks()[0];

        const camerasSelect = document.querySelector("#cameras");
        cameras.forEach((camera) => {
          const option = document.createElement("option");
          option.value = camera.deviceId;
          option.innerText = camera.label;
          if (currentCamera.label === camera.label) {
            option.selected = true;
          }
          camerasSelect.appendChild(option);
        });
      } catch (error) {
        console.log(error);
      }
    }
  };

  const getMedia = useCallback(async (deviceId) => {
    // 초기 실행
    const initialConstraints = {
      audio: false,
      video: { width: 160, height: 150 },
    };
    // (select에서) 카메라를 변경했을 때의 device로 실행
    const cameraConstraints = {
      audio: false,
      video: {
        deviceId: { exact: deviceId },
        width: { exact: 600 },
        height: { exact: 593 },
      },
    };
    try {
      myStream = await navigator.mediaDevices.getUserMedia(
        deviceId ? cameraConstraints : initialConstraints
      );
      document.querySelector(".MMyCamDiv1").srcObject = myStream;
      await getCameras();
    } catch (error) {
      console.log(error);
    }
  }, []);

  async function handleCameraChange() {
    await getMedia(document.querySelector("#cameras").value);

    // 다른 브라우저로 보내진 나의 비디오와 오디오 데이터를 컨트롤
    // 내 카메라가 변경될 때마다 상대방의 내화면도 바뀌게 해줌
    if (myPeerConnection) {
      const videoTrack = myStream.getVideoTracks()[0];
      const videoSender = myPeerConnection
        .getSenders()
        .find((sender) => sender.track.kind === "video");
      console.log(videoSender);
      videoSender.replaceTrack(videoTrack);
    }
  }

  // socket Code

  // Peer A
  socket.on("welcome", async (rooms) => {
    console.log("node로 부터 온 welcome ");
    console.log(`현재 들어온 rooms들 확인`, rooms);
    const offer = await myPeerConnection.createOffer();
    myPeerConnection.setLocalDescription(offer);
    // console.log(myPeerConnection.setLocalDescription(offer));
    console.log("send the offer");
    socket.emit("offer", offer, roomName);
  });

  socket.on("roomsCheck", (rooms) => {
    console.log(rooms);
  });

  // Peer B
  socket.on("offer", async (offer) => {
    console.log("received the offer");
    await myPeerConnection.setRemoteDescription(offer);
    const answer = await myPeerConnection.createAnswer();
    myPeerConnection.setLocalDescription(answer);
    socket.emit("answer", answer, roomName);
    console.log("sent the answer ");
  });

  // Peer A
  socket.on("answer", (answer) => {
    console.log("received the answer");
    myPeerConnection.setRemoteDescription(answer);
  });

  socket.on("ice", (ice) => {
    console.log("receive candidate");
    myPeerConnection.addIceCandidate(ice);
  });

  socket.on("peer-leaving", () => {
    const peerStream = document.querySelector(".MPartenerCamDiv1");

    dispatch(PARTNERNICK(""));
    document.querySelector(".MPartenerCamSubText").innerText = partnerNick;

    peerStream.srcObject.getTracks().forEach((track) => {
      track.stop();
    });
    peerStream.srcObject = null;

    if (!alert("상대방이 나가셨습니다.\n 확인을 누르시면 홈페이지로 이동합니다.")) {
      hangUp();
    }
  });

  // 미팅 나가기버튼 && (한명이 나가고) 미팅 나가기 버튼 클릭시
  function hangUp() {
    const peerStream = document.querySelector(".MPartenerCamDiv1");

    myPeerConnection.close();
    myPeerConnection = null;
    dispatch(PARTNERNICK(""));
    document.querySelector(".MPartenerCamSubText").innerText = partnerNick;

    // 내 비디오 끔
    myStream.getTracks().forEach((track) => {
      // Clearly eindicates that th stream no longer uses the source
      track.stop();
    });

    // 피어 비디오 끔
    if (peerStream?.srcObject) {
      peerStream.srcObject.getTracks().forEach((track) => {
        track.stop();
      });
      peerStream.srcObject = null;
    }

    // 방 떠나기
    socket.emit("leave-room", roomName, () => {
      roomName = "";

      // Generate new socketIO socket (disconnect from previous)
      socket.disconnect();
      // socket = io("https://blurblur.kr", {
      //     path: "/socket",
      //     transports: ["websocket", "polling"],
      //     secure: true,
      // });
      // 인터벌 초기화 해줘야 함!!!!!!!!!!!!!!!!!!!!!!!
      navigate("/home");
    });
  }

  // RTC Code

  function makeConnection() {
    console.log("makeConnection 들어왔음");
    try {
      myPeerConnection = new RTCPeerConnection({
        iceServers: [
          {
            urls: [
              "stun:stun.l.google.com:19302",
              "stun:stun1.l.google.com:19302",
              "stun:stun2.l.google.com:19302",
              "stun:stun3.l.google.com:19302",
              "stun:stun4.l.google.com:19302",
            ],
          },
        ],
      });
      // console.log(myStream.getTracks());
      myPeerConnection.addEventListener("icecandidate", handleIce);
      myPeerConnection.addEventListener("track", handleAddStream);
      myStream.getTracks().forEach((track) => myPeerConnection.addTrack(track, myStream));
      console.log(`makeConnection이 성공했습니다.`);
    } catch (error) {
      console.log(`${error} makeConnection이 실패했습니다.`);
    }
  }

  function handleIce(data) {
    console.log("sent candidate");
    socket.emit("ice", data.candidate, roomName);
    console.log(socket.emit("ice", data.candidate, roomName));
  }

  function handleAddStream(data) {
    console.log("got an event from my peer");
    const peerStream = document.querySelector(".MPartenerCamDiv1");
    peerStream.srcObject = data.stream[0];
  }

  // (파트너 캠 상단의) 관심사 표현 토글

  const showLight = useCallback(() => {
    setLightToggle((prev) => !prev);

    if (!lightToggle) {
      // basic 클래스 있을 경우(두번째 false 부터)
      if (document.querySelector(".basicLight")) {
        document.querySelector(".basicLight").classList.replace("basicLight", "clickLight");
        document
          .querySelector(".basicLightChangeDiv")
          .classList.replace("basicLightChangeDiv", "clickLightChangeDiv");
      }
      // basic 클래스 없을 경우(첫번째 false)
      else {
        document.querySelector(".lightTagBtn").classList.add("clickLight");
        document.querySelector(".lightTagsDiv").classList.add("clickLightChangeDiv");
      }

      // document.querySelector(".lightTagsDiv").style.display = "block";

      // click 클래스인 경우(true)
    } else {
      document.querySelector(".clickLight").classList.replace("clickLight", "basicLight");
      document
        .querySelector(".clickLightChangeDiv")
        .classList.replace("clickLightChangeDiv", "basicLightChangeDiv");
      // document.querySelector(".lightTagsDiv").style.display = "none";
    }
  }, [lightToggle]);

  // (나의 캠 상단의) 이모지 표현 토글
  const showSmile = () => {
    setSmileToggle((prev) => !prev);

    if (!smileToggle) {
      if (document.querySelector(".basicSmileChangeDiv"))
        document
          .querySelector(".basicSmileChangeDiv")
          .classList.replace("basicSmileChangeDiv", "clickSmileChangeDiv");
      else document.querySelector(".ImotionDiv").classList.add("clickSmileChangeDiv");
    } else
      document
        .querySelector(".clickSmileChangeDiv")
        .classList.replace("clickSmileChangeDiv", "basicSmileChangeDiv");
  };

  // (관심사/이미지가 켜져있을 때) 바깥 배경 누르게되면 토글 off 처리
  const lightAndSmileBgOut = () => {
    if (smileToggle) {
      setSmileToggle((prev) => !prev);
      document
        .querySelector(".clickSmileChangeDiv")
        .classList.replace("clickSmileChangeDiv", "basicSmileChangeDiv");
    }
    if (lightToggle) {
      setLightToggle((prev) => !prev);
      document.querySelector(".clickLight").classList.replace("clickLight", "basicLight");
      document
        .querySelector(".clickLightChangeDiv")
        .classList.replace("clickLightChangeDiv", "basicLightChangeDiv");
    }
  };

  // 나의 캠 토글
  const showCam = (e) => {
    console.log(myStream.getUserMedia);
    // console.log(myStream.getVideoTracks().enabled);
    myStream.getVideoTracks().forEach((track) => (track.enabled = !track.enabled));

    if (camToggle) {
      document.querySelector(".camOn").classList.replace("camOn", "camOff");
    } else {
      document.querySelector(".camOff").classList.replace("camOff", "camOn");
    }
    setCamToggle((prev) => !prev);
  };

  // 나의 마이크 토글
  const openMyMic = () => {
    myStream.getAudioTracks().forEach((track) => (track.enabled = !track.enabled));

    if (myMicToggle) {
      document.querySelector(".myMicOn").classList.replace("myMicOn", "myMicOff");
    } else {
      document.querySelector(".myMicOff").classList.replace("myMicOff", "myMicOn");
    }
    setMyMicToggle((prev) => !prev);
  };

  // 파트너 마이크 토글
  const openPartnerMic = () => {
    if (partnerMicToggle) {
      document.querySelector(".partMicOn").classList.replace("partMicOn", "partMicOff");
    } else {
      document.querySelector(".partMicOff").classList.replace("partMicOff", "partMicOn");
    }
    setPartnerMicToggle((prev) => !prev);
  };

  // 나의 음량 조절
  const onChangeMySoundSlider = () => {
    const slider = document.querySelector(".slider");
    const progress = document.querySelector(".progressSlider");
    setMySoundVal(slider.value);
    const val = slider.value + "%";
    progress.style.width = val;
  };

  // 파트너 음량 조절
  const onChangePartnerSoundSlider = () => {
    const slider = document.querySelector(".partSlider");
    const progress = document.querySelector(".partProgressSlider");
    setPartnerSoundVal(slider.value);
    const val = slider.value + "%";
    progress.style.width = val;
  };

  // 나의 음량 토글
  const showMySound = () => {
    if (!mysoundToggle) {
      // document.querySelector(".soundOn").classList.replace("soundOn", "soundOff");
      document.querySelector(".MMyCamSubSoundDesc").style.display = "block";
    } else {
      document.querySelector(".MMyCamSubSoundDesc").style.display = "none";
      // document.querySelector(".soundOff").classList.replace("soundOff", "soundOn");
    }
    setMySoundToggle((prev) => !prev);
  };

  // 파트너 음량 토글
  const showPartnerSound = () => {
    if (!partnerSoundToggle) {
      document.querySelector(".MPartenerCamSubSoundDesc").style.display = "block";
    } else {
      document.querySelector(".MPartenerCamSubSoundDesc").style.display = "none";
    }
    setPartnerSoundToggle((prev) => !prev);
  };

  // 파트너 신고 토글
  const openBlock = () => {
    if (!blockToggle) {
      // 신고 div block으로 변경
      document.querySelector(".MPartenerCamSubBlockDesc").style.display = "block";
    } else {
      // 신고 div none으로 변경
      document.querySelector(".MPartenerCamSubBlockDesc").style.display = "none";
    }
    setBlockToggle((prev) => !prev);
  };

  // (파트너) 신고 모달 토글
  const showBlockModal = () => {
    // blockModalToggle이 false 라면
    if (!isShowBlockModal) {
      // 1. 토글 버튼을 닫아주고
      setBlockToggle((prev) => !prev);
      // 1. 해당 버튼의 div를 none처리 해줌
      document.querySelector(".MPartenerCamSubBlockDesc").style.display = "none";
      dispatch(BTOGGLE(!isShowBlockModal));
    }
  };

  const showAlertModal = () => {
    dispatch(CLOSE_ALERT_TOGGLE(false));
  };

  // 나의 캠 세팅 토글
  const showSetting = () => {
    dispatch(CAM_OPEN_TOGGLE(true));
    console.log("hi");
  };

  if (!firstRendering) {
    firstRendering = true;
    setTimeout(async () => {
      // 소켓통신을 통해서 방에 접속(이부분은 매칭이 되었을때 진행해야 하므로 전 페이지로 빼낼예정)
      // 카메라 장치 동작 메서드
      await getMedia();
      makeConnection();
      roomName = sendRoomName;
      socket.emit("join_room", roomName);
      console.log(`sendRoomName: ${sendRoomName}, ${roomName}`);
      socket = io("https://blurblur.kr", {
        path: "/socket",
        transports: ["websocket", "polling"],
        secure: true,
      });
      console.log(`socket: ${socket} `, socket);
    }, 3000);

    // setTimeout(() => {
    //   if (!alert("상대가 접속하지 않았기 때문에 홈페이지로 이동합니다.")) {
    //     dispatch(ROOM_NUM(""));
    //     dispatch(PARTNERNICK(""));
    //     navigate("/home");
    //   }
    // }, 30000);
  }

  useEffect(() => {
    const lightTagsDiv = document.querySelector(".lightTagsDiv");
    for (let i = 1; i <= partnerInterests.length; i++) {
      const lightTag = document.createElement("span");
      lightTag.id = `lightTag${i}`;
      lightTag.innerText = partnerInterests[i - 1];
      lightTagsDiv.appendChild(lightTag);
    }
  });

  return (
    <div className="MeetingIn">
      <div className="MeetingIn_CamDiv">
        <select id="cameras" onChange={handleCameraChange}></select>
        {closeAlertToggle ? (
          <Alert showAlertModal={showAlertModal} content="신고가 완료되었습니다:)" />
        ) : undefined}
        {isShowBlockModal ? <BlockModal /> : ""}
        {camOpenToggle ? <SettingModal /> : ""}
        <div className="tempBackDiv" onClick={lightAndSmileBgOut}></div>
        <div className="MLeftDiv1">
          <Imoticon />
          <div className="ImotionBtn" onClick={showSmile}></div>
          <div className="MMyCamDiv">
            <video className="MMyCamDiv1" autoPlay playsInline></video>
          </div>
          <MyCamSubDiv
            showSetting={showSetting}
            showCam={showCam}
            openMyMic={openMyMic}
            showMySound={showMySound}
            mySoundVal={mySoundVal}
            onChangeMySoundSlider={onChangeMySoundSlider}
          />
        </div>
        <div className="MRightDiv1">
          <LightTag showLight={showLight} />
          <div className="MPartenerCamDiv">
            <div className="blurEffect"></div>
            <video className="MPartenerCamDiv1" autoPlay playsInline></video>
          </div>
          <div className="MPartenerCamSubDiv">
            <span className="MPartenerCamSubText">{partnerNick} </span>
            <div className="MPartenerCamSubBtnsDiv">
              <div className="MPartenerCamSubExitBtn" onClick={hangUp}></div>
              <div className="MPartenerCamSubBlockBtn" onClick={openBlock}></div>
              <div className="MPartenerCamSubBlockDesc">
                <div className="MPartenerCamSubBlockDescTop"></div>
                <div className="MPartenerCamSubBlockDescMain" onClick={showBlockModal}>
                  <span className="MPartenerCamSubBlockDescText">Report</span>
                </div>
              </div>
              <div className="MPartenerCamSubMicBtn partMicOn" onClick={openPartnerMic}></div>
              <div className="MPartenerCamSubSoundBtn" onClick={showPartnerSound}></div>
              <div className="MPartenerCamSubSoundDesc">
                <div className="MPartenerCamSubSoundDescTop"></div>
                <div className="MPartenerCamSubSoundDescMain"></div>
                <span className="MPartenerCamSubSoundDescSoundVal">{partnerSoundVal}</span>
                <div className="MPartenerCamSubSoundDescBar">
                  <div className="part-range-slider">
                    <input
                      type="range"
                      className="partSlider"
                      min="0"
                      max="100"
                      onChange={onChangePartnerSoundSlider}
                    ></input>
                    <div className="partProgressSlider"></div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <ProgressBar />
    </div>
  );
}

export default MeetingIn;
