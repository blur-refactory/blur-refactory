import { createSlice } from "@reduxjs/toolkit";

const userEdit = createSlice({
  name: "userEdit",
  initialState: { value: "" },
  reducers: {
    nickname: (state, action) => {
      state.nickname = action.payload;
      console.log(state.ncikname);
    },
    intro: (state, action) => {
      state.intro = action.payload;
    },
    age: (state, action) => {
      state.age = action.payload;
    },
  },
});

export default userEdit.reducer;
export const { nickname, intro, age } = userEdit.actions;
