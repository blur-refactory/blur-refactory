import "./index.css";
// import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { BTOGGLE, CLOSE_ALERT_TOGGLE } from "../../../redux/reducers/MToggle";
function BlockModal() {
  // const [checkOptionValue, setCheckOptionValue] = useState("");
  const dispatch = useDispatch();
  const isShowBlockModal = useSelector((state) => state.mt.isShowBlockModal);

  const confirmClick = () => {
    // 백엔드에게 선택한 option값(checkOptionValue)을 보내고, 성공시
    // console.log(checkOptionValue);

    // 창 닫기 하고
    dispatch(BTOGGLE(!isShowBlockModal));

    // 신고되었습니다 알람창 띄우기
    dispatch(CLOSE_ALERT_TOGGLE(true));
  };

  const closeBlockModal = () => {
    dispatch(BTOGGLE(!isShowBlockModal));
  };

  // function catchingValue(e) {
  //   setCheckOptionValue(e.target[e.target.value - 1].innerText);
  // }

  return (
    <div className="BlockModal">
      <div className="AlertHeader">
        <span className="AlertHeaderText">신고 사유</span>
      </div>
      <div className="AlertBody">
        <select
          className="BlockAlertSelect"
          // onChange={catchingValue}
        >
          <option value="1">불쾌한 노출</option>
          <option value="2">부적절한 언행</option>
          <option value="3">사용 불가 연령</option>
          <option value="4">불편한 행동</option>
        </select>
        <div className="BlockConfirmBtnDiv" onClick={confirmClick}>
          <span className="BlockConfirmBtnText">신고하기</span>
        </div>
        <div className="BlockCancleBtnDiv" onClick={closeBlockModal}>
          <span className="BlockCancleBtnText">취소</span>
        </div>
      </div>
    </div>
  );
}

export default BlockModal;
