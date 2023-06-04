import "./index.css";
import ChatPageDialogueMe from "./ChatDialogueMe";
import ChatPageDialogueYou from "./ChatDialogueYou";

function ChatPage({ showChatPage }) {
  return (
    <div className="ChatPageBack">
      <div className="ChatPageHeader">
        <div className="ChatPageHeaderBtn" onClick={showChatPage}></div>
        <div className="ChatPageHeaderName">한효주</div>
      </div>
      <div className="ChatPageContent">
        <div className="ChatPageDialogue">
          <ChatPageDialogueYou content={"그래그래"} />
          <ChatPageDialogueMe content={"아니아니"} />
        </div>
        <div className="ChatPageInputDiv">
          <div className="ChatPageInput">
            <input className="ChatPageInputMessage"></input>
            <button className="ChatPageInputButton"></button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ChatPage;
