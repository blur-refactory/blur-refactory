/* eslint-disable jsx-a11y/alt-text */
/* eslint-disable react-hooks/exhaustive-deps */

import "../../App.css";
import "./index.css";

import axios from "axios";
import React, { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

import MyInfoModal from "../../components/MyInfo/Modal";
import Hash from "../../components/MyInfo/Hash";
import ModalWrap from "../../components/Start/ModalWrap";
import Alert from "../../components/Common/Alert";

function MyInfo() {
  //profile edit modal
  const [miModal, setMyInfoModal] = useState(false);
  const showMyinfoModal = () => {
    setMyInfoModal((pre) => !pre);
  };
  //hash modal
  const [hashModal, setHashModal] = useState(false);
  const showHashModal = () => {
    setHashModal((pre) => !pre);
  };
  //alert modal
  const [alertModal, setalertModal] = useState(false);
  const showAlertModal = () => {
    setalertModal((pre) => !pre);
  };
  // 페이지 이동
  const navigate = useNavigate();
  // reducer에서 변경된 값을 가져오자
  const user = useSelector((state) => {
    return state.user.value;
  });

  const intro = useSelector((state) => {
    return state.user.intro;
  });

  const age = useSelector((state) => {
    return state.user.age;
  });

  const nickName = useSelector((state) => {
    return state.user.nickname;
  });

  const hashCheck = useSelector((state) => {
    return state.hashCheck.checkIntData;
  });



  // 화면 켜지자 말자 띄우는 거
  const API_URL = `${process.env.REACT_APP_API_ROOT_DONGHO}/api/profile`;
  const [proFile, setProFile] = useState([]);
  const [userInterests, setUserInterests] = useState([]);
  useEffect(() => {
    axios({
      headers: {
        "Content-Type": "application/json",
      },
   
      method: "GET",
      url: `${API_URL}`,
      data: {},
    })
      .then((res) => {
        setProFile(res.data);
        setUserInterests(res.data.userInterests);
  
      })
      .catch((err) => {
        console.log(err);
      });
  }, [nickName, intro, age, hashCheck]);

  return (
    <div className="myinfo">
      {miModal || hashModal ? (
        <ModalWrap
          miModal={miModal}
          hashModal={hashModal}
          showHashModal={showHashModal}
          showMyinfoModal={showMyinfoModal}
        />
      ) : null}
      {miModal && !hashModal ? (
        <MyInfoModal
          showHashModal={showHashModal}
          showMyinfoModal={showMyinfoModal}
          showAlertModal={showAlertModal}
          setUserInterests={setUserInterests}
        />
      ) : null}

      {hashModal && !miModal ? (
        <Hash
          showMyinfoModal={showMyinfoModal}
          showHashModal={showHashModal}
          showAlertModal={showAlertModal}
        />
      ) : null}

      {alertModal && !miModal && !hashModal ? (
        <Alert
          showAlertModal={showAlertModal}
          content={"변경사항이 저장되었습니다."}
        />
      ) : null}

      <div className="DarkBlurDiv"></div>
      <div
        onClick={() => {
          navigate("/home");
        }}
        className="MIbackbtn">
        out
      </div>

      <div className="MIImgDiv">
        {proFile.image ? (
          <img className="MIImg" src={proFile.image} />
        ) : (
          <img className="MIImgBack" />
        )}
      </div>
      <span className="MIHashTag">Hash Tag</span>
      

      {userInterests === null || userInterests.length === 0 || userInterests.includes(null) ? (
        <div className="MIHashSet" onClick={showHashModal} disabled={alertModal === true ? true : false}>
          <div className="MIHashSetIcon">
            <span className="MIHashSetText">설정하기</span>
          </div>
        </div>
      ) : (
        <div className="showint" onClick={showHashModal} disabled={alertModal === true ? true : false}>
          {userInterests.map((item, idx) => {
            return (
              <div className="showintdiv" key={idx}>
                {item}
              </div>
            );
          })}
        </div>
      )}


      <div className="MINameAgeDiv">
        <span className="MIAge"> {age === "" ? proFile.age : age}</span>
        <span className="MIName">{nickName === "" ? proFile.nickname : nickName}</span>
      </div>
      <div className="MIIntroducingDiv">
        <span className="MIIntroducingTitle">Introducing</span>
        <span className="MIIntroducingText">
          {intro === "" ? proFile.introduce : intro}
        </span>
      </div>
      <span className="MIProfileLogo">Blur:-)</span>
      <div
        className="MIEdit"
        onClick={() => {
          showMyinfoModal();
        }}
        disabled={alertModal === true ? true : false}>
        profile edit
      </div>
    </div>
  );
}


export default MyInfo;
