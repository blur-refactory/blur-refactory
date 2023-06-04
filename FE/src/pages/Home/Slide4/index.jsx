import slideImage4 from "../../../assets/images/slideImage4.jpg";

function Slide4() {
  return (
    <div className="HomeRightDiv">
      <div className="HomeRightImgDiv">
        <img className="HomeRightImg" src={slideImage4} alt="no"></img>
      </div>
      <div className="HomeRightToolTipDiv">
        <span className="HomeRightToolTipText">
          상대방의 관심사를 파악해보세요.
        </span>
      </div>
    </div>
  );
}

export default Slide4;
