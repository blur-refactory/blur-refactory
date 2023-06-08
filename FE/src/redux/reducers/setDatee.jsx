import { createSlice } from "@reduxjs/toolkit";

const setDatee = createSlice({
  name: "setDatee",
  initialState: {
    distancee: 20,
    ageRange: [0, 100],
  },
  reducers: {
    setDistancee(state, action) {
      state.distancee = action.payload;
    },
    setAgeRange(state, action) {
      state.ageRange = action.payload;
    },
  },
});

export const { setDistancee, setAgeRange } = setDatee.actions;
export default setDatee.reducer;
