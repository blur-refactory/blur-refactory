/* eslint-disable react-hooks/exhaustive-deps */
import "./index.css";
import { useCallback, useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux"; // useSeletor: useState와 같은 값 변경 메서드
import {
  MTOGGLE,
  ROOM_NUM,
  PARTNERINTERESTS,
  PARTNERNICK,
} from "../../../redux/reducers/MToggle";
// eslint-disable-next-line no-unused-vars
import { useNavigate } from "react-router-dom";
import { CustomAxios } from "../../../api/CustomAxios";

let myStream;
let USERSEX = "";
let firstRendering = false;
let errorCnt = 0;
function MeetingNotIn() {
  let userId = useSelector((state) => state.strr.id); // store에 저장되어있는 내 아이디
  USERSEX = useSelector((state) => state.mt.myGender); // store에 저장되어있는 내 성별
  let myGeo = useSelector((state) => state.mt.myGeo); // store에 저장되어있는 내 위치(위도, 경도)

  const [isMatching, setIsMatching] = useState(false);
  const [camToggle, setCamToggle] = useState(true);
  const [myMicToggle, setMyMicToggle] = useState(true);

  const navigate = useNavigate();
  const dispatch = useDispatch();

  let mainTimer = setInterval(actionStart, 7000);

  // 방번호 생성 메서드
  function makeRoomName() {
    let today = new Date();
    let year = today.getFullYear();
    let month = ("0" + (today.getMonth() + 1)).slice(-2);
    let day = ("0" + today.getDate()).slice(-2);
    let hours = ("0" + today.getHours()).slice(-2);
    let minutes = ("0" + today.getMinutes()).slice(-2);
    let seconds = ("0" + today.getSeconds()).slice(-2);
    let dataString = `${year}${month}${day}${hours}${minutes}${seconds}`;

    return dataString;
  }

  const getCameras1 = useCallback(async () => {
    try {
      myStream = await navigator.mediaDevices.getUserMedia({
        audio: false,
        video: { width: { exact: 237.75 }, height: { exact: 286.5 } },
      });

      document.querySelector(".MMyCamDiv3").srcObject = myStream;
      console.log("hi im rendering");
    } catch (error) {
      console.log(error);
    }
  }, []);

  const catchingAnimation = useCallback(() => {
    // e.preventDefault();
    const centerTitle = document.querySelector(".MCenterTitle");
    const centerDesc = document.querySelector(".MCenterDesc");
    const darkBlurDiv = document.querySelector(".DarkBlurDiv");

    if (!isMatching) {
      centerTitle.innerText = "Catching !!!";
      centerDesc.innerText = "It's Soon";
      darkBlurDiv.style.display = "block";
    } else {
      centerTitle.innerText = "Searching Other :)";
      centerDesc.innerText = "Please wait For 5 minutes.";
      darkBlurDiv.style.display = "none";
    }
    // console.dir(centerTitle);
    setIsMatching(!isMatching);
  }, [isMatching]);

  const toggleChange = (e) => {
    // e.preventDefault();
    dispatch(MTOGGLE());

    myStream.getTracks().forEach((track) => track.stop());
    document.querySelector(".MMyCamDiv3").srcObject = null;
    myStream = "";
  };

  const handleCamToggle = useCallback(() => {
    myStream.getVideoTracks().forEach((track) => (track.enabled = !camToggle));
    // myStream.getVideoTracks().forEach((track) => console.log(track.enabled));
    // console.log(myStream.getVideoTracks());

    // console.log(camToggle);
    if (camToggle) {
      document.querySelector(".noShow").classList.replace("noShow", "show");
    } else {
      document.querySelector(".show").classList.replace("show", "noShow");
    }
    setCamToggle((prev) => !prev);
  }, [camToggle]);

  const handleMicToggle = useCallback(() => {
    myStream
      .getAudioTracks()
      .forEach((track) => (track.enabled = !myMicToggle));
    myStream.getAudioTracks().forEach((track) => console.log(track.enabled));
    // console.log(myStream.getAudioTracks());
    // console.log(myMicToggle);
    if (myMicToggle) {
      document
        .querySelector(".myMicOn")
        .classList.replace("myMicOn", "myMicOff");
    } else {
      document
        .querySelector(".myMicOff")
        .classList.replace("myMicOff", "myMicOn");
    }
    setMyMicToggle(!myMicToggle);
  }, [myMicToggle]);

  useEffect(() => {
    getCameras1();
  }, []);

  function handleError() {
    if (++errorCnt >= 10) {
      // 에러가 10회이상일 경우 해당 요청 취소 및 알람
      const stopReqData = { gender: USERSEX, userId: userId };
      const axiosInstance = CustomAxios;

      axiosInstance.post("/api/match/stop", stopReqData);
      if (!alert("잠시후 다시 한번 시도해 주세요!")) stopMatching();
    }
  }

  function handleAccept(pId, sId) {
    if (window.confirm("[매칭 성공]\n미팅 페이지로 이동합니다.")) {
      const acceptReqData = {
        Id: userId,
        partnerId: pId,
        sessionId: sId,
        myGender: USERSEX,
      };
      const axiosInstance = CustomAxios;

      axiosInstance
        .post("/api/match/accept", acceptReqData)
        // 데이터가 정상적으로 온다면 -> res 데이터 안씀 + meeting In으로 넘어감
        .then((res) => {
          // resData인 관심사 배열을 store에 저장(meeting In에서 사용할 것)
          dispatch(PARTNERINTERESTS(res.data.partnerInterests));
          dispatch(PARTNERNICK(res.data.partnerNickname));

          if (!firstRendering) {
            firstRendering = true;

            // 캐칭 페이지로 이동
            catchingAnimation();

            // 7초 뒤에 meetingIn페이지 이동(toggleChange 메서드)
            const timer = setTimeout(() => {
              clearInterval(mainTimer);
              clearTimeout(timer);
              toggleChange();
            }, 7 * 1000);
          }
        })
        // 실패했을 경우 에러 반환 => 인터벌 안닫힘, 다시 요청해야함
        .catch((error) => {
          handleError();
        });
    } else {
      // 취소 버튼을 눌렀을 때, axios:decline
      dispatch(ROOM_NUM(""));
      const axiosInstance = CustomAxios;

      axiosInstance("/api/match/decline")
        .then((res) => {
          clearInterval(mainTimer);
          navigate("/home");
        })
        .catch((err) => {
          console.log(`error, decline error 발생`);
        });
    }
  }

  // 아래 코드는 axios 통신 시 사용할 코드
  function actionStart() {
    // dispatch(MTOGGLE(true));
    const checkReqData = {
      gender: USERSEX,
      lat: myGeo.lat,
      lng: myGeo.lng,
      userId: userId,
    };
    const axiosInstance = CustomAxios;
    axiosInstance
      .post("/api/match/check", checkReqData)
      .then((res) => {
        // [check resData : myGender, parnerId, sessionId]
        // [남자]
        if (USERSEX === "M" && res.data.myGender) {
          if (res.data.partnerId && res.data.sessionId) {
            dispatch(ROOM_NUM(res.data.sessionId));
            handleAccept(res.data.partnerId, res.data.sessionId);
          } else {
            console.log(errorCnt);
            if (++errorCnt >= 10) {
              handleError();
            }
          }
        }
        // [여자]
        else {
          if (res.data.partnerId && res.data.myGender) {
            // 방번호(sessionId) 생성
            let makingRoomName = makeRoomName();
            console.log(makingRoomName);

            // 성공했을 때 store에 방번호 저장하기
            dispatch(ROOM_NUM(makingRoomName));
            handleAccept(res.data.partnerId, res.data.sessionId);
          } else {
            console.log("check OK, 남자와 매칭이 안됐을 경우", ++errorCnt);
            if (errorCnt >= 10) {
              clearInterval(mainTimer);
              mainTimer = null;
            }
          }
        }
      })
      // check axios 통신이 아예 안되는 경우 => 인터벌 안닫힘, 다시 요청해야함
      .catch((err) => {
        console.log("check 실패 ", errorCnt);
        handleError();
      });
  }

  function stopMatching() {
    dispatch(ROOM_NUM(""));
    console.log(`stopMatching 발동`);
    myStream.getTracks().forEach((track) => track.stop());
    document.querySelector(".MMyCamDiv3").srcObject = null;
    myStream = "";
    clearInterval(mainTimer);
    mainTimer = null;
    navigate("/home");
  }

  return (
    <div className="MeetingNotIn">
      <div className="DarkBlurDiv"></div>
      <div className="MCenterDiv">
        <div className="MCenterImgDiv"></div>
        <div className="MCenterTitle">Searching Other :)</div>
        <div className="MCenterDesc">Please wait For 5 minutes.</div>
        <div className="MCenterCirclesDiv">
          <div className="MPCenterCircle1"></div>
          <div className="MPCenterCircle2"></div>
          <div className="MPCenterCircle3"></div>
        </div>
      </div>
      <div className="MLeftDiv">
        <div className="MMyCamLabel">My Camera</div>
        <div className="MMyCamDiv2 noShow"></div>
        <div
          className="MMyCamSubCamToggleBtn noShow"
          onClick={handleCamToggle}
        ></div>
        <div
          className="MMyCamSubMicBtn1 myMicOn"
          onClick={handleMicToggle}
        ></div>
        <video className="MMyCamDiv3 show" autoPlay playsInline></video>
      </div>
      {!isMatching ? (
        <div className="MStopBtnDiv" onClick={stopMatching}>
          <span className="MStopBtnText">Stop</span>
        </div>
      ) : (
        ""
      )}
    </div>
  );
}

export default MeetingNotIn;
