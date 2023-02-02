import "./modalWrap.css";

function ModalWrap({
  showSignInModal,
  showSignUpModal,
  showSearchPwModal,
  showBlurInfoModal,
  signInModal,
  signUpModal,
  searchPwModal,
  blurInfoModal,
}) {
  return (
    <div
      className="SIModalWrap"
      onMouseDown={() => {
        if (signInModal === true) {
          showSignInModal();
        } else if (signUpModal === true) {
          showSignUpModal();
        } else if (searchPwModal === true) {
          showSearchPwModal();
        } else if (blurInfoModal === true) {
          showBlurInfoModal();
        }
      }}
    >
      어두운배경
    </div>
  );
}

export default ModalWrap;
