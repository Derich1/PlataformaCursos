// src/pages/CursoDetalhes.tsx
import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { Button } from "../../components/ui/button";
import type { CursoDTO } from "../../types/curso";

export const Curso: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const [curso, setCurso] = useState<CursoDTO | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [moduloAberto, setModuloAberto] = useState(null);

  const toggleModulo = (index: any) => {
    setModuloAberto(moduloAberto === index ? null : index);
  };

  useEffect(() => {
    if (!id) {
      setError("ID de curso inválido.");
      setLoading(false);
      return;
    }

    const fetchCurso = async () => {
      try {
        const response = await axios.get<CursoDTO>(`http://localhost:8082/curso/${id}`);
        setCurso(response.data);
      } catch (err) {
        console.error(err);
        setError("Não foi possível carregar os detalhes do curso.");
      } finally {
        setLoading(false);
      }
    };

    fetchCurso();
  }, [id]);

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <p className="text-lg font-medium">Carregando curso...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex flex-col items-center justify-center min-h-screen px-4">
        <p className="text-red-600 font-semibold mb-4">{error}</p>
        <Button variant="outline" onClick={() => navigate(-1)}>
          Voltar
        </Button>
      </div>
    );
  }

  if (!curso) {
    return (
      <div className="flex flex-col items-center justify-center min-h-screen px-4">
        <p className="text-gray-700 mb-4">Curso não encontrado.</p>
        <Button variant="outline" onClick={() => navigate(-1)}>
          Voltar
        </Button>
      </div>
    );
  }

  return (
    <div className="container mx-auto py-10 px-4">
      <div className="max-w-2xl mx-auto shadow-md rounded-lg border">
        <div className="p-6 border-b">
          <h1 className="text-2xl font-bold">{curso.nome}</h1>
        </div>
        <div className="p-6 space-y-4">
          <div>
            {new Intl.NumberFormat("pt-BR", {
              style: "currency",
              currency: "BRL",
            }).format(curso.preco)}
          </div>
          <div>
            <p className="text-gray-700">{curso.descricao}</p>
          </div>
          <div>
            <span className="font-semibold">Criado por</span> {curso.professor}
          </div>
          <div>{curso.categoria}</div>

          {curso.modulos.length > 0 && (
            <div>
              <span className="font-semibold block mb-2">Conteúdo do curso</span>
              <div className="space-y-2">
                {curso.modulos.map((modulo, index) => {
                  const isOpen = moduloAberto === index;
                  return (
                    <div
                      key={index}
                      className="border rounded-lg shadow-sm overflow-hidden transition-all duration-500"
                    >
                      <button
                        onClick={() => toggleModulo(index)}
                        className="w-full text-left p-4 bg-gray-100 hover:bg-gray-200 font-semibold"
                      >
                        {modulo.titulo}
                      </button>

                      <div
                        className={`transition-all duration-500 ease-in-out ${
                          isOpen ? "max-h-[500px] opacity-100" : "max-h-0 opacity-0"
                        } overflow-hidden`}
                      >
                        <ul className="p-4 pl-6 list-disc text-gray-700">
                          {modulo.aulas.map((aula, i) => (
                            <li key={i}>
                              {aula.titulo} – {aula.duracaoEmMinutos ?? 0} min
                            </li>
                          ))}
                        </ul>
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
