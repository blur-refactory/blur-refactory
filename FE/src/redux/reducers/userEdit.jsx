import { createSlice } from "@reduxjs/toolkit";

const userEdit = createSlice({
  name: "userEdit",
  initialState: { 
    nickname: "",
    intro: "",
    age: ""
  },
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

export const { nickname, intro, age } = userEdit.actions;
export default userEdit.reducer;
