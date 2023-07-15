import React, { useEffect, useState } from "react";
import "./index.css";
import ChatItem from "./ChatItem";
import ChatPage from "./ChatPage";
import axios from 'axios';

function ChatList({ showChatPage }) {
  const [chatRooms, setChatRooms] = useState([]);
  const [selectedChatRoom, setSelectedChatRoom] = useState(null);
  const [isChatPageVisible, setIsChatPageVisible] = useState(false);
  const [selectedRoomId, setSelectedRoomId] = useState("");

  console.log(chatRooms)
  
  useEffect(() => {
    axios({
      // headers: {
      //   "X-Username" : "test@test.com",
      // },
      method: "GET",
      url: 'http:/blurblur.kr/api/chat/getChatrooms',
      // url: 'http://localhost:8081/chat/getChatrooms',
      data: {},
    })
    .then((response) => {
      // 정렬된 채팅방 목록
      const sortedChatRooms = response.data.sort((a, b) => {
        // 날짜와 시간을 파싱하여 비교
        const aTimestamp = new Date(a.lastestMessageTime);
        const bTimestamp = new Date(b.lastestMessageTime);
        return bTimestamp - aTimestamp;
      });

      setChatRooms(sortedChatRooms);

      if (sortedChatRooms.length > 0) {
        setSelectedChatRoom(sortedChatRooms[0]);
        setSelectedRoomId(sortedChatRooms[0].id);
      }
    })
    .catch((error) => {
      console.error(error);
    });
  }, []);

  const handleChatItemClick = (chatRoom) => {
    setSelectedChatRoom(chatRoom);
    setIsChatPageVisible(true);
    setSelectedRoomId(chatRoom.id);
  };

  const handleBackClick = () => {
    setIsChatPageVisible(false);
  };

  return (
    <div className="ChatBackground">
      <div className="ChatHeader">
        <div className="ChatHeaderEmoticon"/>
        <div className="ChatHeaderText">Chatting</div>
      </div>
      {isChatPageVisible ? (
        <ChatPage
          chatRoom={selectedChatRoom}
          showChatPage={handleBackClick}
          selectedRoomId={selectedRoomId}
        />
      ) : (
        <div className="ChatList">
          {chatRooms.map((chatRoom) => (
            <ChatItem
              key={chatRoom.id}
              chatRoom={chatRoom}
              showChatPage={handleChatItemClick}
              selectedRoomId={selectedRoomId}
            />
          ))}
        </div>
      )}
    </div>
  );
}

export default ChatList;
