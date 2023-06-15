import { useEffect } from "react";
import { useState } from "react";
import { useSelector } from "react-redux";

export default function Imoticon() {
  // (나의 캠 상단의) 이모지 표현 토글
  const [smileToggle, setSmileToggle] = useState(false);

  const partnerInterests = useSelector((state) => state.mt.partnerInterests);

  const showSmile = () => {
    setSmileToggle((prev) => !prev);

    if (!smileToggle) {
      if (document.querySelector(".basicSmileChangeDiv"))
        document
          .querySelector(".basicSmileChangeDiv")
          .classList.replace("basicSmileChangeDiv", "clickSmileChangeDiv");
      else document.querySelector(".ImotionDiv").classList.add("clickSmileChangeDiv");
    } else
      document
        .querySelector(".clickSmileChangeDiv")
        .classList.replace("clickSmileChangeDiv", "basicSmileChangeDiv");
  };

  useEffect(() => {
    const lightTagsDiv = document.querySelector(".lightTagsDiv");
    for (let i = 1; i <= partnerInterests.length; i++) {
      const lightTag = document.createElement("span");
      lightTag.id = `lightTag${i}`;
      lightTag.innerText = partnerInterests[i - 1];
      lightTagsDiv.appendChild(lightTag);
    }
  });

  return (
    <>
      <div className="ImotionDiv">
        <div className="Imotion1"></div>
        <div className="Imotion2"></div>
        <div className="Imotion3"></div>
        <div className="Imotion4"></div>
        <div className="Imotion5"></div>
        <div className="Imotion6"></div>
        <div className="Imotion7"></div>
      </div>
      <div className="ImotionBtn" onClick={showSmile}></div>
    </>
  );
}
