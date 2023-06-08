import React, { useEffect, useState } from "react";
import "./index.css";
import ChatPageDialogueMe from "./ChatDialogueMe";
import ChatPageDialogueYou from "./ChatDialogueYou";
import chatsocket from "../chatsocket";

function ChatPage({ showChatPage }) {
  // useState를 사용하여 현재 입력된 메시지와 이전 메시지를 저장하는 상태를 만듭니다.
  const [message, setMessage] = useState("");
  const [messages, setMessages] = useState([]);

  // useEffect를 사용하여 컴포넌트가 마운트될 때 웹소켓 리스너를 설정합니다.
  useEffect(() => {
    // 'new message' 이벤트를 수신할 때마다, 이전 메시지에 새 메시지를 추가합니다.
    chatsocket.on("new message", (data) => {
      setMessages((prevMessages) => [...prevMessages, data]);
    });

    // 컴포넌트가 언마운트될 때 이 리스너를 제거합니다.
    return () => {
      chatsocket.off("new message");
    };
  }, []);

  // 메시지 입력 필드의 값이 변경될 때마다 이 함수가 호출되어 상태를 업데이트합니다.
  const handleMessageChange = (e) => {
    setMessage(e.target.value);
  };

  // "send message" 버튼이 클릭되었을 때 이 함수가 호출됩니다.
  const handleSendClick = () => {
    // 현재 입력된 메시지를 서버로 전송합니다.
    chatsocket.emit("new message", { text: message });
    // 메시지를 전송한 후에는 입력 필드를 비웁니다.
    setMessage("");
  };

  return (
    // 이 아래는 React 컴포넌트의 렌더링 부분입니다. HTML과 유사한 JSX 문법을 사용합니다.
    <div className="ChatPageBack">
      <div className="ChatPageHeader">
        <div className="ChatPageHeaderBtn" onClick={showChatPage}></div>
        <div className="ChatPageHeaderName">DB에 저장되어있는 상대방 이름</div>
      </div>
      <div className="ChatPageContent">
        <div className="ChatPageDialogue">
          {/* 이전에 받았던 모든 메시지를 순회하면서 화면에 표시합니다. */}
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
            <button className="ChatPageInputButton" onClick={handleSendClick}>
              Send message
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ChatPage;
