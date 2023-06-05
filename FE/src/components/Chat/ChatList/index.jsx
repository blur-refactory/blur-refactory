// ChatList 컴포넌트
import React, { useEffect, useState } from "react";
import "./index.css";
import ChatItem from "./ChatItem";

function ChatList({ showChatPage }) {
  const [chatRooms, setChatRooms] = useState([]);

  useEffect(() => {
    // TODO: 서버에서 채팅방 정보를 가져오는 로직을 작성하세요.
    // 예를 들면, fetch나 axios 등의 HTTP 클라이언트를 사용할 수 있습니다.
    // 아래 코드는 임시로 채팅방 정보를 가정한 것입니다.
    const fetchedChatRooms = [
      {
        id: "chat_room_1",
        name: "상대방 이름",
        picture: "상대방 사진 URL",
        lastMessage: "웹소켓에서 마지막으로 보낸 메시지",
        unreadCount: 5,
      },
    ];

    setChatRooms(fetchedChatRooms);
  }, []);

  return (
    <div className="ChatBackground">
      <div className="ChatHeader">
        <div className="ChatHeaderEmoticon"></div>
        <div className="ChatHeaderText">
          <h3>Chatting</h3>
        </div>
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
