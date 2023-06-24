import { createSlice } from "@reduxjs/toolkit";

const saveTokenReducer = createSlice({
  name: "saveTokenReducer",
  initialState: {
    isLogin: "false",
    savedId: "",
    id: "",
    profiled: "false",
  },
  reducers: {
    saveLogin: (state, action) => {
      state.isLogin = action.payload;
    },

    saveId: (state, action) => {
      state.savedId = action.payload;
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
export const { saveLogin, saveId, loginId, ISMYPROFILE } = saveTokenReducer.actions;
