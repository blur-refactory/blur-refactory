import { createSlice } from "@reduxjs/toolkit";

const saveTokenReducer = createSlice({
  name: "saveTokenReducer",
  initialState: {
    isLogin: "false",
    id: "",
    profiled: "false",
  },
  reducers: {
    saveLogin: (state, action) => {
      state.isLogin = action.payload;
    },

    loginId: (state, action) => {
      state.id = action.payload;
    },

    ISMYPROFILE: (state, action) => {
      state.profiled = action.payload;
    },
  },
});

export default saveTokenReducer.reducer;
export const { saveLogin, loginId, ISMYPROFILE } = saveTokenReducer.actions;
