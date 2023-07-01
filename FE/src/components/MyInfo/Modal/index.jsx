/* eslint-disable no-unused-vars */
/* eslint-disable react-hooks/exhaustive-deps */

import "../../../App.css";
import "./index.css";
import React, { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { nickname, intro, age } from "../../../redux/reducers/userEdit";
import SetModal from "./SetModal/setmodal";

import axios from "axios";
// import styled from "styled-components";

function MyInfoModal({ showMyinfoModal, showAlertModal }) {
  const API_URL = `${process.env.REACT_APP_API_ROOT_DONGHO}/api/profile`;

  // 컴포넌트 켜지자말자 데이터 받아 오기
  const [proFile, setProFile] = useState([]);
  const token = useSelector((state) => {
    return state.strr.token;
  });

  useEffect(() => {
    axios({
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      url: `${API_URL}/getProfile`,
      data: {},
    })
      .then((res) => {
        setProFile(res.data);
      })
      .catch((err) => {});
  }, []);

  // 유저프로필 업데이트 하기
  const distance = useSelector((state) => state.setDatee.distancee);
  const minAge = useSelector((state) => state.setDatee.ageRange[0]);
  const maxAge = useSelector((state) => state.setDatee.ageRange[1]);

  const handleSave = () => {
    const updatedProfile = {

      age: ageInput === "" ? proFile.age : ageInput,
      nickname: nameInput === "" ? proFile.nickname : nameInput,
      introduce: introInput === "" ? proFile.introduce : introInput,
      mbti: mbti === "" ? proFile.mbti : mbti,
      gender:
        genderCheck === ""
          ? proFile.gender
          : gender[genderCheck === "check" ? 0 : 1],
      minAge: minAge === "" ? proFile.minAge : minAge,
      maxAge: maxAge === "" ? proFile.maxAge : maxAge,
      maxDistance: distance === "" ? proFile.maxDistance : distance,
    };

    setProFile(updatedProfile);

    axios({
      method: "put",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      url: `${API_URL}/updateProfile`,
      data: updatedProfile,
    })
      .then((res) => {
        dispatch(nickname(res.data.nickname));
        dispatch(intro(res.data.introduce));
        dispatch(age(res.data.age));
        setProFile(res.data);
      })
      .catch((err) => {console.log(err)});
  };


  //setmodal
  const [setModal, setSettingmodal] = useState(false);
  const showSettingModal = () => {
    setSettingmodal((pre) => !pre);
  };

  //profile 변경
  const [nameInput, setNameInput] = useState("");
  const [introInput, setIntroInput] = useState("");
  const [ageInput, setAgeInput] = useState("");

  // nicName
  const [nickName, setNickName] = useState("");

  const handleInputChange = (e) => {
    if (e.target.value.length <= 10) {
      setNameInput(e.target.value);
      setProFile({ ...proFile, nickname: e.target.value });
    } else {
      alert("10글자 이상 입력할 수 없습니다.");
    }
  };
  // age
  const [agee, setAge] = useState("");
  const handleAgeChange = (e) => {
    const inputValue = e.target.value;
    if (!isNaN(inputValue) && inputValue.length <= 2) {
      setAgeInput(inputValue);
      setProFile({ ...proFile, age: e.target.value });
    } else {
      alert("숫자 2자리 이하만 입력 가능합니다.");
    }
  };

  //introducing
  // eslint-disable-next-line react-hooks/exhaustive-deps
  const [introducing, setIntroducing] = useState("");
  const introHandleChange = (e) => {
    setIntroInput(e.target.value);
    setProFile({ ...proFile, introduce: e.target.value });
  };

  // mbti
  const [mbti, setMbti] = useState("");

  const [mbtiOptions, setMbtiOptions] = useState([
    { value: "ISTJ", label: "ISTJ - Inspector" },
    { value: "ISFJ", label: "ISFJ - Protector" },
    { value: "INFJ", label: "INFJ - Counselor" },
    { value: "INTJ", label: "INTJ - Architect" },

    { value: "ISTP", label: "ISTP - Craftsman" },
    { value: "ISFP", label: "ISFP - Composer" },
    { value: "INFP", label: "INFP - Healer" },
    { value: "INTP", label: "INTP - Architect" },

    { value: "ESTP", label: "ESTP - Dynamo" },
    { value: "ESFP", label: "ESFP - Performer" },
    { value: "ENFP", label: "ENFP - Champion" },
    { value: "ENTP", label: "ENTP - Visionary" },

    { value: "ESTJ", label: "ESTJ - Supervisor" },
    { value: "ESFJ", label: "ESFJ - Provider" },
    { value: "ENFJ", label: "ENFJ - Teacher" },
    { value: "ENTJ", label: "ENTJ - Commander" },
  ]);

  const handleSelectChange = (event) => {
    setMbti(event.target.value);
  };
  // state 변경 핸들러
  const handleUpload = () => {
    setNickName(() => {
      return nameInput;
    });
    setAge(() => {
      return ageInput;
    });
    setIntroducing(() => {
      return introInput;
    });
  };

  // 엔터 누르는 거 onKeyPress 랑 써야됨
  const handleOnKeyPress = (e) => {
    if (e.key === "Enter") {
      handleUpload(); // Enter 입력이 되면 클릭 이벤트 실행
    }
  };

  // 이미지
  const [selectedImage, setSelectedImage] = useState(null);
  const [previewImage, setPreviewImage] = useState(null);

  function handleImageChange(event) {
    setSelectedImage(event.target.files[0]);
    setPreviewImage(URL.createObjectURL(event.target.files[0]));
    alert("이미지를 업로드한 후 저장 버튼을 클릭해주세요.");
  }

  function handleSubmit(event) {
    event.preventDefault();
    const formData = new FormData();
    formData.append("profileImage", selectedImage);
    axios
      .post(`${API_URL}/updateImage`, formData)
      .then((res) => {
        console.log(res.data);
      })
      .catch((err) => {
        console.log(err.data);
      });
  }

  // 성별
  const gender = ["M", "F"];
  // 초기 상태값으로 저장된 데이터가 'F'이면 'prev', 'M'이면 'check'로 설정
  const [genderCheck, setgenderCheck] = useState(
    proFile.gender === "F" ? "prev" : "check"
  );
  // 첫 렌더링 시, genderCheck 상태값을 초기 데이터에 따라 설정
  useEffect(() => {
    setgenderCheck(proFile.gender === "F" ? "prev" : "check");
  }, [proFile.gender]);

  // 버튼 클릭 이벤트 핸들러
  function handleButtonClick(genderType) {
    setgenderCheck(genderType); // genderCheck 상태값 변경
  }

  // 데이터 주고 받기
  const dispatch = useDispatch();

  // setProFile(updatedProfile);
  return (
    <div className="Modal">
      {setModal ? <SetModal showSettingModal={showSettingModal} /> : null}
      <div className="leftModal">
          <button type="submit" className="imageEditBtn">
            <form onSubmit={handleSubmit}>
                저장
            </form>
          </button>
        <div className="imgbox">
          <label htmlFor="profileImg">
            {previewImage ? (
              <img
                src={previewImage}
                alt="프로필 이미지"
                className="leftModalImg"
              />
            ) : (
              <img
                className="leftModalImg"
                src={
                  proFile.image ||
                  "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png"
                }
                alt="프로필 이미지"
              />
            )}
          </label>
          <input
            type="file"
            id="profileImg"
            onChange={handleImageChange}
            style={{ display: "none" }}
          />
        </div>

        <div className="leftModalNameDiv">
          <span className="leftModalName">
            welcome {proFile.nickname || "Guest"}
          </span>
        </div>
        <div className="leftModalbtnDiv">
          <button
            className="leftModalEditbtn"
            onClick={() => {
              setSettingmodal(false);
            }}>
            <span className="leftModalProText"> Profile Edit</span>
          </button>
        </div>
        <div className="leftModalbtnDiv">
          <button className="leftModalSetbtn" onClick={showSettingModal}>
            <span className="leftModalSetText"> Setting</span>
          </button>
        </div>
      </div>

      <div className="ProfileModal">
        <span className="PMLabel">Profile Edit</span>
        <div className="fieldsGrid">
          <div className="fieldContainer">
            <span className="fieldLabel">NickName </span>
            <input
              type="text"
              className="fieldInput narrow"
              value={proFile.nickname || ""}
              onChange={handleInputChange}
              placeholder="10자까지만 가능합니다."
              onKeyPress={handleOnKeyPress}></input>
          </div>
          <div className="fieldContainer">
            <span className="fieldLabel">Age</span>
            <input
              type="text"
              className="fieldInput narrow"
              value={proFile.age || ""}
              onChange={handleAgeChange}
              placeholder="숫자만 입력 가능합니다."></input>
          </div>
          <div className="fieldContainer">
            <span className="fieldLabel">MBTI</span>
            <select
              value={mbti}
              onChange={handleSelectChange}
              className="fieldInput wide">
              {mbtiOptions.map((mbtiOptions) => (
                <option key={mbtiOptions.value} value={mbtiOptions.value}>
                  {mbtiOptions.label}
                </option>
              ))}
            </select>
          </div>
          <div className="fieldContainer">
            <span className="fieldLabel">E-mail </span>
            <input
              type="text"
              className="fieldInput narrow"
              value={proFile.email}
            />
          </div>
        </div>

        <div className="PMMGender">
          <span className="PMMGenderLable">Gender</span>
          <div className="PMMGenderdiv">
            <button
              className={`btn genderButton ${
                genderCheck === "check" ? "active" : ""
              }`}
              onClick={() => handleButtonClick("check")}>
              {gender[0]}
            </button>
            <button
              className={`btn genderButton ${
                genderCheck === "prev" ? "active" : ""
              }`}
              onClick={() => handleButtonClick("prev")}>
              {gender[1]}
            </button>
          </div>
        </div>
        <div className="PMIntroducing">
          <span className="PMIntroducingLabel">Introducing</span>
          <input
            type="text"
            className="PMIntroducingInput"
            value={proFile.introduce || ""}
            onChange={introHandleChange}
          />
        </div>
      </div>

      <button
        className="ModalOut"
        onClick={() => {
          showMyinfoModal();
          showAlertModal();
          handleSave();
        }}>
        <span className="ModalOutText">confirm</span>
      </button>
    </div>
  );
}
export default MyInfoModal;
// ReactDOM.render(<ExampleSlider />, document.getElementById("root"));
