import "./index.css";

function BlurInfo({ showBlurInfoModal }) {
  return (
    <div className="BlurInfoSubFrame">
      <p className="CommNavLogo">Blur :-)</p>
      <button className="BlurInfoClose" onClick={showBlurInfoModal}></button>
      <p className="Paragraph1">
        블러(blur)는 대표적인 첫인상인 외모를 차단함으로써 자신의 첫인상을
        만들어가자는 취지로 만들어졌습니다.
        <br></br>
        외모가 아닌 내면의 가치를 여러분이 되기를 희망합니다.
      </p>
      <p className="Paragraph2">
        간단 설명
        <ol>
          <li>블러 소개팅을 시작하기 전 선호 취향을 설정합니다.</li>
          <li>블러 소개팅이 시작되면 블러처리가된 상대방이 등장합니다.</li>
          <li>블러는 시간이 지남에 따라 점점 약해집니다.</li>
          <li>
            시간이 다 지나게 되면 서로의 얼굴을 확인 할 수 있으며 채팅방이
            자동으로 만들어집니다.
          </li>
        </ol>
        주의!!! 채팅방을 나가게되면 다시 만들어지지 않으므로 나감에 있어 신중
        해주세요.
      </p>
    </div>
  );
}

export default BlurInfo;
