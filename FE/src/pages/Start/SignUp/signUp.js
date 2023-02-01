import { useEffect, useRef, useState } from "react";
import "./signUp.css";
import axios from "axios";

function SignUp({ showSignUpModal, showSignInModal }) {
  const API_URL = "http://localhost:8080";

  const [id, setId] = useState(null);
  const enterId = (e) => {
    setId(e.target.value);
    console.log(id);
  };

  const [ps1, setPs1] = useState(null);
  const enterPs1 = (e) => {
    setPs1(e.target.value);
    console.log(ps1);
  };

  const [ps2, setPs2] = useState(null);
  const enterPs2 = (e) => {
    setPs2(e.target.value);
    console.log(ps2);
  };

  const [email, setEmail] = useState(null);
  const enterEmail = (e) => {
    setEmail(e.target.value);
    console.log(email);
  };

  const [emailCode, setEmailCode] = useState(null);
  const enterEmailCode = (e) => {
    setEmailCode(e.target.value);
    console.log(emailCode);
  };

  const [idCheck, setIdCheck] = useState(false);
  const callIdCheck = () => {
    axios({
      method: "post",
      url: `${API_URL}/checkId`,
      data: {
        userId: id,
      },
    })
      .then((res) => {
        console.log(res);

        if (!res.data) {
          alert("아이디가 중복되었습니다");
        } else {
          alert("사용가능한 아이디입니다.");
          setIdCheck(res.data);
        }
      })
      .catch((err) => {
        alert("중복확인 실패했습니다");
        setIdCheck(true);
      });
  };

  const [psCheck, setPsCheck] = useState(false);
  const [psWarn, setPsWarn] = useState(false);
  const callPsCheck = (ps1, ps2) => {
    if (ps1 === ps2) {
      setPsCheck(true);
      setPsWarn(false);
    } else {
      setPsCheck(false);
      setPsWarn(true);
    }
  };

  useEffect(() => {
    callPsCheck(ps1, ps2);
  }, [ps1, ps2]);

  const [emailCheck, setEmailCheck] = useState(false);

  const sendToEmail = () => {
    axios({
      method: "post",
      url: `${API_URL}/sendAuthEmail`,
      data: {
        email: email,
      },
    })
      .then((res) => {
        console.log(res);
        setEmailCheck(true);
        alert("인증번호를 보냈습니다!");
      })
      .catch((err) => {
        console.log(err);
        alert("인증번호를 보내지 못했습니다.");
      });
  };

  const [emailCodeCheck, setEmailCodeCheck] = useState(false);

  const onSubmit = (e) => {
    e.preventDefault();
    if (
      id &&
      ps1 &&
      email &&
      emailCode &&
      idCheck &&
      psCheck &&
      emailCheck &&
      emailCodeCheck
    ) {
      axios({
        method: "post",
        url: `${API_URL}/register`,
        data: {
          userId: id,
          password: ps1,
          email: email,
        },
      })
        .then((res) => {
          console.log(res);
        })
        .catch((err) => {
          console.log(err);
        });
    } else {
      alert("아이디중복 또는 비밀번호불일치 또는 이메일확인코드오류 입니다.");
    }
  };

  useEffect(() => {
    if (idCheck === true) {
      alert("아이디가 바뀌었습니다. 다시 중복확인 해주세요");
      setIdCheck(false);
    }
  }, [id]);

  useEffect(() => {
    if (emailCheck === true) {
      alert("이메일이 바뀌었습니다. 다시 인증코드 보내세요");
      setEmailCheck(false);
    }
  }, [email]);

  useEffect(() => {
    if (emailCodeCheck === true) {
      alert("이메일인증코드가 바뀌었습니다. 다시 인증코드 보내세요");
    }
    setEmailCodeCheck(false);
  }, [emailCode]);

  const signUpButton = useRef(null);

  useEffect(() => {
    if (
      id &&
      ps1 &&
      email &&
      emailCode &&
      idCheck &&
      psCheck &&
      emailCheck &&
      emailCodeCheck
    ) {
      signUpButton.current.disabled = false;
      signUpButton.current.style.background = "#50a1a3";
    } else {
      signUpButton.current.disabled = true;
      signUpButton.current.style.background = "grey";
    }
  }, [id, ps1, email, emailCode, idCheck, psCheck, emailCheck, emailCodeCheck]);

  return (
    <div className="SUModal">
      <div className="SUModalHeader">
        <h3 className="SUModalHeaderText">Sign Up</h3>
      </div>
      <form>
        <div className="SUModalInputIdDiv">
          <label className="SUModalInputIdLabel" htmlFor="user_id">
            ID
          </label>

          <input
            className="SUModalInputId"
            id="user_id"
            placeholder="  ID를 입력해 주세요"
            onChange={enterId}
          ></input>
          <button

            onClick={(e) => {
              return e.preventDefault(), callIdCheck();
            }}

          >
            아이디 중복체크
          </button>
        </div>
        <div className="SUModalInputPwDiv">
          <label className="SUModalInputPwLabel" htmlFor="user_pw">
            PW
          </label>

          <input
            className="SUModalInputPw"
            id="user_pw"
            placeholder="  PW를 입력해 주세요"
            onChange={enterPs1}
          ></input>

        </div>
        <div className="SUModalInputPwChkDiv">
          <label className="SUModalInputPwChkLabel" htmlFor="user_pw_re">
            PW Check
          </label>

          <input
            className="SUModalInputPwChk"
            id="user_pw_re"
            placeholder="  PW를 다시 입력해 주세요"
            onChange={enterPs2}
          ></input>

          {psWarn ? <span>비밀번호가 다릅니다!</span> : null}
        </div>
        <div className="SUModalInputEmailDiv">
          <label className="SUModalInputEmailLabel" htmlFor="user_email">
            E-mail
          </label>

          <input
            className="SUModalInputEmail"
            id="user_email"
            placeholder="  E-mail을 입력해 주세요"
            onChange={enterEmail}
          ></input>
          <button
<<<<<<< HEAD
            onClick={(e) => {
              return e.preventDefault(), sendToEmail();
            }}
=======

          // style={{ cursor: "pointer" }}
          // onClick={(e) => {
          //   e.preventDefault(), sendToEmail();
          // }}

>>>>>>> ce4c4a5fa23caa8fe08224125ac7520b553b8c7e
          >
            이메일로 인증번호 보내기
          </button>
        </div>
        <div className="SUModalInputEmailConfirmDiv">
          <label className="SUModalInputEmailConfirmLabel" htmlFor="user_email_confirm">
            E-mail 인증번호
          </label>

          <input
            className="SUModalInputEmailConfirm"
            id="user_email_confirm"
            placeholder="  인증번호를 입력해 주세요"
            onChange={enterEmailCode}
          ></input>
<<<<<<< HEAD
          <button onClick={(e) => e.preventDefault()}>인증번호 확인</button>
        </div>

        <button className="SUSignUpBtn" ref={signUpButton} onClick={onSubmit}>
          {/* <span className="SUBtnText">회원가입</span> */}
          회원가입
=======
          <button
            style={{ cursor: "pointer" }}
            onClick={(e) => e.preventDefault()}
          >

            인증번호 확인
          </button>
        </div>

        <button className="SUSignUpBtn" style={{ cursor: "pointer" }} onClick={onSubmit}>
          <span className="SUBtnText">회원가입</span>
>>>>>>> ce4c4a5fa23caa8fe08224125ac7520b553b8c7e
        </button>
      </form>

      <button
        className="SUCancleBtn"
        onClick={() => {
          showSignUpModal();
          showSignInModal();
        }}
      >
        <span className="SUCancleBtnText">취소</span>
      </button>
      <div className="PlaceHolder"></div>
    </div>
  );
}

export default SignUp;
