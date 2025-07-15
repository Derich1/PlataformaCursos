import { Link } from "react-router-dom";
import { Button } from "../ui/button";
import { useSelector } from "react-redux";
import type { RootState } from "../../redux/store";
import { CgProfile } from "react-icons/cg";

export default function Header () {
    const usuario = useSelector((state: RootState) => state.user.user)

    return (
        <header className="bg-white shadow-md sticky top-0 z-50">
            <div className="container mx-auto px-6 py-4 flex items-center justify-between">
                <a href="/" className="text-2xl font-bold text-blue-700 hover:text-blue-900 transition-colors">
                Desenvolva suas habilidades
                </a>
                
                <nav className="flex items-center space-x-4">
                <a href="#courses" className="text-gray-700 hover:text-blue-600 transition-colors">
                    Cursos
                </a>
                <a href="#about" className="text-gray-700 hover:text-blue-600 transition-colors">
                    Sobre
                </a>
                <a href="#contact" className="text-gray-700 hover:text-blue-600 transition-colors">
                    Contato
                </a>

                {usuario ? (
                    <Link to="/perfil" className="text-gray-700 hover:text-blue-600 transition-colors text-xl">
                    <CgProfile />
                    </Link>
                ) : (
                    <Link to="/login">
                    <Button
                        variant="default"
                        className="bg-blue-600 hover:bg-blue-700 text-white font-medium px-4 py-2 rounded transition-colors"
                    >
                        Login
                    </Button>
                    </Link>
                )}

                <Link to="/cadastrarCurso">
                    <Button
                    variant="default"
                    className="bg-green-600 hover:bg-green-700 text-white font-medium px-4 py-2 rounded transition-colors"
                    >
                    Cadastrar Curso
                    </Button>
                </Link>
                </nav>
            </div>
            </header>

    )
}
