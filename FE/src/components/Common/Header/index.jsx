import "./index.css";
import { Link } from "react-router-dom";
import { loginId, saveLogin } from "../../../redux/reducers/saveToken";
import { useSelector, useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";

function Header({ showSignInModal, showChatList }) {
  const isLogin = useSelector((state) => state.strr.isLogin);

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const logInOut = () => {
    if (isLogin) {
      dispatch(saveLogin(false));
      // dispatch(loginId(null));

      console.log("로그아웃");

      navigate("/");
    } else {
      showSignInModal();
    }
  };

  const MyInfo = () => {
    if (isLogin) {
      navigate("/MyInfo");
    } else {
      console.log(isLogin);
      showSignInModal();
    }
  };

  const Chat = () => {
    if (isLogin) {
      navigate("/home");

      //채팅창 뜨는 기능 추가 예정
      showChatList();
    } else {
      showSignInModal();
    }
  };

  const BlurIcon = () => {
    if (isLogin) {
      navigate("/home");
    } else {
      showSignInModal();
    }
  };

  return (
    <div className="CommNavDiv">
      <span className="CommNavLogo" onClick={BlurIcon}>
        Blur:-)
      </span>

      <div className="CommNavBtnsDiv">
        <span className="CommNavBtnChat" onClick={Chat}>
          Chat
        </span>
        <span className="CommNavBtnMyInfo" onClick={MyInfo}>
          MyInfo
        </span>
        <span className="CommNavBtnAbout" onClick={logInOut}>
          {isLogin ? "Logout" : "Login"}
        </span>
      </div>
    </div>
  );
}

export default Header;

{
  /* <Link to="/MyInfo">
  <span className="CommNavBtnMyInfo">MyInfo</span>
</Link>; */
}
