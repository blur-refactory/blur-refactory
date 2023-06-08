import "./index.css";
import { useDispatch } from "react-redux";
import { CAM_OPEN_TOGGLE } from "../../../redux/reducers/MToggle";
// import { useState } from "react";
function SettingModal() {
  const dispatch = useDispatch();
  // const [checkOptionValue, setCheckOptionValue] = useState("");
  const confirmClick = () => {
    // 백엔드에게 선택한 option값을 보냄
    // 만약 checkOptionValue값이 빈값일 때 -> 원래 선택했던것을 선택했음 -> 변경이 안되는걸로
    // console.log(checkOptionValue);

    // 창 닫기
    dispatch(CAM_OPEN_TOGGLE(false));
  };
  // function catchingValue(e) {
  //   setCheckOptionValue(e.target[e.target.value - 1].innerText);
  // }

  return (
    <div className="camSettingModal">
      <div className="camAlertHeader">
        <span className="camAlertHeaderText">카메라 설정</span>
      </div>
      <div className="camAlertBody">
        <select
          className="camAlertSelect"
          // onChange={catchingValue}
        >
          <option value="1">Logitech 3.0</option>
          <option value="2">Desktop Cam</option>
        </select>
        <div className="camConfirmBtnDiv" onClick={confirmClick}>
          <span className="camConfirmBtnText">확인</span>
        </div>
      </div>
    </div>
  );
}

export default SettingModal;
