import React, { useState, useEffect } from "react";
import "./setmodal.css";
import { useDispatch, useSelector } from "react-redux";
import axios from "axios";
import { setDistancee, setAgeRange } from "../../../../redux/reducers/setDatee";

function SetModal() {
  const API_URL = `${process.env.REACT_APP_API_ROOT_DONGHO}/blur-profile/profile`;
  const id = useSelector((state) => state.strr.id);
  const token = useSelector((state) => state.strr.token);
  const dispatch = useDispatch();
  const [proFile, setProFile] = useState([]);
  const gender = proFile.gender === "F" ? "Male" : "Female";
  const [distance, setDistance] = useState(0);
  const [leftSliderValue, setLeftSliderValue] = useState(20);
  const [rightSliderValue, setRightSliderValue] = useState(50);
  const handleSave = () => {
    dispatch(setDistancee(distance));
    dispatch(setAgeRange([leftSliderValue, rightSliderValue]));
  };
  const changeDistance = (event) => {
    setDistance(event.target.value);
  };
  const handleLeftSliderChange = (event) => {
    const newLeftSliderValue = Number(event.target.value);
    if (newLeftSliderValue <= rightSliderValue) {
      setLeftSliderValue(newLeftSliderValue);
    }
  };
  const handleRightSliderChange = (event) => {
    const newRightSliderValue = Number(event.target.value);
    if (newRightSliderValue >= leftSliderValue) {
      setRightSliderValue(newRightSliderValue);
    }
  };

  useEffect(() => {
    axios({
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      url: `${API_URL}/${id}/getProfile`,
      data: {},
    })
      .then((res) => {
        setProFile(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [id, token]);

  return (
    <div className="SettingModal">
      <div className="setsavebtn" onClick={handleSave}>
        Save
      </div>
      <div className="SetModal">
        <span className="SEtLabel">Setting</span>
        <div className="SEtMidModalChangediv">
          <div className="ModalInputBox">
            <span className="SetMidPartnerLable">Partner Gender</span>
            <div className="SetMMPartnerCheckdiv">
              <div className="gender">{gender}</div>
              <div className="blurdiv"></div>
            </div>
          </div>

          <div className="ModalInputBox">
            <span className="SetMidPartnerLable">Distance from partner</span>
            {distance} km
            <div className="SetMMPartnerCheckdiv">
              <div className="blurdiv" />
              <div className="range-slider">
                <input
                  type="range"
                  className="range-slider progressSlider"
                  min="0"
                  max="50"
                  value={distance}
                  onChange={changeDistance}></input>
              </div>
            </div>
          </div>

          <div className="ModalInputBox">
            <span className="SetMidPartnerLable">Partner's age group</span>
            {leftSliderValue}살 ~ {rightSliderValue}살
            <div className="SetMMPartnerCheckdiv">
              <div className="blurdiv"></div>
              <div className="range-slider">
                <input
                  className="range-slider1 range-slider1-left"
                  type="range"
                  min="20"
                  max="34"
                  value={leftSliderValue}
                  onChange={handleLeftSliderChange}
                />
                <input
                  className="range-slider1 range-slider1-right"
                  type="range"
                  min="36"
                  max="50"
                  value={rightSliderValue}
                  onChange={handleRightSliderChange}
                />
                <div
                  className="range-bar"
                  style={{
                    left: `${((leftSliderValue - 20) / 30) * 100}%`,
                    width: `${
                      ((rightSliderValue - leftSliderValue) / 30) * 100
                    }%`,
                  }}
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default SetModal;
