// ChatList 컴포넌트
import React, { useEffect, useState } from "react";
import "./index.css";
import ChatItem from "./ChatItem";
import ChatPage from "./ChatPage";
import axios from 'axios';

function ChatList({ showChatPage }) {
  const [chatRooms, setChatRooms] = useState([]);
  const [selectedChatRoom, setSelectedChatRoom] = useState(null);
  const [isChatPageVisible, setIsChatPageVisible] = useState(false);

  useEffect(() => {
    axios({
      method: "GET",
      url: 'http://blurblur.kr/api/chat/getChatrooms',
      data: {},
    })
    .then((response) => {
      setChatRooms(response.data);
      setSelectedChatRoom(response.data[0]); // 첫 번째 채팅방을 선택
    })
    .catch((error) => {
      console.error(error);
    });
  }, []);

  const handleChatItemClick = (chatRoom) => {
    setSelectedChatRoom(chatRoom);
    setIsChatPageVisible(true);
  };

  const handleBackClick = () => {
    setIsChatPageVisible(false);
  };
  
  return (
    <div className="ChatBackground">
      <div className="ChatHeader">
        <div className="ChatHeaderEmoticon"></div>
        <div className="ChatHeaderText">Chatting</div>
      </div>
      {isChatPageVisible ? (
        <ChatPage
          chatRoom={selectedChatRoom}
          showChatPage={handleBackClick}
        />
      ) : (
        <div className="ChatList">
          {chatRooms.map((chatRoom) => (
            <ChatItem
              key={chatRoom.id}
              chatRoom={chatRoom}
              showChatPage={handleChatItemClick}
            />
          ))}
        </div>
      )}
    </div>
  );
}

export default ChatList;
