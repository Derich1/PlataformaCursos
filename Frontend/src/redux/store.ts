import { combineReducers, configureStore } from "@reduxjs/toolkit";
import storage from 'redux-persist/lib/storage'; // usa localStorage no navegador
import { persistReducer, persistStore } from "redux-persist";
import userReducer from "./userSlice"
import pagamentoReducer from "./pagamentoSlice"
import cartReducer from "./cartSlice"

const persistConfig = {
  key: 'root', // chave de persistência
  storage,     // armazenamento (localStorage)
  whitelist: ['user', 'cart'] // reducers que serão persistidos (ex.: user)
};

const rootReducer = combineReducers({
  user: userReducer,
  cart: cartReducer,
  pagamento: pagamentoReducer
});

const persistedReducer = persistReducer(persistConfig, rootReducer);

export const store = configureStore({
  reducer: persistedReducer,
});

export const persistor = persistStore(store);


// Tipos para TypeScript
export type RootState = ReturnType<typeof rootReducer>;
