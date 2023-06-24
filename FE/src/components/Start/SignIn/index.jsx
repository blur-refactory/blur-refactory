import "./index.css";
import { useState } from "react";
import axios from "axios";
import { loginId, saveLogin } from "../../../redux/reducers/saveToken";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { useRef } from "react";
import { useSelector } from "react-redux";

function SignIn({ showSignUpModal, showSignInModal, showSearchPwModal, ref }) {
  let API_URL = `${process.env.REACT_APP_API_ROOT_DONGHO}`;
  // console.log(API_URL);
  const SOCIAL_API_URL = process.env.REACT_APP_SOCIAL_SIGN_API_URL;
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const savedId = useSelector((state) => state.strr.id);
  const checkbox = useRef();

  const [signId, setSignId] = useState("");
  const [signPs, setSignPs] = useState("");

  const handleLogin = () => {
    if (signId && signPs) {
      axios({
        method: "post",
        url: `${API_URL}/auth/login`,
        data: {
          email: signId,
          password: signPs,
        },
      })
        .then((res) => {
          // if (res.data.header.code === 407) {
          //   alert(res.data.header.message);
          // } else if (res.data.header.code === 408) {
          //   alert(res.data.header.message);
          // } else {
          console.log("로그인 성공", res);

          dispatch(saveLogin(true));
          // dispatch(loginId(signId));
          // if (checkbox.current.checked) {
          //   dispatch(saveId(signId));
          // } else {
          //   dispatch(saveId(""));
          // }
          navigate("/home");
          alert("로그인에 성공했습니다.");
          // }
        })
        .catch((err) => {
          alert("에러가 발생했습니다.", err);
        });
    } else {
      alert("아이디와 비밀번호를 입력해주세요.");
    }
  };

  //소셜 로그인 함수
  const socialSignIn = (socialType) => {
    return `${SOCIAL_API_URL}/oauth2/authorization/${socialType}`;
  };

  return (
    <div className="SIModal">
      <div className="SIModalHeader">
        <span className="SIModalHeaderText">Sign In</span>
        <button className="SIModalClose" onClick={showSignInModal}></button>
      </div>

      <div className="ModalInputIdDiv">
        <label className="ModalInputIdLabel" htmlFor="user_id">
          ID
        </label>
        <input
          className="ModalInputId"
          id="user_id"
          placeholder="ID를 입력해 주세요"
          onChange={(e) => setSignId(e.target.value)}
          defaultValue={savedId}
        ></input>
      </div>
      <div className="ModalInputPwDiv">
        <label className="ModalInputPwLabel" htmlFor="user_pw">
          PW
        </label>
        <input
          className="ModalInputPw"
          id="user_pw"
          placeholder="PW를 입력해 주세요"
          type="password"
          onChange={(e) => setSignPs(e.target.value)}
        ></input>
      </div>

      <div className="LoginBtnDiv">
        <button
          className="SISignUpBtn"
          onClick={() => {
            showSignInModal();
            showSignUpModal();
          }}
        >
          회원가입
        </button>
        <button
          className="SISearchingPwBtn"
          onClick={() => {
            showSearchPwModal();
            showSignInModal();
          }}
        >
          비밀번호찾기
        </button>
        <button className="LoginBtn" onClick={handleLogin}>
          로그인
        </button>
        {/* <div className="IdSaveDiv">
          <input className="IdSaveToggle" type="checkbox" ref={checkbox}></input>

          <label className="IdSaveText">아이디 저장</label>
        </div> */}
      </div>

      <button
        className="KakaoLoginBtn"
        onClick={(e) => {
          e.preventDefault();
          window.location.href = socialSignIn("kakao");
        }}
      >
        카카오로 로그인
      </button>

      <button
        className="GoogleLoginBtn"
        onClick={(e) => {
          e.preventDefault();
          window.location.href = socialSignIn("google");
        }}
      >
        구글로 로그인
      </button>
      <div className="PlaceHolder"></div>
    </div>
  );
}

export default SignIn;
