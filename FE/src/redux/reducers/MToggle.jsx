// MToggle.jsx : MeetingPage에서 매칭 전/후를 나타내기 위한 toggle
import { createSlice } from "@reduxjs/toolkit";

const MToggle = createSlice({
  name: "MToggle",
  initialState: {
    togg: false,
    isShowBlockModal: false,
    closeAlertToggle: false,
    camOpenToggle: false,
    roomNumber: 1,
    myGender: "",
    myGeo: {},
    partnerInterests: [],
    partnerNick: "",
    partnerId: "",
    roomToken: "",
  },
  reducers: {
    /**
     * modify comp: meetingNotIn
     * desc: meetingNotIn or meetingIn을 보여주기위한 토글변수
     */
    MTOGGLE: (state, action) => {
      state.togg = !state.togg;
    },
    /**
     * modify comp: meetingIn
     * desc: 신고이미지버튼 -> 신고하기 클릭시에 나오는 모달 토글변수
     */
    BTOGGLE: (state, action) => {
      state.isShowBlockModal = action.payload;
    },
    /**
     * modify comp: meetingNotIn
     * desc: makingRoomName를 통해 생성한 미팅 방번호
     */
    ROOM_NUM: (state, action) => {
      state.roomNumber = action.payload;
    },
    /**
     * modify comp: meetingIn
     * desc: 신고 완료시 알람모달 토글변수
     */
    CLOSE_ALERT_TOGGLE: (state, action) => {
      state.closeAlertToggle = action.payload;
    },
    /**
     * modify comp: meetingIn
     * desc: 나의 캠 세팅 토글변수
     */
    CAM_OPEN_TOGGLE: (state, action) => {
      state.camOpenToggle = action.payload;
    },
    /**
     * modify comp: meetingNotIn
     * desc: 백엔드로부터 받은 내 성별
     */
    MYGENDER: (state, action) => {
      state.myGender = action.payload;
    },
    /**
     * modify comp: start
     * desc: 내 위도/경도 객체
     */
    MYGEO: (state, action) => {
      state.myGeo = action.payload;
    },
    /**
     * modify comp: meetingNotIn
     * desc: 백엔드로부터 받은 파트너 관심사 리스트
     */
    PARTNERINTERESTS: (state, action) => {
      state.partnerInterests = action.payload;
    },
    PARTNERNICK: (state, action) => {
      state.partnerNick = action.payload;
    },
    PARTNERID: (state, action) => {
      state.partnerId = action.payload;
    },
    ROOMTOKEN: (state, action) => {
      state.roomToken = action.payload;
    },
  },
});

// 리듀서메서드명.reducer에 해당 리듀서가 담겨있음
export default MToggle.reducer;
export const {
  MTOGGLE,
  BTOGGLE,
  ROOM_NUM,
  CLOSE_ALERT_TOGGLE,
  CAM_OPEN_TOGGLE,
  MYGENDER,
  MYGEO,
  PARTNERINTERESTS,
  PARTNERNICK,
  PARTNERID,
  ROOMTOKEN,
} = MToggle.actions;
