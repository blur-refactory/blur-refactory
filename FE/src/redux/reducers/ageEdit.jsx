// reducer 부분은 createSlice만 있으면 된다.
import { createSlice } from "@reduxjs/toolkit";

const ageEdit = createSlice({
  name: "ageEdit",
  initialState: { value: "" },
  reducers: {
    age: (state, action) => {
      state.value = action.payload;
    },
  },
});

export default ageEdit.reducer;
export const { age } = ageEdit.actions;
