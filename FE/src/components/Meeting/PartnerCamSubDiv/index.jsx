import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { BTOGGLE } from "../../../redux/reducers/MToggle";

export default function PartnerCamSubDiv({ handleHangUp }) {
  const [partnerSoundVal, setPartnerSoundVal] = useState(50);
  const [partnerSoundToggle, setPartnerSoundToggle] = useState(false);
  const [partnerMicToggle, setPartnerMicToggle] = useState(true);
  const [blockToggle, setBlockToggle] = useState(false);

  const isShowBlockModal = useSelector((state) => state.mt.isShowBlockModal);
  const partnerNick = useSelector((state) => state.mt.partnerNick);

  const dispatch = useDispatch();

  // 파트너 음량 조절
  const handlePartnerSoundSlider = () => {
    const slider = document.querySelector(".partSlider");
    const progress = document.querySelector(".partProgressSlider");
    setPartnerSoundVal(slider.value);
    const val = slider.value + "%";
    progress.style.width = val;
  };

  // 파트너 음량 토글
  const handleShowPartnerSound = () => {
    if (!partnerSoundToggle) {
      document.querySelector(".MPartenerCamSubSoundDesc").style.display = "block";
    } else {
      document.querySelector(".MPartenerCamSubSoundDesc").style.display = "none";
    }
    setPartnerSoundToggle((prev) => !prev);
  };

  // 파트너 마이크 토글
  const handleOpenPartnerMic = () => {
    if (partnerMicToggle) {
      document.querySelector(".partMicOn").classList.replace("partMicOn", "partMicOff");
    } else {
      document.querySelector(".partMicOff").classList.replace("partMicOff", "partMicOn");
    }
    setPartnerMicToggle((prev) => !prev);
  };

  // (파트너) 신고 모달 토글
  const handleShowBlockModal = () => {
    // blockModalToggle이 false 라면
    if (!isShowBlockModal) {
      // 1. 토글 버튼을 닫아주고
      setBlockToggle((prev) => !prev);
      // 1. 해당 버튼의 div를 none처리 해줌
      document.querySelector(".MPartenerCamSubBlockDesc").style.display = "none";
      dispatch(BTOGGLE(!isShowBlockModal));
    }
  };

  // 파트너 신고 토글
  const handleOpenBlock = () => {
    if (!blockToggle) {
      // 신고 div block으로 변경
      document.querySelector(".MPartenerCamSubBlockDesc").style.display = "block";
    } else {
      // 신고 div none으로 변경
      document.querySelector(".MPartenerCamSubBlockDesc").style.display = "none";
    }
    setBlockToggle((prev) => !prev);
  };

  return (
    <div className="MPartenerCamSubDiv">
      <span className="MPartenerCamSubText">{partnerNick} </span>
      <div className="MPartenerCamSubBtnsDiv">
        <div className="MPartenerCamSubExitBtn" onClick={handleHangUp}></div>
        <div className="MPartenerCamSubBlockBtn" onClick={handleOpenBlock}></div>
        <div className="MPartenerCamSubBlockDesc">
          <div className="MPartenerCamSubBlockDescTop"></div>
          <div className="MPartenerCamSubBlockDescMain" onClick={handleShowBlockModal}>
            <span className="MPartenerCamSubBlockDescText">Report</span>
          </div>
        </div>
        <div className="MPartenerCamSubMicBtn partMicOn" onClick={handleOpenPartnerMic}></div>
        <div className="MPartenerCamSubSoundBtn" onClick={handleShowPartnerSound}></div>
        <div className="MPartenerCamSubSoundDesc">
          <div className="MPartenerCamSubSoundDescTop"></div>
          <div className="MPartenerCamSubSoundDescMain"></div>
          <span className="MPartenerCamSubSoundDescSoundVal">{partnerSoundVal}</span>
          <div className="MPartenerCamSubSoundDescBar">
            <div className="part-range-slider">
              <input
                type="range"
                className="partSlider"
                min="0"
                max="100"
                onChange={handlePartnerSoundSlider}
              ></input>
              <div className="partProgressSlider"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
