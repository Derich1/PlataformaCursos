import { useSelector } from "react-redux";
import type { RootState } from "../../redux/store";
import axios from "axios";
import { useEffect, useState } from "react";
import type { CursoDTO } from "../../types/curso";

type Cursos = {
  curso: CursoDTO | null;
};

export const cursosMatriculados = () => {
  const usuario = useSelector((state: RootState) => state.user.user);
  const [cursos, setCursos] = useState<Cursos[] | null>(null);

  const handleCursosMatriculados = async () => {
    const response = await axios.get(
      `http://localhost:8081/usuario/cursosMatriculados/${usuario!.id}`
    );
    setCursos(response.data);
  };

  useEffect(() => {
    handleCursosMatriculados();
  }, []);

  if (!usuario) return;

  return (
    <>
      {cursos &&
        cursos.map((item, index) => <div key={index}>{item.curso?.nome}</div>)}
    </>
  );
};
