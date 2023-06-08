// ChatList 컴포넌트
import React, { useEffect, useState } from "react";
import "./index.css";
import ChatItem from "./ChatItem";
import axios from 'axios';

function ChatList({ showChatPage }) {
  const [chatRooms, setChatRooms] = useState([]);

  useEffect(() => {
    axios({
      method: "GET",
      url: 'http://localhost:8081/chat/getChatrooms',
      headers: {
        'X-Username': 'test@test.com', // 로컬 테스트 시
        // 'Authorization': 'Bearer accessToken', // 배포서버
        // 환경에 따라 적절한 헤더를 사용하세요.
      },
      data: {},
    })
    .then((response) => {
      setChatRooms(response.data);
    })
    .catch((error) => {
      console.error(error);
    });
  }, []);
  

  return (
    <div className="ChatBackground">
      <div className="ChatHeader">
        <div className="ChatHeaderEmoticon"></div>
        <div className="ChatHeaderText">Chatting</div>
      </div>
      <div className="ChatList">
        {chatRooms.map((chatRoom) => (
          <ChatItem
            key={chatRoom.id}
            chatRoom={chatRoom}
            showChatPage={showChatPage}
          />
        ))}
      </div>
    </div>
  );
}

export default ChatList;
