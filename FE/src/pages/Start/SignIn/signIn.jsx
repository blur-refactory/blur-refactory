import "./signIn.css";
import { useState } from "react";
import axios from "axios";
import { loginId, saveToken } from "../../../redux/reducers/saveToken";
import { saveId } from "../../../redux/reducers/saveToken";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { useRef } from "react";
import { useSelector } from "react-redux";

function SignIn({ showSignUpModal, showSignInModal, showSearchPwModal }) {
  const API_URL = `${process.env.REACT_APP_API_ROOT_DONGHO}/blur-auth/auth`;
  const SOCIAL_API_URL = "blur-auth";
  // const API_URL = process.env.REACT_APP_SIGN_API_URL;
  // const SOCIAL_API_URL = process.env.REACT_APP_SOCIAL_SIGN_API_URL;
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const savedId = useSelector((state) => state.strr.id);
  const checkbox = useRef();

  const [signId, setSignId] = useState("");
  const [signPs, setSignPs] = useState("");

  const enterSignId = (e) => {
    setSignId(e.target.value);
    console.log(signId);
  };

  const enterSignPs = (e) => {
    setSignPs(e.target.value);
    console.log(signPs);
  };

  //로그인 함수
  const signIn = () => {
    if (signId && signPs) {
      axios({
        method: "post",
        url: `${API_URL}/login`,
        data: {
          userId: signId,
          password: signPs,
        },
      })
        .then((res) => {
          console.log(res);
          dispatch(saveToken(res.data.body.token));
          dispatch(loginId(signId));
          if (checkbox.current.checked) {
            console.log("디스패치");

            dispatch(saveId(signId));
          } else {
            console.log("초기화");
            dispatch(saveId(""));
          }

          navigate("/home");
        })
        .catch((err) => {
          console.log(err);
          alert("에러가 발생했습니다.");
        });
    } else {
      alert("아이디와 비밀번호를 입력해주세요");
    }
  };

  //소셜 로그인 함수
  const socialSignIn = (socialType) => {
    return `${SOCIAL_API_URL}/oauth2/authorization/${socialType}?redirect_uri=http://localhost:3000/oauth/redirect`;
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
        <input className="ModalInputId" id="user_id" placeholder="ID를 입력해 주세요" onChange={enterSignId} defaultValue={savedId}></input>
      </div>
      <div className="ModalInputPwDiv">
        <label className="ModalInputPwLabel" htmlFor="user_pw">
          PW
        </label>
        <input className="ModalInputPw" id="user_pw" placeholder="PW를 입력해 주세요" type="password" onChange={enterSignPs}></input>
      </div>

      <div className="LoginBtnDiv">
        <button className="LoginBtn" onClick={signIn}>
          로그인
        </button>
        <div className="IdSaveDiv">
          <input className="IdSaveToggle" type="checkbox" ref={checkbox}></input>

          <label className="IdSaveText">아이디 저장</label>
        </div>
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
        className="NaverLoginBtn"
        onClick={(e) => {
          e.preventDefault();
          window.location.href = socialSignIn("naver");
        }}
      >
        네이버로 로그인
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
