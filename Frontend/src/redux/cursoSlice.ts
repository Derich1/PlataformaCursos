import { createSlice, type PayloadAction } from "@reduxjs/toolkit";

type Curso = {
  id: string;
  nome: string;
  preco: number;
  descricao: string;
  professor: string;
  categoria: string;
};

type CursoState = {
    curso: Curso[] | null
}

const initialState: CursoState = {
  curso: null
};

const cartSlice = createSlice({
  name: "cart",
  initialState,
  reducers: {
    setCursos: (state, action: PayloadAction<Curso[]>) => {
      state.curso = action.payload // Adiciona o item ao carrinho
    },
  },
});

export const { setCursos } = cartSlice.actions;

export default cartSlice.reducer;
