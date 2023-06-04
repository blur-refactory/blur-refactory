import slideImage3 from "../../../assets/images/slideImage3.jpg";

function Slide3() {
  return (
    <div className="HomeRightDiv">
      <div className="HomeRightImgDiv">
        <img className="HomeRightImg" src={slideImage3} alt="no"></img>
      </div>
      <div className="HomeRightToolTipDiv">
        <span className="HomeRightToolTipText">
          이모티콘을 눌러서 상대방에게 보내주세요.
        </span>
      </div>
    </div>
  );
}

export default Slide3;
