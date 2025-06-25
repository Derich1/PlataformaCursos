// src/pages/CursoDetalhes.tsx
import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { Card, CardHeader, CardTitle, CardContent } from "../../components/ui/card";
import { Button } from "../../components/ui/button";

interface Curso {
  id: string;
  nome: string;
  preco: number;
  descricao: string;
  professor: string;
  categoria: string;
  modulos: string[];
}

export const Curso: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const [curso, setCurso] = useState<Curso | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!id) {
      setError("ID de curso inválido.");
      setLoading(false);
      return;
    }

    const fetchCurso = async () => {
      try {
        const response = await axios.get<Curso>(`http://localhost:8082/curso/${id}`);
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
      <Button className="mb-6 cursor-pointer" variant="ghost" onClick={() => navigate(-1)}>
        ← Voltar
      </Button>

      <Card className="max-w-2xl mx-auto shadow-md">
        <CardHeader>
          <CardTitle className="text-2xl">{curso.nome}</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            <div>
              <span>
                {new Intl.NumberFormat("pt-BR", {
                    style: "currency",
                    currency: "BRL",
                }).format(curso.preco)}
              </span>
            </div>
            <div>
              <span className="font-semibold">Descrição:</span>
              <p className="mt-1 text-gray-700">{curso.descricao}</p>
            </div>
            <div>
              <span className="font-semibold">Criado por</span>{" "}
              <span>{curso.professor}</span>
            </div>
            <div>
              <span>{curso.categoria}</span>
            </div>

            {curso.modulos.length > 0 && (
                <div>
                    <span className="font-semibold block mb-2">Vídeos:</span>
                    <div className="space-y-4">
                    {curso.modulos.map((key, index) => {
                        const videoUrl = `https://plataforma-cursos-bucket-sp.s3.sa-east-1.amazonaws.com/${key}`;
                        return (
                        <video key={index} controls className="w-full rounded-lg shadow">
                            <source src={videoUrl} type="video/mp4" />
                            Seu navegador não suporta o elemento de vídeo.
                        </video>
                        );
                    })}
                    </div>
                </div>
            )}

          </div>
        </CardContent>
      </Card>
    </div>
  );
};
