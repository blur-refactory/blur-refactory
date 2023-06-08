// import { useEffect } from "react";
import "./index.css";

let ProgressBarTmp = 0;
function ProgressBar() {
  let currentVal = false;
  // const user = useSelector((state) => state.mt.@@@@@@); // Redux에 저장되어있는 @@@@@

  const start = () => {
    const processBar = document.querySelector(".ProgressBar");
    const currentProcessBar = document.createElement("div");
    currentProcessBar.className = "currentProcessBar";
    const msgCurrentTime = document.createElement("span");
    msgCurrentTime.className = "msgCurrentTime";
    processBar.appendChild(currentProcessBar);
    currentProcessBar.appendChild(msgCurrentTime);

    if (!currentVal) {
      currentVal = true;

      let width = 3;
      let timer = setInterval(frame, 1000);

      function frame() {
        // 10분(600초)이 다채워졌을 경우, 채팅창에 리스트 추가(시간은 계속감)
        let minute = parseInt(width / 60);
        let second = width % 60;
        if (width > 600) {
          currentProcessBar.innerHTML = `${minute}분 0초`;
          clearInterval(timer);

          // [상대유저 아이디 매핑 알림] && [백에게 채팅을 추가하기 위한 axios 통신]
          alert("김블리님이 채팅목록에 추가 되었습니다:)");
          // axios({
          //   method: "post",
          //   url: `${API_URL}/start`, // 바뀌어야 할 부분!!!!!!!!!!!!!!!!!!!!!!
          //   // 내 아이디, 파트너 아이디를 보냄
          //   data: {
          //     myId: "id1",
          //     partnerId: "id2",
          //   },
          // })
          //   .then((res) => console.log(res.data))
          //   .catch((err) => console.log(err));
        } else {
          if (width > 60) currentProcessBar.innerHTML = `${minute}분 ${second}초`;
          // if(width === )
          width++;
          currentProcessBar.style.width = (width / 600) * 100 + "%";
        }
      }
    }
  };

  if (ProgressBarTmp === 1) {
    ProgressBarTmp = 0;
    setTimeout(() => {
      start();
    }, 3000);
  }

  return <div className="ProgressBar">{/* <Timer /> */}</div>;
}

export default ProgressBar;
