import { createSlice, type PayloadAction } from "@reduxjs/toolkit";

type CartItem = {
  id: string;
  nome: string;
  precoEmCentavos: number;
  descricao: string;
  professor: string;
  categoria: string;
};

type CartState = {
  items: CartItem[];
};

const initialState: CartState = {
  items: [], // Carrinho começa vazio
};

const cartSlice = createSlice({
  name: "cart",
  initialState,
  reducers: {
    addItemToCart: (state, action: PayloadAction<CartItem>) => {
      state.items.push(action.payload); // Adiciona o item ao carrinho
    },
    removeItemFromCart: (state, action: PayloadAction<string>) => {
      state.items = state.items.filter((item) => item.id !== action.payload);
    },
    clearCart: (state) => {
      state.items = [];
    },    
  },
});

export const { addItemToCart, removeItemFromCart, clearCart } = cartSlice.actions;

export default cartSlice.reducer;
