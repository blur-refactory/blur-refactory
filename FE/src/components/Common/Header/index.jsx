import "./index.css";
import { Link } from "react-router-dom";
import { loginId, saveToken } from "../../../redux/reducers/saveToken";
import { useSelector } from "react-redux";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";

function Header({ showSignInModal, showChatList }) {
  const userState = useSelector((state) => state.strr.token);

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const logInOut = () => {
    if (userState) {
      dispatch(saveToken(null));
      dispatch(loginId(null));

      console.log("로그아웃");

      navigate("/");
    } else {
      showSignInModal();
    }
  };

  const MyInfo = () => {
    if (userState) {
      navigate("/MyInfo");
    } else {
      console.log(userState);
      showSignInModal();
    }
  };

  const Chat = () => {
    if (userState) {
      navigate("/home");

      //채팅창 뜨는 기능 추가 예정
      showChatList();
    } else {
      showSignInModal();
    }
  };

  const BlurIcon = () => {
    if (userState) {
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
          {userState ? "Logout" : "Login"}
        </span>
      </div>
    </div>
  );
}

export default Header;

{/* <Link to="/MyInfo">
  <span className="CommNavBtnMyInfo">MyInfo</span>
</Link>; */}
