import { Link } from "react-router-dom";
import { Button } from "../ui/button";

export default function Header () {

    return (
        <header className="bg-white shadow-md">
            <div className="container mx-auto px-6 py-4 flex items-center justify-between">
            <a href="/" className="text-2xl font-bold">Desenvolva suas habilidades</a>
            <nav className="space-x-4">
                <a href="#courses" className="hover:text-blue-600">Cursos</a>
                <a href="#about" className="hover:text-blue-600">Sobre</a>
                <a href="#contact" className="hover:text-blue-600">Contato</a>
                <Link to="/login"> <Button variant="default" className="cursor-pointer">Login</Button> </Link>
                
            </nav>
            </div>
        </header>
    )
}
