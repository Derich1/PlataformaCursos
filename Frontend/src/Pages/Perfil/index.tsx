import React from 'react';
import { useSelector } from 'react-redux';
import type { RootState } from '../../redux/store';

export const Perfil: React.FC = () => {
  const usuario = useSelector((state: RootState) => state.user.user)

  if (!usuario) return <div className="text-center mt-10 text-gray-500">Carregando perfil...</div>;

  return (
    <div className="max-w-2xl mx-auto p-6 mt-10 bg-white shadow-xl rounded-2xl">
      <h1 className="text-3xl font-bold text-center mb-6 text-blue-700">Perfil do Usuário</h1>

      <div className="space-y-2">
        <p><span className="font-semibold">Nome:</span> {usuario.nome}</p>
        <p><span className="font-semibold">Documento:</span> {usuario.documento}</p>
        <p><span className="font-semibold">Data de Nascimento:</span> {usuario.dataNascimento}</p>
        <p><span className="font-semibold">Email:</span> {usuario.email}</p>
        <p><span className="font-semibold">Tipo de usuário:</span> {usuario.tipo}</p>
      </div>

      {usuario.cursosMatriculados !== undefined && (
        <div className="mt-6">
        <h2 className="text-xl font-semibold text-gray-700">Cursos Matriculados:</h2>
        <ul className="list-disc ml-6 mt-1 text-gray-600">
          {usuario.cursosMatriculados.length > 0 ? (
            usuario.cursosMatriculados.map((curso, i) => <li key={i}>{curso}</li>)
          ) : (
            <li>Nenhum curso matriculado</li>
          )}
        </ul>
      </div>
      )}

      {usuario.tipo === 'professor' && usuario.cursosCriados !== undefined && (
        <div className="mt-6">
          <h2 className="text-xl font-semibold text-gray-700">Cursos Criados:</h2>
          <ul className="list-disc ml-6 mt-1 text-gray-600">
            {usuario.cursosCriados.length > 0 ? (
              usuario.cursosCriados.map((curso, i) => <li key={i}>{curso}</li>)
            ) : (
              <li>Nenhum curso criado</li>
            )}
          </ul>
        </div>
      )}
    </div>
  );
};

export default Perfil;
