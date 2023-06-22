// import React, { useEffect, useState } from "react";
// import "./index.css";
// import ChatPageDialogueMe from "./ChatDialogueMe";
// import ChatPageDialogueYou from "./ChatDialogueYou";
// import axios from 'axios';

// export function ChatPage({ showChatPage, chatRoom }) {
//   const [messages, setMessages] = useState([]);
//   const [messageInput, setMessageInput] = useState("");
//   // console.log(chatRoom)

//   useEffect(() => {
//     // const username = chatRoom.maleId;
//     const username = 'test@test.com';

//     // HTTP 요청을 통해 헤더 값을 설정
//     axios.defaults.headers.common['X-Username'] = username;

//     // WebSocket 연결 시도
//     var socket = new WebSocket(`ws://localhost:8081/ws?roomId=${chatRoom.id}`);
    
//     socket.addEventListener("message", (event) => {
//       const message = JSON.parse(event.data);

//       if (Array.isArray(message)) {
//         // 배열 형태의 메시지인 경우, 여러 메시지를 받아온 경우입니다.
//         // 적절한 처리를 수행합니다.
//         for (let i = 0; i < message.length; i++) {
//           setMessages((prevMessages) => [...prevMessages, message[i]]);
//         }
//       } else if (message.message !== undefined) {
//         // 단일 메시지인 경우, 새로운 메시지를 받았을 때입니다.
//         // 적절한 처리를 수행합니다.
//         setMessages((prevMessages) => [message, ...prevMessages]);
//       }
//     });

//     return () => {
//       socket.close();
//     };
//   }, []);

//   // 채팅 메시지를 전송하는 함수를 구현합니다.
//   const sendMessage = () => {
//     // 서버로 메시지를 전송하는 코드를 작성합니다.
//     // ...

//     // 전송한 메시지를 화면에 표시합니다.
//     if (messageInput.trim() !== "") {
//       setMessages((prevMessages) => [
//         { sender: "me", text: messageInput },
//         ...prevMessages,
//       ]);
//       setMessageInput("");
//     }
//   };

//   return (
//     <div className="ChatPageBack">
//       <div className="ChatPageHeader">
//         <div className="ChatPageHeaderBtn" onClick={showChatPage}></div>
//         <div className="ChatPageHeaderName">{chatRoom.femaleName}</div>
//       </div>
//       <div className="ChatPageContent">
//         <div className="ChatPageDialogue">
//           {messages.map((message, index) =>
//             message.sender === "me" ? (
//               <ChatPageDialogueMe key={index} content={message.text} />
//             ) : (
//               <ChatPageDialogueYou key={index} content={message.text} />
//             )
//           )}
//         </div>
//         <div className="ChatPageInputDiv">
//           <div className="ChatPageInput">
//           <input
//                 className="ChatPageInputMessage"
//                 value={messageInput}
//                 onChange={(e) => setMessageInput(e.target.value)}
//               />
//               <button
//                 className="ChatPageInputButton"
//                 onClick={sendMessage}
//               >
//                 Send message
//               </button>
//             </div>
//           </div>
//         </div>
//       </div>
//   );
// }

// export default ChatPage;

import React, { useEffect, useState } from "react";
import "./index.css";
import ChatPageDialogueMe from "./ChatDialogueMe";
import ChatPageDialogueYou from "./ChatDialogueYou";
import axios from 'axios';

export function ChatPage({ showChatPage, chatRoom }) {
  const [messages, setMessages] = useState([]);
  const [messageInput, setMessageInput] = useState("");


  useEffect(() => {
    const roomId = 'testtest2'; // 방 ID

    const queryParams = {
      roomId: roomId,
    };
  
    // HTTP 요청 보내기
    axios.get('ws://localhost:8081/ws?roomId=testMaletestFemale&userId=man', { params: queryParams })
      .then(response => {
        // HTTP 요청 성공 시 WebSocket 연결 시도
        const url = 'ws://blurblur.kr/api/ws';
        const webSocket = new WebSocket(url);
        
        webSocket.onopen = () => {
          // WebSocket 연결이 열리면 실행되는 코드
          console.log('WebSocket 연결이 열렸습니다.');
        };
  
        // 이벤트 핸들러 등록 등 WebSocket 사용에 필요한 로직 작성
  
        // 컴포넌트 언마운트 시 WebSocket 연결 종료
        return () => {
          webSocket.close();
        };
      })
      .catch(error => {
        // HTTP 요청 실패 시 처리
        console.error('HTTP 요청 실패:', error);
      });
  }, []);

  const sendMessage = () => {
    // 서버로 메시지를 전송하는 코드를 작성합니다.
    // ...

    // 전송한 메시지를 화면에 표시합니다.
    if (messageInput.trim() !== "") {
      setMessages((prevMessages) => [
        { sender: "me", text: messageInput },
        ...prevMessages,
      ]);
      setMessageInput("");
    }
  };

  return (
    <div className="ChatPageBack">
      <div className="ChatPageHeader">
        <div className="ChatPageHeaderBtn" onClick={showChatPage}></div>
        <div className="ChatPageHeaderName">{chatRoom.femaleName}</div>
      </div>
      <div className="ChatPageContent">
        <div className="ChatPageDialogue">
          {messages.map((message, index) =>
            message.sender === "me" ? (
              <ChatPageDialogueMe key={index} content={message.text} />
            ) : (
              <ChatPageDialogueYou key={index} content={message.text} />
            )
          )}
        </div>
        <div className="ChatPageInputDiv">
          <div className="ChatPageInput">
            <input
              className="ChatPageInputMessage"
              value={messageInput}
              onChange={(e) => setMessageInput(e.target.value)}
            />
            <button
              className="ChatPageInputButton"
              onClick={sendMessage}
            >
              Send message
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ChatPage;
