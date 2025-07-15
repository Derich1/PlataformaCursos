import { createSlice, type PayloadAction } from '@reduxjs/toolkit'
import type { Usuario } from '../types/usuario'
import type { LoginResponseDTO } from '../types/loginResponse'

interface UserState {
  user: Usuario | null
  token: string | null
}

const initialState: UserState = {
  user: null,
  token: null,
}

const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    loginSuccess: (state, action: PayloadAction<LoginResponseDTO>) => {
      state.token = action.payload.token
      state.user = action.payload.usuario;
    },
    updateUser: (state, action: PayloadAction<UserState>) => {
      state.token = action.payload.token;
      state.user = action.payload.user;
    },
    logout: (state) => {
      state.user = null
      state.token = null
    },
  },
})

export const { loginSuccess, updateUser, logout } = userSlice.actions
export default userSlice.reducer

