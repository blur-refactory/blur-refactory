import womanImage from "../../../../../assets/images/woman.jpg";


function ChatPageDialogueYou({ content, chatRoom  }) {

  return (
    <div className="ChatPageDialogueYou">
      <div className="ChatPageDialogueContent">{content}</div>
      <div className="ChatPageProfilePicture" style={{ backgroundImage: `url(${chatRoom.OpponentImage ? chatRoom.OpponentImage : womanImage})` }}/>
    </div>
  );
}

export default ChatPageDialogueYou;
