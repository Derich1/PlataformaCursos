import { combineReducers, configureStore } from "@reduxjs/toolkit";
import storage from 'redux-persist/lib/storage'; // usa localStorage no navegador
import { persistReducer, persistStore } from "redux-persist";
import userReducer from "./userSlice"
import pagamentoReducer from "./pagamentoSlice"
import cartReducer from "./cartSlice"
import cursoReducer from "./cursoSlice"

const persistConfig = {
  key: 'root', // chave de persistência
  storage,     // armazenamento (localStorage)
  whitelist: ['user', 'curso'] // reducers que serão persistidos (ex.: user)
};

const rootReducer = combineReducers({
  user: userReducer,
  cart: cartReducer,
  pagamento: pagamentoReducer,
  curso: cursoReducer
});

const persistedReducer = persistReducer(persistConfig, rootReducer);

export const store = configureStore({
  reducer: persistedReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        // 🛠 Ignora as actions do redux-persist que não são serializáveis
        ignoredActions: ['persist/PERSIST', 'persist/REHYDRATE', 'persist/REGISTER'],
      },
    }),
});

export const persistor = persistStore(store);


// Tipos para TypeScript
export type RootState = ReturnType<typeof rootReducer>;
