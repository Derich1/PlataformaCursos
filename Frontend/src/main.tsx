import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import AppRoutes from './routes/routes'
import { Provider } from 'react-redux'
import { PersistGate } from 'redux-persist/integration/react'
import { persistor, store } from './redux/store'
import "./index.css"

createRoot(document.getElementById('root')!).render(
  <Provider store={store}>
        <PersistGate loading={<div>Carregando...</div>} persistor={persistor}>
            <BrowserRouter>
              <AppRoutes />
            </BrowserRouter>
        </PersistGate>
    </Provider>
)
