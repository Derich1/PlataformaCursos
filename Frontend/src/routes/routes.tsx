import { Route, Routes } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import Home from "../Pages/Home";
import Cadastro from "../Pages/Cadastro";
import Login from "../Pages/Login";
import Header from "../components/Header";
import { CadastroCurso } from "../Pages/CadastroCurso";
import { Curso } from "../Pages/Curso";


export default function AppRoutes() {
    

    return(
        <div className="flex flex-col min-h-screen">
          <ToastContainer
            position="top-right"
            autoClose={3000}
            hideProgressBar={false}
            newestOnTop={false}
            closeOnClick
            rtl={false}
            pauseOnFocusLoss
            draggable
            pauseOnHover
            style={{ zIndex: 100000 }}
          />
          <Header/>
      
      <Routes>
          <Route index element={<Home />} />
          <Route path="cadastrar" element={<Cadastro />} />
          <Route path="login" element={<Login />} />
          <Route path="cadastrarCurso" element={<CadastroCurso />} />
          <Route path="/curso/:id" element={<Curso />} />
      </Routes>
    </div>
    )
}