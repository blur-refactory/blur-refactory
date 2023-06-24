import axios from "axios";

const API_URL = `${process.env.REACT_APP_API_ROOT_DONGHO}`;

export const CustomAxios = axios.create({
  baseURL: `${API_URL}`, // 기본 서버 주소 입력
});
