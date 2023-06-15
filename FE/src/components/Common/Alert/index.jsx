import "./index.css";

function Alert({ showAlertModal, content }) {
  return (
    <div className="AlertDiv">
      <div className="AlertHeader">
        <span className="AlertHeaderText">Alert</span>
      </div>
      <span className="AlertDesc">{content}</span>
      <button className="AlertBtnDiv" onClick={showAlertModal}>
        <span className="AlertBtnText">확인</span>
      </button>
    </div>
  );
}

export default Alert;
