import { useState } from "react";
import "./index.css";
import axios from "axios";

function SearchPw({ showSignInModal, showSearchPwModal, showAlertModal }) {
  const API_URL = `${process.env.REACT_APP_API_ROOT_DONGHO}`;

  const [spId, setSpId] = useState(null);

  const callSearchPwCheck = () => {
    axios({
      method: "put",
      url: `${API_URL}/auth/findPassword`,
      data: {
        userId: spId,
      },
    })
      .then((res) => {
        console.log(res);
        showAlertModal();
        showSearchPwModal();
      })
      .catch((err) => {
        console.log(err);
        alert("ID를 정확히 입력해 주세요");
      });
  };

  return (
    <div className="SPModal">
      <div className="SPModalHeader">
        <span className="SPModalHeaderText">Searching Password</span>
        <button
          className="SPModalClose"
          onClick={() => {
            showSignInModal();
            showSearchPwModal();
          }}
        ></button>
      </div>
      <div className="SPModalInputIdDiv">
        <label className="SPModalInputIdLabel">ID</label>
        <input
          className="SPModalInputId"
          placeholder="ID를 입력해 주세요"
          onChange={(e) => setSpId(e.target.value)}
        ></input>
      </div>

      <button
        className="SPConfirmBtn"
        onClick={() => {
          callSearchPwCheck();
        }}
      >
        임시비밀번호 이메일로 전송하기
      </button>
      <button
        className="SPCancleBtn"
        onClick={() => {
          showSignInModal();
          showSearchPwModal();
        }}
      >
        취소
      </button>
    </div>
  );
}

export default SearchPw;
