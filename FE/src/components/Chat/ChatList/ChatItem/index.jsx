// ChatItem 컴포넌트
import "./index.css";

function ChatItem({ chatRoom, showChatPage}) {
  // chatRoom 객체는 상대방 이름, 사진, 마지막 메시지, 안 읽은 메시지의 수 등의 정보를 담고 있습니다.
  // 이 정보를 바탕으로 각 항목을 렌더링합니다.
  // console.log("ChatItem in ChatPage:", chatRoom); 
  const handleItemClick = () => {
    showChatPage(chatRoom);
  };
  return (
    <div className="ChatItem" onClick={() => handleItemClick()}>
      <div className="ChatPicture">
        {/* 여기에는 상대방의 사진을 렌더링합니다. */}
        <img src={chatRoom.picture} />
      </div>
      <div className="ChatContent">
        <div className="ChatName">
        <span className="ChatNameWho">{chatRoom.femaleName}</span> 
        </div>
        <div className="ChatText">
          <span className="ChatTextWhat">{chatRoom.lastMessage}</span>
        </div>
      </div>
      <div className="ChatAlarm">{chatRoom.unreadCount}</div>
    </div>
  );
}

export default ChatItem;
