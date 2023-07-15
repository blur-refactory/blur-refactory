import { createSlice } from "@reduxjs/toolkit";

const userEdit = createSlice({
  name: "userEdit",
  initialState: { 
    nickname: "",
    intro: "",
    age: "",
    image: ""
  },
  reducers: {
    nickname: (state, action) => {
      state.nickname = action.payload;
    },
    intro: (state, action) => {
      state.intro = action.payload;
    },
    age: (state, action) => {
      state.age = action.payload;
    },
    image: (state, action) => {
      state.image = action.payload;
    },
  },
});

export const { nickname, intro, age, image } = userEdit.actions;
export default userEdit.reducer;
