import React, { useEffect, useState } from "react";
import "./index.css";
import ChatPageDialogueMe from "./ChatDialogueMe";
import ChatPageDialogueYou from "./ChatDialogueYou";

function ChatPage({ showChatPage }) {
  const [message, setMessage] = useState(""); // 현재 입력된 메시지를 상태로 관리
  const [messages, setMessages] = useState([]); // 이전 메시지를 상태로 관리

  useEffect(() => {
    const chatsocket = new WebSocket("wss://www.shinemustget.com/api/ws");

    // 웹소켓으로부터 새 메시지를 수신할 때마다 이전 메시지에 새 메시지를 추가
    chatsocket.addEventListener("message", (event) => {
      const data = JSON.parse(event.data);
      setMessages((prevMessages) => [...prevMessages, data]);
    });

    // 컴포넌트가 언마운트될 때 웹소켓 연결을 닫음
    return () => {
      chatsocket.close();
    };
  }, []);

  // 메시지 입력 필드의 값이 변경될 때 호출되어 상태를 업데이트
  const handleMessageChange = (e) => {
    setMessage(e.target.value);
  };

  // "send message" 버튼 클릭 시 현재 입력된 메시지를 서버로 전송하고 입력 필드를 비움
  const handleSendClick = () => {
    const chatsocket = new WebSocket("wss://www.blurblur.kr/user-service/ws");
    chatsocket.addEventListener("open", () => {
      chatsocket.send(JSON.stringify({ text: message }));
      chatsocket.close();
    });
    setMessage("");
  };

  return (
    <div className="ChatPageBack">
      <div className="ChatPageHeader">
        <div className="ChatPageHeaderBtn" onClick={showChatPage}></div>
        <div className="ChatPageHeaderName">DB에 저장되어있는 상대방 이름</div>
      </div>
      <div className="ChatPageContent">
        <div className="ChatPageDialogue">
          {/* 이전에 받은 모든 메시지를 순회하면서 화면에 표시 */}
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
              value={message}
              onChange={handleMessageChange}
            />
            <button
              className="ChatPageInputButton"
              onClick={handleSendClick}></button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ChatPage;
