import { useEffect } from "react";
import { loginId, saveToken } from "../../../redux/reducers/saveToken";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { useLocation } from "react-router-dom";
import axios from "axios";

function SocialSignInRedirect() {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const location = useLocation();

  useEffect(() => {
    console.log(location);

    const token = location.search.substring(7);
    console.log(token);

    if (token) {
      dispatch(saveToken(token));
      navigate("/home");

      axios({
        method: "get",
        url: `${process.env.REACT_APP_API_ROOT_DONGHO}/blur-auth/auth`,
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      })
        .then((res) => {
          console.log(res.data.body.userId);
          dispatch(loginId(res.data.body.userId));
        })
        .catch((err) => {
          console.log(err);
          alert("아이디가 저장되지 못했습니다.");
        });
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
  return <></>;
}

export default SocialSignInRedirect;
