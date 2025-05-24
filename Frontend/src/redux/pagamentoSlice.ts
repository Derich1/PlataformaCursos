import { createSlice, type PayloadAction } from "@reduxjs/toolkit";

interface Pagamento {
  idCurso: string;
  idComprador: string;
  metodoPagamento: string;
  valor: number;
  parcelas: number;
  status: boolean;
}

interface PagamentoState {
  pagamento: Pagamento | null
}

const initialState: PagamentoState = {
  pagamento: null
};

const pagamentoSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setPagamento: (state, action: PayloadAction<Pagamento>) => {
      state.pagamento = action.payload
    },
  },
});

export const { setPagamento } = pagamentoSlice.actions;
export default pagamentoSlice.reducer;
