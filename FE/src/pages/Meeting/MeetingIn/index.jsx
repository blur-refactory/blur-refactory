import "./index.css";

import { useCallback } from "react";
import { useDispatch, useSelector } from "react-redux";
import { CLOSE_ALERT_TOGGLE, PARTNERNICK } from "../../../redux/reducers/MToggle";
import { io } from "socket.io-client";

import ProgressBar from "../../../components/Meeting/ProgressBar";
import Alert from "../../../components/Common/Alert";
import Imoticon from "../../../components/Meeting/Imoticon";
import LightTag from "../../../components/Meeting/LightTag";
import MyCamSubDiv from "../../../components/Meeting/MyCamSubDiv";
import SelectModal from "../../../components/Common/SelectModal";
import PartnerCamSubDiv from "../../../components/Meeting/PartnerCamSubDiv";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { useEffect } from "react";

// let socket = io("https://blurblur.kr", {
//   path: "/socket",
//   transports: ["websocket", "polling"],
//   secure: true,
// });
// console.log(`socket: `, socket);
let socket = null;
let roomName;
let myPeerConnection;
let myStream;
let firstRendering = false;
let meetingInTmp = 0;
const cameraAlertData = [
  {
    index: 1,
    desc: "Logitech 3.0",
  },
  {
    index: 2,
    desc: "Desktop Cam",
  },
];
const blockAlertData = [
  {
    index: 1,
    desc: "불쾌한 노출",
  },
  {
    index: 2,
    desc: "부적절한 언행",
  },
  {
    index: 3,
    desc: "사용 불가 연령",
  },
  {
    index: 4,
    desc: "불편한 행동",
  },
];
// console.log("MeetingIn 페이지 렌더링");
function MeetingIn() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const isShowBlockModal = useSelector((state) => state.mt.isShowBlockModal);
  const closeAlertToggle = useSelector((state) => state.mt.closeAlertToggle);
  const camOpenToggle = useSelector((state) => state.mt.camOpenToggle);
  const sendRoomName = useSelector((state) => state.mt.roomNumber);
  const partnerNick = useSelector((state) => state.mt.partnerNick);

  const [timer2, setTimer2] = useState(null);

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
      // setMyStream(
      //   await navigator.mediaDevices.getUserMedia(deviceId ? cameraConstraints : initialConstraints)
      // );
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

  // 미팅 나가기버튼 && (한명이 나가고) 미팅 나가기 버튼 클릭시
  function handleHangUp() {
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

  // (관심사/이미지가 켜져있을 때) 바깥 배경 누르게되면 토글 off 처리
  // const lightAndSmileBgOut = () => {
  //   if (smileToggle) {
  //     setSmileToggle((prev) => !prev);
  //     document
  //       .querySelector(".clickSmileChangeDiv")
  //       .classList.replace("clickSmileChangeDiv", "basicSmileChangeDiv");
  //   }
  //   if (lightToggle) {
  //     setLightToggle((prev) => !prev);
  //     document.querySelector(".clickLight").classList.replace("clickLight", "basicLight");
  //     document
  //       .querySelector(".clickLightChangeDiv")
  //       .classList.replace("clickLightChangeDiv", "basicLightChangeDiv");
  //   }
  // };

  const camOff = () => {
    myStream.getVideoTracks().forEach((track) => (track.enabled = !track.enabled));
  };
  const micOff = () => {
    myStream.getAudioTracks().forEach((track) => (track.enabled = !track.enabled));
  };
  const showAlertModal = () => {
    dispatch(CLOSE_ALERT_TOGGLE(false));
  };

  function setUpSocketListeners() {
    console.log("setUpSocketListeners 실행");
    socket.emit("join_room", roomName);

    //Peer A
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
        handleHangUp();
      }
    });
  }

  if (!firstRendering) {
    firstRendering = true;

    const timer = setTimeout(async () => {
      // 소켓통신을 통해서 방에 접속(이부분은 매칭이 되었을때 진행해야 하므로 전 페이지로 빼낼예정)
      // 카메라 장치 동작 메서드
      await getMedia();
      makeConnection();
      roomName = sendRoomName;

      socket = io("https://blurblur.kr", {
        path: "/socket",
        transports: ["websocket", "polling"],
        secure: true,
      });

      setUpSocketListeners();
      clearTimeout(timer);

      console.log(`sendRoomName: ${sendRoomName}, ${roomName}`);
      console.log(`socket: ${socket} `, socket);
    }, 5 * 1000);

    // setTimeout(() => {
    //   if (!alert("상대가 접속하지 않았기 때문에 홈페이지로 이동합니다.")) {
    //     dispatch(ROOM_NUM(""));
    //     dispatch(PARTNERNICK(""));
    //     navigate("/home");
    //   }
    // }, 30000);
  }

  return (
    <div className="MeetingIn">
      <div className="MeetingIn_CamDiv">
        <select id="cameras" onChange={handleCameraChange}></select>
        {closeAlertToggle ? (
          <Alert showAlertModal={showAlertModal} content="신고가 완료되었습니다:)" />
        ) : undefined}
        {isShowBlockModal ? (
          <SelectModal
            headerMsg={"신고 사유"}
            data={blockAlertData}
            confirmMsg={"신고"}
            status={"block"}
          />
        ) : (
          ""
        )}
        {camOpenToggle ? (
          <SelectModal
            headerMsg={"카메라 설정"}
            data={cameraAlertData}
            confirmMsg={"확인"}
            status={"camera"}
          />
        ) : (
          ""
        )}
        {/* <div className="tempBackDiv" onClick={lightAndSmileBgOut}></div> */}
        <div className="MLeftDiv1">
          <Imoticon />
          <div className="MMyCamDiv">
            <video className="MMyCamDiv1" autoPlay playsInline></video>
          </div>
          <MyCamSubDiv camOff={camOff} micOff={micOff} myStream={myStream} />
        </div>
        <div className="MRightDiv">
          <LightTag />
          <div className="MPartenerCamDiv">
            <div className="blurEffect"></div>
            <video className="MPartenerCamDiv1" autoPlay playsInline></video>
          </div>
          <PartnerCamSubDiv handleHangUp={handleHangUp} />
        </div>
      </div>
      <ProgressBar />
    </div>
  );
}

export default MeetingIn;
