import { createSlice } from "@reduxjs/toolkit";

const userEdit = createSlice({
  name: "userEdit",
  initialState: { value: "" },
  reducers: {
    nickname: (state, action) => {
      state.value = action.payload;
    },
    intro: (state, action) => {
      state.value = action.payload;
    },
    age: (state, action) => {
      state.value = action.payload;
    },
  },
});

export default userEdit.reducer;
export const { nickname, intro, age } = userEdit.actions;
