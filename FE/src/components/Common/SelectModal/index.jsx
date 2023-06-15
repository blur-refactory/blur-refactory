import "./index.css";
// import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { BTOGGLE, CAM_OPEN_TOGGLE, CLOSE_ALERT_TOGGLE } from "../../../redux/reducers/MToggle";
export default function SelectModal({ headerMsg, data, confirmMsg, status }) {
  // const [checkOptionValue, setCheckOptionValue] = useState("");
  const dispatch = useDispatch();
  const isShowBlockModal = useSelector((state) => state.mt.isShowBlockModal);
  const camOpenToggle = useSelector((state) => state.mt.camOpenToggle);

  const confirmClick = () => {
    // 백엔드에게 선택한 option값(checkOptionValue)을 보내고, 성공시
    // console.log(checkOptionValue);

    if (status === "camera") {
      dispatch(CAM_OPEN_TOGGLE(!camOpenToggle));
      // 백엔드에게 선택한 option값을 보냄
      // 만약 checkOptionValue값이 빈값일 때 -> 원래 선택했던것을 선택했음 -> 변경이 안되는걸로
    } else if (status === "block") {
      // // 창 닫기
      dispatch(BTOGGLE(!isShowBlockModal));
      // // 신고되었습니다 알람창 띄우기
      dispatch(CLOSE_ALERT_TOGGLE(true));
    }
  };

  const closeBlockModal = () => {
    if (status === "camera") {
      dispatch(CAM_OPEN_TOGGLE(!camOpenToggle));
    } else if (status === "block") {
      dispatch(BTOGGLE(!isShowBlockModal));
    }
  };

  // function catchingValue(e) {
  //   setCheckOptionValue(e.target[e.target.value - 1].innerText);
  // }

  function createOptions() {
    const arr = [];
    data.map((el) => {
      return arr.push(
        <option key={el.index} value={el.index}>
          {el.desc}
        </option>
      );
    });
    return arr;
  }

  return (
    <div className="BlockModal">
      <div className="AlertHeader">
        <span className="AlertHeaderText">{headerMsg}</span>
      </div>
      <div className="AlertBody">
        <select
          className="BlockAlertSelect"
          // onChange={catchingValue}
        >
          {createOptions()}
        </select>
        <div className="BlockConfirmBtnDiv" onClick={confirmClick}>
          <span className="BlockConfirmBtnText">{confirmMsg}</span>
        </div>
        <div className="BlockCancleBtnDiv" onClick={closeBlockModal}>
          <span className="BlockCancleBtnText">취소</span>
        </div>
      </div>
    </div>
  );
}
