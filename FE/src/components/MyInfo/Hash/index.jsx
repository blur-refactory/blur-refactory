/* eslint-disable no-unused-vars */
/* eslint-disable react-hooks/exhaustive-deps */

import "./index.css";
import React, { useState, useEffect, useRef } from "react";
import axios from "axios";
import UserCheckdata from "./HashComponent/UserCheckdata";
import { setCheckDataa } from "../../../redux/reducers/checkDataSlice";
import { useDispatch, useSelector } from "react-redux";

function Hash({ showHashModal, showAlertModal }) {
  // const id = "123123";

  const [intdata, setIntData] = useState([]); // 관심사 전체 가져오기
  const [userIntData, setuserIntData] = useState([]); // 체크했던 데이터 가져오기
  const [checkData, setcheckData] = useState([]); //체크한 데이터 띄우기
  const [limit] = useState(5);
  // 검색기능
  const [searchBar, setSearchBar] = useState(false);
  const [searchQuery, setSearchQuery] = useState("");
  const [searchResult, setSearchResult] = useState(null);

  //데이터 가져오기
  const token = useSelector((state) => {
    return state.strr.token;
  });
  const id = useSelector((state) => {
    return state.strr.id;
  });

  const dispatch = useDispatch();

  const API_URL = `${process.env.REACT_APP_API_ROOT_DONGHO}/api/profile`;
  useEffect(() => {
    axios({
      method: "GET",
      url: `${API_URL}/getInterest`,
      data: {},
    })
      .then((res) => {
        setIntData(res.data.interests);
        const userInterests = res.data.userInterests.map((interest) => {
          return interest.interestName;
        });
        setuserIntData(userInterests);
      })

      .catch((err) => {
        alert("카테고리 없다.");
        console.log(err);
      });
  }, []);

  // 관심사 업데이트
  const intSave = () => {
    const updatedHash = {
      interests: checkData.length > 0 ? checkData : userIntData,
    };

    setcheckData(updatedHash);

    axios({
      method: "put",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      url: `${API_URL}/updateInterest`,
      data: updatedHash,
    })
      .then((res) => {
        // console.log(res.data);
        console.log("성공><");
        dispatch(setCheckDataa(checkData));
        console.log(updatedHash);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const filteredData = intdata.filter((item) =>
    item.interestName.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const handleSearch = (e) => {
    setSearchQuery(e.target.value);
  };

  const handleRemove = (item) => {
    setcheckData((prev) => prev.filter((intdata) => intdata !== item));
  };

  const handleClick = (interestName) => {
    if (checkData.length >= limit) {
      alert(
        `최대 ${limit}개만 선택 가능합니다. 취소하려면 위에 선택된 관심사를 클릭해 제거해주세요`
      );
      return;
    }
    const found = checkData.find((data) => data === interestName);
    if (found) {
      handleRemove(interestName);
    } else {
      setcheckData((prev) => [...prev, interestName]);
    }
  };

  const HashSearch = ({ results, handleClick }) => {
    const handleClickOutside = (e) => {
      if (ref.current && !ref.current.contains(e.target)) {
        handleClick();
      }
    };

    const ref = useRef();

    useEffect(() => {
      document.addEventListener("click", handleClickOutside);
      return () => {
        document.removeEventListener("click", handleClickOutside);
      };
    });

    return <div ref={ref}></div>;
  };

  const simularSearch = () => {
    axios
      .post(`${API_URL}/getInterestRank`, {
        interestName: searchQuery,
      })
      .then((response) => {
        console.dir(response);
        console.log(searchQuery);

        const data = response.data; // 응답 데이터 추출

        setSearchResult(data);
        setSearchBar(true);
      })
      .catch((error) => {
        console.error(error);
        console.error(error.status);
      });
  };

  return (
    <div className="Hash">
      {searchBar && (
        <HashSearch
          data={searchResult}
          handleClick={() => setSearchBar(false)}
        />
      )}

      <div>
        <div className="hashSerchDiv">
          <div className="inputdodbogi" />
          <input
            className="hashinput"
            type="text"
            placeholder="Search interests"
            value={searchQuery}
            onChange={handleSearch}
            onKeyPress={(event) => {
              if (event.key === "Enter") {
                simularSearch();
              }
            }}
            style={{ outline: "none" }}
          />
          {searchQuery !== "" && (
            <HashSearch handleClick={() => setSearchQuery("")} />
          )}
          <div className="hashVec" />
        </div>
        <div className="hashintdiv">
          <div className="searchintbox">
            <div className="searchdiv">
              {searchQuery.length > 0 &&
                filteredData.map((item, idx) => {
                  const selected = checkData.includes(item.interestName);
                  return (
                    <div
                      className="searchbox"
                      key={item.interestName}
                      data-idx={idx}
                    >
                      <button
                        className={`btn ${
                          selected ? "changesearchback" : "searchback"
                        }`}
                      >
                        <input
                          className="custom-checkbox-style"
                          type="checkbox"
                          key={item.interestName}
                          data-idx={idx}
                          value={item.interestName}
                          checked={selected}
                          onChange={(e) => {
                            handleClick(item.interestName);
                          }}
                        />
                        {item.interestName}
                      </button>
                    </div>
                  );
                })}
            </div>
          </div>
          <div>
            <div className="hashaddiv">
              {checkData.length > 0 && (
                <div className="hashaddiv">
                  {checkData.map((item, idx) => (
                    <UserCheckdata
                      item={item}
                      onRemove={() => handleRemove(item)}
                    />
                  ))}
                </div>
              )}
            </div>
          </div>
          <div className="cainterestdiv">
            {intdata.map((item, idx) => {
              const selected = checkData.includes(item.interestName);
              return (
                <div
                  className="cainterestbox"
                  key={item.interestName}
                  data-idx={idx}
                >
                  <button
                    className={`btn ${
                      selected ? "changecaintBack" : "caintBack"
                    }`}
                  >
                    <input
                      className="custom-checkbox-style"
                      type="checkbox"
                      key={item.interestName}
                      data-idx={idx}
                      value={item.interestName}
                      checked={selected}
                      onChange={(e) => {
                        handleClick(item.interestName);
                      }}
                    />
                    {item.interestName}
                  </button>
                </div>
              );
            })}
          </div>
        </div>
      </div>

      <button
        className="hashEdit"
        onClick={() => {
          showHashModal();
          showAlertModal();
          intSave();
        }}
      >
        선호 정보 수정
      </button>
    </div>
  );
}

export default Hash;
