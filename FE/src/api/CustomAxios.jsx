import axios from "axios";
import { useSelector } from "react-redux";

// export default function CustomAxios() {
//   const API_URL = `${process.env.REACT_APP_API_ROOT_WONWOONG}`;
//   // let myToken = useSelector((state) => state.strr.token); // store에 저장되어있는 토큰

//   return axios.create({
//     baseURL: `${API_URL}`,
//     // headers: {
//     //   "Content-Type": "application/json",
//     //   Authorization: `Bearer ${myToken}`,
//     // },
//   });
// }

export const CustomAxios = axios.create({
  baseURL: `1`, // 기본 서버 주소 입력
  headers: {
    access_token: "1",
  },
});
