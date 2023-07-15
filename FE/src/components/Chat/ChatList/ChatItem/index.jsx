// ChatItem 컴포넌트
import "./index.css";
import womanImage from "../../../../assets/images/woman.jpg";


function ChatItem({ chatRoom, showChatPage}) {

  const handleItemClick = () => {
    showChatPage(chatRoom);
  };

  return (
  <div className="ChatItem" onClick={() => handleItemClick()}>
  <div className="ChatPicture" style={{ backgroundImage: `url(${chatRoom.OpponentImage ? chatRoom.OpponentImage : womanImage})` }}>
 </div>
      <div className="ChatContent">
        <div className="ChatName">
        <span className="ChatNameWho">{chatRoom.opponentName}</span> 
        </div>
        <div className="ChatText">
          <span className="ChatTextWhat">{chatRoom.lastestMessage}</span>
        </div>
      </div>
      {/* <div className="ChatAlarm">{chatRoom.unreadCount}</div> */}
    </div>
  );
}

export default ChatItem;
