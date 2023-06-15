import "./index.css";

import { useState, useRef, useEffect } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

import Header from "../../components/Common/Header";
import SignIn from "../../components/Start/SignIn";
import ModalWrap from "../../components/Start/ModalWrap";
import SignUp from "../../components/Start/SignUp";
import SearchPw from "../../components/Start/SearchPw";
import Alert from "../../components/Common/Alert";

function Start() {
  const [signInModal, setSignInModal] = useState(false);
  const [signUpModal, setSignUpModal] = useState(false);
  const [searchPwModal, setSearchPwModal] = useState(false);
  const [alertModal, setalertModal] = useState(false);

  const userState = useSelector((state) => state.strr.token);
  const navigate = useNavigate();
  const ref1 = useRef(null);
  const ref2 = useRef(null);
  const ref3 = useRef(null);
  const ref4 = useRef(null);

  const showSignInModal = () => {
    setSignInModal((pre) => !pre);
  };

  const showSignUpModal = () => {
    setSignUpModal((pre) => !pre);
  };

  const showSearchPwModal = () => {
    setSearchPwModal((pre) => !pre);
  };

  const showAlertModal = () => {
    setalertModal((pre) => !pre);
  };

  const StartBtn = () => {
    if (userState) {
      navigate("/home");
    } else {
      showSignInModal();
    }
  };

  //스크롤 함수
  let page = 1;
  let selectedRef;

  const scrollFunction = (event) => {
    event.preventDefault();

    const deltaY = event.deltaY;
    const direction = deltaY > 0 ? "down" : "up"; //마우스 휠 방향

    if (direction === "down") {
      if (page === 4) {
        return;
      }
      page = page + 1;
    } else {
      if (page === 1) {
        return;
      }
      page = page - 1;
    }

    switch (page) {
      case 1:
        selectedRef = ref1.current;
        break;
      case 2:
        selectedRef = ref2.current;
        break;
      case 3:
        selectedRef = ref3.current;
        break;
      case 4:
        selectedRef = ref4.current;
        break;
      default:
        break;
    }

    selectedRef.scrollIntoView({ behavior: "smooth" });
  };

  const scrollFunctionRef = useRef(scrollFunction);
  const StartRef = useRef(null);

  useEffect(() => {
    if (!signUpModal && !signInModal && !searchPwModal && !alertModal) {
      StartRef.current.addEventListener("wheel", scrollFunctionRef.current, {
        passive: false,
      });
      document.body.style.overflow = "";
    } else {
      StartRef.current.removeEventListener("wheel", scrollFunctionRef.current);
      document.body.style.overflow = "hidden";
    }
  }, [signInModal, signUpModal, searchPwModal, alertModal, scrollFunctionRef]);

  //이미지 애니메이션.
  // useEffect(() => {
  //   const targets = document.querySelectorAll(".SubFrameImage");
  //   const options = {
  //     rootMargin: "0px",
  //     threshold: 0.5,
  //   };

  //   const observer = new IntersectionObserver((entries, observer) => {
  //     entries.forEach((entry) => {
  //       if (entry.isIntersecting) {
  //         entry.target.classList.add("animate");
  //       } else {
  //         entry.target.classList.remove("animate");
  //       }
  //     });
  //   }, options);

  //   targets.forEach((target) => {
  //     observer.observe(target);
  //   });
  // }, []);

  //텍스트 애니메이션.
  useEffect(() => {
    const targets = document.querySelectorAll(".SubFrameParagraphText");

    const options = {
      rootMargin: "0px",
      threshold: 0.5,
    };

    const observer = new IntersectionObserver((entries, observer) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          entry.target.classList.add("fade-up");
        } else {
          entry.target.classList.remove("fade-up");
        }
      });
    }, options);

    targets.forEach((target) => {
      observer.observe(target);
    });
  }, []);

  // 둥둥 떠있는 포인터 마지막 페이지에서는 사라지게 하기.
  useEffect(() => {
    const targets = [ref4.current];
    const scrollElement = document.querySelector(".scrollTemp");

    const options = {
      rootMargin: "0px",
      threshold: 0.5,
    };

    const observer = new IntersectionObserver((entries, observer) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          scrollElement.classList.remove("scroll");
        } else {
          scrollElement.classList.add("scroll");
        }
      });
    }, options);

    observer.observe(targets[0]);
  }, []);

  //마지막 페이지에서 첫 페이지로 가는 함수

  const goBlur = () => {
    ref1.current.scrollIntoView({ behavior: "smooth" });
    page = 1;
  };

  return (
    <div className="Start" ref={StartRef}>
      {signInModal || signUpModal || searchPwModal ? (
        <ModalWrap
          signInModal={signInModal}
          signUpModal={signUpModal}
          searchPwModal={searchPwModal}
          showSignInModal={showSignInModal}
          showSignUpModal={showSignUpModal}
          showSearchPwModal={showSearchPwModal}
        />
      ) : null}

      {signInModal && !signUpModal && !searchPwModal ? (
        <SignIn
          showSignUpModal={showSignUpModal}
          showSignInModal={showSignInModal}
          showSearchPwModal={showSearchPwModal}
        />
      ) : null}

      {signUpModal && !signInModal && !searchPwModal ? (
        <SignUp showSignUpModal={showSignUpModal} showSignInModal={showSignInModal} />
      ) : null}

      {searchPwModal && !signInModal && !signUpModal ? (
        <SearchPw
          showSignInModal={showSignInModal}
          showSearchPwModal={showSearchPwModal}
          showAlertModal={showAlertModal}
        />
      ) : null}

      {alertModal && !signInModal && !signUpModal && !searchPwModal ? (
        <Alert
          showAlertModal={showAlertModal}
          content={"임시비밀번호가 이메일로 전송되었습니다.\n임시비밀번호로 로그인해주세요."}
        />
      ) : null}

      <Header showSignInModal={showSignInModal} />
      <div className="SubFrame" ref={ref1}>
        <div className="SubLeftDiv">
          <span className="SubLeftTitle">Hey, Just Blur!</span>
          <span className="SubLeftDesc">Show me your own color.</span>

          <div
            className="CommBoxFrame1 color-5"
            onClick={StartBtn}
            disabled={alertModal === true ? true : false}
          >
            <span className="CommBoxFrameStart">Start</span>
          </div>
        </div>

        <div className="SubRightDiv">
          <div className="SubRightImg"></div>
        </div>
      </div>
      <div className="SubFrame2" ref={ref2}>
        <div className="SubFrameParagraph">
          <strong className="SubFrameParagraphText">
            <span>안녕하세요.</span>
            <br></br>
            <span>보여줄 수 없었던</span>
            <br></br>
            <span>내 모습을 보여줄 수 있는곳,</span>
            <br></br>
            <span>Blur입니다.</span>
          </strong>
        </div>
        <div className="SubFrameImage"></div>
      </div>
      <div className="SubFrame3" ref={ref3}>
        <div className="SubFrameImage"></div>
        <div className="SubFrameParagraph">
          <strong className="SubFrameParagraphText">
            <span>입구컷 당하는 당신</span>
            <br></br>
            <span>걱정마세요!</span>
          </strong>
        </div>
      </div>
      <div className="SubFrame4" ref={ref4}>
        <div className="SubFrameParagraph">
          <strong className="SubFrameParagraphText">
            <span> Blur에서 당신의 매력을 </span>
            <br />
            <span>마음껏 보여주세요!</span>
            <br />
            <br />
            <br />
            <span className="BlurGo" onClick={goBlur}>
              블러 하러가기 Click!
            </span>
          </strong>
        </div>
        <div className="SubFrameImage"></div>
      </div>
      <span className="scrollTemp"></span>
    </div>
  );
}

export default Start;
