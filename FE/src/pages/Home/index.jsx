/* eslint-disable no-unused-vars */
import "./index.css";

import { useEffect, useState } from "react";
import { useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { MYGENDER, MYGEO } from "../../redux/reducers/MToggle";
import { saveToken, ISMYPROFILE } from "../../redux/reducers/saveToken";

import Header from "../../components/Common/Header";
import BlurInfo from "../../components/Home/Info";
import ModalWrap from "../../components/Start/ModalWrap";
import Alert from "../../components/Start/Alert";
import Slide1 from "../../components/Home/Carousel/Slide1";
import Slide2 from "../../components/Home/Carousel/Slide2";
import Slide3 from "../../components/Home/Carousel/Slide3";
import Slide4 from "../../components/Home/Carousel/Slide4";
import Slide5 from "../../components/Home/Carousel/Slide5";
import ChatList from "../../components/Chat/ChatList";
import ChatPage from "../../components/Chat/ChatPage";
import { CustomAxios } from "../../api/CustomAxios";
let myStream;
let carousel;
function Home() {
  let userId = useSelector((state) => state.strr.id); // Redux에 저장되어있는 MToggle
  let myToken = useSelector((state) => state.strr.token); // store에 저장되어있는 토큰
  const profiled = useSelector((state) => state.strr.profiled);

  const [toggleStartVideo, setToggleStartVideo] = useState(false);
  const [blurInfoModal, setBlurInfoModal] = useState(false);
  const [alertModal, setalertModal] = useState(false);
  const [chatList, setChatList] = useState(false);
  const [chatPage, setChatPage] = useState(false);
  const [slideNumber, setSlideNumber] = useState(0);

  const dispatch = useDispatch();
  const navigate = useNavigate();

  // startVideo 함수 실행하면 자신의 모습 볼수있음
  const videoRef = useRef(null);
  const CONSTRAINTS = {
    video: { width: { exact: 50 }, height: { exact: 36 } },
  };

  const startVideo = async () => {
    if (toggleStartVideo === false) {
      setToggleStartVideo(!setToggleStartVideo);
      myStream = await navigator.mediaDevices.getUserMedia(CONSTRAINTS);
      if (videoRef && videoRef.current && !videoRef.current.srcObject) {
        videoRef.current.srcObject = myStream;
      } else {
        videoRef.current.srcObject = null;
      }
    }
  };

  const showBlurInfoModal = () => {
    setBlurInfoModal((pre) => !pre);
  };

  const showAlertModal = () => {
    setalertModal((pre) => !pre);

    if (alertModal) {
      myStream.getTracks().forEach((track) => track.stop());
      videoRef.srcObject = null;
    }
  };

  const showChatList = () => {
    setChatList((pre) => !pre);
    if (chatPage) {
      setChatPage(false);
    }
  };

  const showChatPage = () => {
    setChatPage((pre) => !pre);
    if (chatList) {
      setChatList(false);
    }
  };

  //캐러셀 화면
  useEffect(() => {
    carousel = setTimeout(() => setSlideNumber((pre) => (pre + 1) % 5), 10000);
  }, [slideNumber]);

  useEffect(() => {
    const axiosInstance = CustomAxios;
    axiosInstance
      .get(`/api/profile/${userId}/check`)
      .then((res) => {
        dispatch(ISMYPROFILE(res.data));
      })
      .catch((err) => console.log(err));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  //Start 버튼에서 미팅으로 갈지, 프로필로 갈지
  const goMeeting = () => {
    /**
     * 프로필 등록 O
     * - 데이터(아이디/위도/경도)를 넘겨줌
     * - axios 성공 시 : 미팅 페이지로 이동
     *         실패 시 : 알람 띄움
     */
    // console.log(`profiled: ${profiled}`);
    // dispatch(MTOGGLE(true));
    // navigate("/meeting");

    if (profiled) {
      // meeting Not In 로 이동
      if (!alert("미팅 대기 페이지로 이동합니다.")) {
        // 데이터 백에 넘겨줌
        navigator.geolocation.getCurrentPosition((loc) => {
          console.log(
            `lat: ${loc.coords.latitude}, lng: ${loc.coords.longitude}`
          );
          dispatch(
            MYGEO({ lat: loc.coords.latitude, lng: loc.coords.longitude })
          );
          const matchStartReqData = {
            lat: loc.coords.latitude,
            lng: loc.coords.longitude,
            userId: userId,
          };
          CustomAxios.post(`/api/match/start`, matchStartReqData)
            .then((res) => {
              // [response data : myGender/partnerId/sessionId(방번호)] - OXX
              dispatch(MYGENDER(res.data.myGender));
              alert("start: 백에 통신 성공");

              // 성공 시 미팅 페이지로 이동
              clearTimeout(carousel);
              navigate("/meeting");
            })
            .catch((err) => {
              console.log(err);
              alert(
                "error\n서버와 통신에 실패했습니다.\n잠시후 다시 한번 시도해 주세요!"
              );
            });
        });
      }
    } else {
      showAlertModal();
    }
  };
  //프로필 설정페이지로 가는 함수
  const goMyInfo = () => {
    navigate("/MyInfo");
  };

  return (
    <div className="Home">
      {chatList && !chatPage ? <ChatList showChatPage={showChatPage} /> : null}
      {chatPage && !chatList ? <ChatPage showChatPage={showChatPage} /> : null}

      {blurInfoModal || alertModal ? (
        <ModalWrap
          blurInfoModal={blurInfoModal}
          showBlurInfoModal={showBlurInfoModal}
        />
      ) : null}
      {blurInfoModal && !alertModal ? (
        <BlurInfo showBlurInfoModal={showBlurInfoModal} />
      ) : null}

      {alertModal && !blurInfoModal ? (
        <Alert
          showAlertModal={goMyInfo}
          content={"프로필 설정을 하지 않으셨습니다. 작성 페이지로 이동합니다."}
        />
      ) : null}

      <Header showChatList={showChatList} />

      <div className="HomeSubFrame">
        <div className="HomeLeftDiv">
          {/* <div className="HomeCamImg"></div> */}
          <video className="HomeCamDiv" autoPlay ref={videoRef} />
          <button className="InfoBlurBtn" onClick={showBlurInfoModal}></button>
          <button className="CamToggle" onClick={startVideo}></button>
          <button className="CommBoxFrame2" onClick={goMeeting}>
            <span className="CommBoxFrameStart">Blur Start</span>
          </button>
        </div>
        {slideNumber === 0 ? <Slide1 /> : null}
        {slideNumber === 1 ? <Slide2 /> : null}
        {slideNumber === 2 ? <Slide3 /> : null}
        {slideNumber === 3 ? <Slide4 /> : null}
        {slideNumber === 4 ? <Slide5 /> : null}
      </div>
      {/* <Test roomId="1" /> */}
    </div>
  );
}

export default Home;
