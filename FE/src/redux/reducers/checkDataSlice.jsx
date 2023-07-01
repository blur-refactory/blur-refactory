import { createSlice } from "@reduxjs/toolkit";

const checkDataSlice = createSlice({
  name: "checkData",
  initialState: {
    checkIntData: [],
  },
  reducers: {
    setCheckDataa: (state, action) => {
      state.checkIntData = action.payload;
   
    },
  },
});

export const { setCheckDataa } = checkDataSlice.actions;

export default checkDataSlice.reducer;
