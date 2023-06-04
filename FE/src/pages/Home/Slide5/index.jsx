import slideImage5 from "../../../assets/images/slideImage5.jpg";

function Slide5() {
  return (
    <div className="HomeRightDiv">
      <div className="HomeRightImgDiv">
        <img className="HomeRightImg" src={slideImage5} alt="no"></img>
      </div>
      <div className="HomeRightToolTipDiv">
        <span className="HomeRightToolTipText">외모는 중요하지 않습니다.</span>
      </div>
    </div>
  );
}

export default Slide5;
