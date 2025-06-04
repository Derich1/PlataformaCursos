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
      <Button className="mb-6" variant="ghost" onClick={() => navigate(-1)}>
        ← Voltar
      </Button>

      <Card className="max-w-2xl mx-auto shadow-md">
        <CardHeader>
          <CardTitle className="text-2xl">{curso.nome}</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            <div>
              <span className="font-semibold">Preço:</span>{" "}
              <span>R$ {curso.preco.toFixed(2)}</span>
            </div>
            <div>
              <span className="font-semibold">Descrição:</span>
              <p className="mt-1 text-gray-700">{curso.descricao}</p>
            </div>
            <div>
              <span className="font-semibold">Professor:</span>{" "}
              <span>{curso.professor}</span>
            </div>
            <div>
              <span className="font-semibold">Categoria:</span>{" "}
              <span>{curso.categoria}</span>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};
