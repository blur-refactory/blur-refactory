import React from "react";

function ChatPageDialogueYou({ content, picture }) {
  return (
    <div className="ChatPageDialogueYou">
      <div className="ChatPageDialogueContent">{content}</div>
      <div className="ChatPageProfilePicture">
        <img src={picture} alt="상대방 프로필 사진" />
      </div>
    </div>
  );
}

export default ChatPageDialogueYou;
