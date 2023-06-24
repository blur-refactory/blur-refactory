import { useEffect } from "react";
import { saveLogin } from "../../../redux/reducers/saveToken";
import { useNavigate, useSearchParams } from "react-router-dom";
import { useDispatch } from "react-redux";
import { useLocation } from "react-router-dom";
import { CustomAxios } from "../../../api/CustomAxios";

function SocialSignInRedirect() {
  const navigate = useNavigate();
  const location = useLocation();

  // const axiosInstance = CustomAxios;
  const [searchParams, setSearchParams] = useSearchParams();
  const dispatch = useDispatch();
  useEffect(() => {
    console.log(location);
    console.log(`searchParams: `, searchParams.get("isLogin"));

    if (searchParams.get("isLogin")) {
      navigate("/home");
      console.log("소셜 로그인 성공");
      dispatch(saveLogin(true));

      // axiosInstance
      //   .get("auth")
      //   .then((res) => {
      //     console.log("소셜 로그인 성공");
      //     // dispatch(loginId(res.data.body.userId));
      //   })
      //   .catch((err) => {
      //     console.log(err);
      //     alert("아이디가 저장되지 못했습니다.");
      //   });
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
  return <></>;
}

export default SocialSignInRedirect;
