import Header from "../../components/Header";
import "./index.css";
import BlurInfo from "./BlurInfo/blurInfo";
import { useCallback, useEffect, useState } from "react";
import ModalWrap from "../Start/ModalWrap/modalWrap";
import Alert from "../Start/Alert";
import Slide1 from "./Slide1/slide1";
import Slide2 from "./Slide2/slide2";
import Slide3 from "./Slide3/slide3";
import ChatList from "./Chat/ChatList/chatlist";
import ChatPage from "./Chat/ChatPage/chatpage";
import { useRef } from "react";
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

let myStream;
function Home() {
  const API_URL = `${process.env.REACT_APP_API_ROOT_WONWOONG}/blur-match/match`;
  console.log(API_URL);
  useEffect(() => {
    console.log(process.env);
  }, []);
  // startVideo 함수 실행하면 자신의 모습 볼수있음
  const CONSTRAINTS = { video: { width: { exact: 440 }, height: { exact: 340 } } };
  const videoRef = useRef(null);

  useEffect(() => {}, []);
  const startVideo = useCallback(async () => {
    myStream = await navigator.mediaDevices.getUserMedia(CONSTRAINTS);
    if (videoRef && videoRef.current && !videoRef.current.srcObject) {
      videoRef.current.srcObject = myStream;
    } else {
      videoRef.current.srcObject = null;
    }
  }, []);

  const [blurInfoModal, setBlurInfoModal] = useState(false);
  const [alertModal, setalertModal] = useState(false);
  const [chatList, setChatList] = useState(false);
  const [chatPage, setChatPage] = useState(false);
  const [slideNumber, setSlideNumber] = useState(0);

  //프로필 설정이 완료여부 알려주는 변수
  const profiled = useSelector((state) => state.strr.profiled);
  const navigate = useNavigate();

  const showBlurInfoModal = () => {
    setBlurInfoModal((pre) => !pre);
  };

  const showAlertModal = () => {
    setalertModal((pre) => !pre);

    if (alertModal) {
      navigator.geolocation.getCurrentPosition((loc) => {
        console.log(`lat: ${loc.coords.latitude}, lng: ${loc.coords.longitude}`);
        axios({
          method: "post",
          url: `${API_URL}/start`,
          data: { lat: loc.coords.latitude, lng: loc.coords.longitude, userId: "anonymous", gender: "M" },
        })
          .then((res) => {
            console.log(`res : `, res.data);
          })
          .catch((err) => {
            console.log(err);
          });
      });

      myStream.getTracks().forEach((track) => track.stop());
      videoRef.srcObject = null;
    }
  };

  const showChatList = () => {
    setChatList((pre) => !pre);
  };

  const showChatPage = () => {
    setChatPage((pre) => !pre);
  };

  //캐러셀 화면
  useEffect(() => {
    setTimeout(() => setSlideNumber((pre) => (pre + 1) % 3), 10000);
  }, [slideNumber]);

  //Start 버튼에서 미팅으로 갈지, 프로필로 갈지
  const goMeeting = () => {
    if (profiled) {
      navigate("/meeting");
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
      {chatList ? <ChatList showChatPage={showChatPage} /> : null}
      {chatPage ? <ChatPage /> : null}
      {blurInfoModal || alertModal ? <ModalWrap blurInfoModal={blurInfoModal} showBlurInfoModal={showBlurInfoModal} /> : null}
      {blurInfoModal && !alertModal ? <BlurInfo /> : null}

      {alertModal && !blurInfoModal ? <Alert showAlertModal={goMyInfo} content={"프로필 설정을 하지 않으셨습니다. 작성 페이지로 이동합니다."} /> : null}

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
      </div>
    </div>
  );
}

export default Home;
