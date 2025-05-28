
import axios from "axios";
import { Button } from "../../components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "../../components/ui/card";
import { setCursos } from "../../redux/cursoSlice";
import { useDispatch, useSelector } from "react-redux";
import { useEffect } from "react";
import type { RootState } from "../../redux/store";

export default function Home() {

    const dispatch = useDispatch()
    const cursos = useSelector((state: RootState) => state.curso.curso)

    useEffect(() => {
        const handleListarCursos = async() => {
            try {
                const response = await axios.get("http://localhost:8082/curso")
                dispatch(setCursos(response.data))
            } catch(error) {
                console.log(error)
            }
        }

        handleListarCursos()
    }, [])

    if (!cursos) return
    

  return (
    <div className="min-h-screen flex flex-col">
      {/* Header */}
      <header className="bg-white shadow-md">
        <div className="container mx-auto px-6 py-4 flex items-center justify-between">
          <h1 className="text-2xl font-bold">Desenvolva suas habilidades</h1>
          <nav className="space-x-4">
            <a href="#courses" className="hover:text-blue-600">Cursos</a>
            <a href="#about" className="hover:text-blue-600">Sobre</a>
            <a href="#contact" className="hover:text-blue-600">Contato</a>
            <Button variant="default">Login</Button>
          </nav>
        </div>
      </header>

      {/* Hero Section */}
      <section className="bg-blue-50 flex-1 flex items-center">
        <div className="container mx-auto px-6 py-20 text-center">
          <h2 className="text-4xl font-extrabold mb-4">Aprenda e Cresça com Nossos Cursos Online</h2>
          <p className="text-lg mb-8">Conteúdo atualizado, instrutores especializados e comunidade ativa.</p>
          <Button size="lg">Ver Cursos</Button>
        </div>
      </section>

      {/* Featured Courses */}
      <section id="courses" className="container mx-auto px-6 py-16">
        <h3 className="text-3xl font-semibold mb-8 text-center">Cursos em Destaque</h3>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          {cursos.map(course => (
            <Card key={course.id} className="hover:shadow-lg transition-shadow">
              <CardHeader>
                <CardTitle>{course.nome}</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="mb-4 text-sm text-gray-600">{course.descricao}</p>
                <Button variant="outline">Saiba Mais</Button>
              </CardContent>
            </Card>
          ))}
        </div>
      </section>

      {/* About / Testimonials */}
      <section id="about" className="bg-gray-100 py-16">
        <div className="container mx-auto px-6">
          <h3 className="text-3xl font-semibold mb-8 text-center">O Que Nossos Alunos Dizem</h3>
          <div className="space-y-6 max-w-2xl mx-auto">
            <blockquote className="border-l-4 border-blue-600 pl-4 italic text-gray-700">
              "A plataforma me ajudou a alcançar meu primeiro emprego como desenvolvedor. Conteúdo excelente!"
            </blockquote>
            <blockquote className="border-l-4 border-blue-600 pl-4 italic text-gray-700">
              "Instrutores muito didáticos e suporte rápido. Recomendo para quem quer aprender de verdade."
            </blockquote>
          </div>
        </div>
      </section>

      <section id="contact" className="bg-gray-100 py-16">
          <a href="https://www.linkedin.com/feed/">linkedin</a>
      </section>

      {/* Footer */}
      <footer className="bg-white border-t py-6">
        <div className="container mx-auto px-6 text-center text-gray-600 text-sm">
          © {new Date().getFullYear()} Desenvolva suas habilidades. Todos os direitos reservados.
        </div>
      </footer>
    </div>
  );
}
