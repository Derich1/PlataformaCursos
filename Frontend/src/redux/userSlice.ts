import { createSlice, type PayloadAction } from "@reduxjs/toolkit";
import type { Usuario } from "../types/usuario";

interface UserState {
  token: string | null;
  user: Usuario | null;
}

const initialState: UserState = {
  token: null,
  user: null,
};

const userSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    loginSuccess: (state, action: PayloadAction<UserState>) => {
      state.token = action.payload.token;
      state.user = action.payload.user;
    },
    updateUser: (state, action: PayloadAction<UserState>) => {
      state.token = action.payload.token;
      state.user = action.payload.user;
    },
    logout: (state) => {
      state.token = null;
      state.user = null;
    },
  },
});

export const { loginSuccess, logout, updateUser } = userSlice.actions;
export default userSlice.reducer;
