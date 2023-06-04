import slideImage2 from "../../../assets/images/slideImage2.jpg";

function Slide1() {
  return (
    <div className="HomeRightDiv">
      <div className="HomeRightImgDiv">
        <img className="HomeRightImg" src={slideImage2} alt="no"></img>
      </div>
      <div className="HomeRightToolTipDiv">
        <span className="HomeRightToolTipText">
          자신감있는 대화로 당신의 매력을 어필하세요.
        </span>
      </div>
    </div>
  );
}

export default Slide1;
