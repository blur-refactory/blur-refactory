function ChatPageDialogueMe({ content }) {
  return (
    <div className="ChatPageDialogueMe">
      <div className="ChatPageDialogueContent">{content}</div>
      <div className="ChatPageProfilePicture">
        db에 저장되어 있는 내사진을 띄워야 한다.
      </div>
    </div>
  );
}

export default ChatPageDialogueMe;
