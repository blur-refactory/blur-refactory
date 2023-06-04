import "./index.css";

function ChatItem({ showChatPage }) {
  return (
    <div className="ChatItem" onClick={showChatPage}>
      <div className="ChatPicture"></div>
      <div className="ChatContent">
        <div className="ChatName">
          <span className="ChatNameWho">한효주</span>
        </div>
        <div className="ChatText">
          <span className="ChatTextWhat">내일 만나자</span>
        </div>
      </div>
      <div className="ChatAlarm">1</div>
    </div>
  );
}

export default ChatItem;
