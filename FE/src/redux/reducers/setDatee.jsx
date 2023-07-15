import { createSlice } from "@reduxjs/toolkit";

const setDatee = createSlice({
  name: "setDatee",
  initialState: {
    distancee: 50,
    ageRange: [0, 50],
  },
  reducers: {
    setDistancee(state, action) {
      state.distancee = action.payload;
      console.log(state.distancee)
    },
    setAgeRange(state, action) {
      state.ageRange = action.payload;
      console.log( state.ageRange)
    },
  },
});

export const { setDistancee, setAgeRange } = setDatee.actions;
export default setDatee.reducer;
