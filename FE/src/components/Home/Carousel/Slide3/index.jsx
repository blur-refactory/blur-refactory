import slideImage3 from "../../../../assets/images/slideImage0.gif";

function Slide3() {
  return (
    <div className="HomeRightDiv">
      <div className="HomeRightImgDiv">
        <img className="HomeRightImg" src={slideImage3} alt="no"></img>
      </div>
      <div className="HomeRightToolTipDiv">
        <span className="HomeRightToolTipText">
          상대방의 관심사를 파악해보세요.
        </span>
      </div>
    </div>
  );
}

export default Slide3;
