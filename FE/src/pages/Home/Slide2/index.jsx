import img2 from "../../../assets/images/HomeRightImg.png";

function Slide2() {
  return (
    <div className="HomeRightDiv">
      <div className="HomeRightImgDiv">
        <img className="HomeRightImg" src={img2} alt="no"></img>
      </div>
      <div className="HomeRightToolTipDiv">
        <span className="HomeRightToolTipText">
          적극적인 대화는 이성에게 어필 포인트가 될 수 있습니다.
        </span>
      </div>
    </div>
  );
}

export default Slide2;
