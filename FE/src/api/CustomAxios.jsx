import axios from "axios";
// import { useSelector } from "react-redux";

const API_URL = `${process.env.REACT_APP_API_ROOT_WONWOONG}`;
// export default function CustomAxios() {
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
  baseURL: `${API_URL}`, // 기본 서버 주소 입력
  headers: {
    access_token: "1",
  },
});
