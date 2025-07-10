import { createSlice, type PayloadAction } from "@reduxjs/toolkit";
import type { CursoDTO } from "../types/curso";

type CursoState = {
    curso: CursoDTO | null
    todosCursos: CursoDTO[]
}

const initialState: CursoState = {
  curso: null,
  todosCursos: []
};

const cursoSlice = createSlice({
  name: "curso",
  initialState,
  reducers: {
    setCursos: (state, action: PayloadAction<CursoDTO[]>) => {
      state.todosCursos = action.payload
    },
    setCurso: (state, action: PayloadAction<CursoDTO>) => {
      state.curso = action.payload
    },
  },
});

export const { setCursos, setCurso } = cursoSlice.actions;

export default cursoSlice.reducer;
