// reducer 부분은 createSlice만 있으면 된다.
import { createSlice } from "@reduxjs/toolkit";

const userEdit = createSlice({
  name: "userEdit",
  initialState: { value: "" },
  reducers: {
    edit: (state, action) => {
      state.value = action.payload;
    },
  },
});

export default userEdit.reducer;
export const { edit } = userEdit.actions;
