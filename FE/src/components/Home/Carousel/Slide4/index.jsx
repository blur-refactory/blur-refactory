import slideImage4 from "../../../../assets/images/slideImage5.svg";

function Slide4() {
  return (
    <div className="HomeRightDiv">
      <div className="HomeRightImgDiv">
        <img className="HomeRightImg" src={slideImage4} alt="no"></img>
      </div>
      <div className="HomeRightToolTipDiv">
        <span className="HomeRightToolTipText">
          이모티콘을 눌러서 상대방에게 보내주세요.
        </span>
      </div>
    </div>
  );
}

export default Slide4;
