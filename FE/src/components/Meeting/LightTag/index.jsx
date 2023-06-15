import { useState } from "react";
import { useCallback } from "react";

export default function LightTag() {
  const [lightToggle, setLightToggle] = useState(false);

  // (파트너 캠 상단의) 관심사 표현 토글
  const showLight = useCallback(() => {
    setLightToggle((prev) => !prev);

    if (!lightToggle) {
      // basic 클래스 있을 경우(두번째 false 부터)
      if (document.querySelector(".basicLight")) {
        document.querySelector(".basicLight").classList.replace("basicLight", "clickLight");
        document
          .querySelector(".basicLightChangeDiv")
          .classList.replace("basicLightChangeDiv", "clickLightChangeDiv");
      }
      // basic 클래스 없을 경우(첫번째 false)
      else {
        document.querySelector(".lightTagBtn").classList.add("clickLight");
        document.querySelector(".lightTagsDiv").classList.add("clickLightChangeDiv");
      }

      // document.querySelector(".lightTagsDiv").style.display = "block";

      // click 클래스인 경우(true)
    } else {
      document.querySelector(".clickLight").classList.replace("clickLight", "basicLight");
      document
        .querySelector(".clickLightChangeDiv")
        .classList.replace("clickLightChangeDiv", "basicLightChangeDiv");
      // document.querySelector(".lightTagsDiv").style.display = "none";
    }
  }, [lightToggle]);

  return (
    <>
      <div className="lightTagsDiv"></div>
      <div className="lightTagBtn" onClick={showLight}></div>
    </>
  );
}
