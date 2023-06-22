import React from "react";

function ChatPageDialogueMe({ content, picture }) {
  return (
    <div className="ChatPageDialogueMe">
      <div className="ChatPageDialogueContent">{content}</div>
      <div className="ChatPageProfilePicture">
        <img src={picture} alt="내 프로필 사진" />
      </div>
    </div>
  );
}

export default ChatPageDialogueMe;
