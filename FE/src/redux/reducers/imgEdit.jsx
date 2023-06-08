import { createSlice } from "@reduxjs/toolkit";

const imgEdit = createSlice({
  name: "imgEdit",
  initialState: { value: File },
  reducers: {
    edit: (state, action) => {
      state.value = action.payload;
    },
  },
});

export default imgEdit.reducer;
export const { img } = imgEdit.actions;
