import React from "react";

export default function MyCamSubDiv({
  showSetting,
  showCam,
  openMyMic,
  showMySound,
  mySoundVal,
  onChangeMySoundSlider,
}) {
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
