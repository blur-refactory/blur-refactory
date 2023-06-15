import { useEffect } from "react";
import { useState } from "react";
import { useDispatch } from "react-redux";
import { CAM_OPEN_TOGGLE } from "../../../redux/reducers/MToggle";

export default function MyCamSubDiv({ camOff, micOff, myStream }) {
  const [mySoundVal, setMySoundVal] = useState(50);
  const [mysoundToggle, setMySoundToggle] = useState(false);
  const [myMicToggle, setMyMicToggle] = useState(true);
  const [camToggle, setCamToggle] = useState(true);

  const dispatch = useDispatch();

  // 나의 캠 세팅 토글
  const showSetting = () => {
    dispatch(CAM_OPEN_TOGGLE(true));
  };

  // 나의 음량 조절
  const onChangeMySoundSlider = () => {
    const slider = document.querySelector(".slider");
    const progress = document.querySelector(".progressSlider");
    setMySoundVal(slider.value);
    const val = slider.value + "%";
    progress.style.width = val;
  };

  useEffect(() => {
    console.log(`11`, myStream);
  });

  // 나의 음량 토글
  const showMySound = () => {
    if (!mysoundToggle) {
      // document.querySelector(".soundOn").classList.replace("soundOn", "soundOff");
      document.querySelector(".MMyCamSubSoundDesc").style.display = "block";
    } else {
      document.querySelector(".MMyCamSubSoundDesc").style.display = "none";
      // document.querySelector(".soundOff").classList.replace("soundOff", "soundOn");
    }
    setMySoundToggle((prev) => !prev);
  };

  // 나의 마이크 토글
  const openMyMic = () => {
    micOff();

    if (myMicToggle) {
      document.querySelector(".myMicOn").classList.replace("myMicOn", "myMicOff");
    } else {
      document.querySelector(".myMicOff").classList.replace("myMicOff", "myMicOn");
    }
    setMyMicToggle((prev) => !prev);
  };

  // 나의 캠 토글
  const showCam = (e) => {
    camOff();

    if (camToggle) {
      document.querySelector(".camOn").classList.replace("camOn", "camOff");
    } else {
      document.querySelector(".camOff").classList.replace("camOff", "camOn");
    }
    setCamToggle((prev) => !prev);
  };

  return (
    <>
      <div className="MMyCamSubDiv">
        <span className="MMyCamSubText">My Camera</span>
        <div className="MMyCamSubBtnsDiv">
          <div className="MMyCamSubCamSettingBtn" onClick={showSetting}></div>
          <div className="MMyCamSubCamToggleBtn camOn" onClick={showCam}></div>
          <div className="MMyCamSubMicBtn myMicOn" onClick={openMyMic}></div>
          <div className="MMyCamSubSoundBtn" onClick={showMySound}></div>
          <div className="MMyCamSubSoundDesc">
            <div className="MMyCamSubSoundDescTop"></div>
            <div className="MMyCamSubSoundDescMain"></div>
            <span className="MMyCamSubSoundDescSoundVal">{mySoundVal}</span>
            <div className="MMyCamSubSoundDescBar">
              <div className="range-slider">
                <input
                  type="range"
                  className="slider"
                  min="0"
                  max="100"
                  onChange={onChangeMySoundSlider}
                ></input>
                <div className="progressSlider"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
