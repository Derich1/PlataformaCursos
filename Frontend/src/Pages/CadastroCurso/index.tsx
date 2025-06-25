// src/pages/CadastrarCurso.tsx
import axios from "axios";
import React, { useState, type FormEvent } from "react";
import type { AulaDTO } from "../../types/aula";
import type { CursoDTO } from "../../types/curso";







export const CadastroCurso: React.FC = () => {
  const [curso, setCurso] = useState<CursoDTO>({
    nome: "",
    preco: 0,
    descricao: "",
    professor: "",
    categoria: "",
    modulos: [],
    quantidadeModulos: 0,
    duracaoTotal: 0,
  });

    const [filesMap, setFilesMap] = useState<Record<string, File>>({});
  const [status, setStatus] = useState<{ carregando: boolean; sucesso: boolean | null; mensagem: string }>({ carregando: false, sucesso: null, mensagem: "" });

  // Handle inputs para campos simples
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setCurso(prev => ({
      ...prev,
      [name]: name === "preco" ? parseFloat(value) : value,
    } as any));
  };

  // Adicionar módulo
  const adicionarModulo = () => {
    setCurso(prev => ({
      ...prev,
      modulos: [...prev.modulos, { titulo: "", aulas: [] }],
    }));
  };

  // Remover módulo
  const removerModulo = (index: number) => {
    const modulos = [...curso.modulos];
    modulos.splice(index, 1);
    setCurso(prev => ({ ...prev, modulos }));
  };

  // Atualizar título de módulo
  const handleModuloTitulo = (index: number, titulo: string) => {
    const modulos = [...curso.modulos];
    modulos[index].titulo = titulo;
    setCurso(prev => ({ ...prev, modulos }));
  };

  // Adicionar aula a um módulo
  const adicionarAula = (modIndex: number) => {
    const modulos = [...curso.modulos];
    modulos[modIndex].aulas.push({ titulo: "", descricao: "", duracaoEmMinutos: undefined });
    setCurso(prev => ({ ...prev, modulos }));
  };

  // Remover aula de um módulo
  const removerAula = (modIndex: number, aulaIndex: number) => {
    const modulos = [...curso.modulos];
    modulos[modIndex].aulas.splice(aulaIndex, 1);
    setCurso(prev => ({ ...prev, modulos }));
  };

  // Atualizar campos de aula
  const handleAulaChange = (
    modIndex: number,
    aulaIndex: number,
    field: keyof AulaDTO,
    value: string | number
  ) => {
    const modulos = [...curso.modulos];
    const aula = modulos[modIndex].aulas[aulaIndex];
    // @ts-ignore
    aula[field] = field === "duracaoEmMinutos" ? Number(value) : value;
    setCurso(prev => ({ ...prev, modulos }));
  };

  // Selecionar arquivo de vídeo para aula
  const handleVideoChange = (modIndex: number, aulaIndex: number, e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files?.[0]) {
      const file = e.target.files[0];
      const key = `${modIndex}-${aulaIndex}`;
      setFilesMap(prev => ({ ...prev, [key]: file }));
    }
  };

  // Calcular totais antes de enviar
  const calcularTotais = () => {
    const quantidadeModulos = curso.modulos.length;
    const duracaoTotal = curso.modulos.reduce(
      (sumMod, mod) => sumMod + mod.aulas.reduce((sumAul, aul) => sumAul + (aul.duracaoEmMinutos ?? 0), 0),
      0
    );
    setCurso(prev => ({ ...prev, quantidadeModulos, duracaoTotal }));
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    calcularTotais();
    setStatus({ carregando: true, sucesso: null, mensagem: "" });

    try {
      // Enviar vídeos de cada aula para S3
      const modulosComVideoKey = await Promise.all(
        curso.modulos.map(async (mod, mi) => {
          const aulas = await Promise.all(
            mod.aulas.map(async (aul, ai) => {
              const keyFile = `${mi}-${ai}`;
              if (filesMap[keyFile]) {
                aul.videoKey = await enviarVideoParaS3(filesMap[keyFile]);
              }
              return aul;
            })
          );
          return { ...mod, aulas };
        })
      );

      const payload: CursoDTO = {
        ...curso,
        modulos: modulosComVideoKey,
      };

      const response = await axios.post("http://localhost:8082/curso", payload, {
        headers: { "Content-Type": "application/json" },
      });

      if (response.status === 200) {
        setStatus({ carregando: false, sucesso: true, mensagem: "Curso cadastrado com sucesso!" });
        // Resetar formulário
        setCurso({ nome: "", preco: 0, descricao: "", professor: "", categoria: "", modulos: [], quantidadeModulos: 0, duracaoTotal: 0 });
        setFilesMap({});
      } else {
        setStatus({ carregando: false, sucesso: false, mensagem: `Falha ao cadastrar: ${response.statusText}` });
      }
    } catch (err) {
      setStatus({ carregando: false, sucesso: false, mensagem: "Erro de rede ou no servidor." });
      console.error(err);
    }
  };

  async function enviarVideoParaS3(videoFile: File): Promise<string> {
    const formData = new FormData();
    formData.append("file", videoFile);
    const response = await axios.post<string>("http://localhost:8082/s3/upload/videosCursos", formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });
    return response.data;
  }


  return (
    <div className="max-w-3xl mt-10 mx-auto p-6 bg-white rounded-lg shadow-lg">
      <h2 className="text-2xl font-bold text-center mb-6">Cadastrar Novo Curso</h2>
      <form onSubmit={handleSubmit} className="space-y-6">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="flex flex-col">
            <label htmlFor="nome" className="mb-1 font-medium">Nome do Curso</label>
            <input id="nome" name="nome" type="text" value={curso.nome} onChange={handleChange} required placeholder="Digite o nome do curso" className="border rounded p-2 focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </div>
          <div className="flex flex-col">
            <label htmlFor="preco" className="mb-1 font-medium">Preço (R$)</label>
            <input id="preco" name="preco" type="number" value={curso.preco} onChange={handleChange} required step="0.01" min="0" placeholder="Ex: 150.00" className="border rounded p-2 focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </div>
        </div>
        <div className="flex flex-col">
          <label htmlFor="descricao" className="mb-1 font-medium">Descrição</label>
          <textarea id="descricao" name="descricao" rows={4} value={curso.descricao} onChange={handleChange} required placeholder="Descreva brevemente o curso" className="border rounded p-2 focus:outline-none focus:ring-2 focus:ring-blue-500 resize-vertical" />
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="flex flex-col">
            <label htmlFor="professor" className="mb-1 font-medium">Professor</label>
            <input id="professor" name="professor" type="text" value={curso.professor} onChange={handleChange} required placeholder="Nome do professor" className="border rounded p-2 focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </div>
          <div className="flex flex-col">
            <label htmlFor="categoria" className="mb-1 font-medium">Categoria</label>
            <input id="categoria" name="categoria" type="text" value={curso.categoria} onChange={handleChange} required placeholder="Ex: Programação, Marketing..." className="border rounded p-2 focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </div>
        </div>

        <div className="space-y-4">
          <h3 className="text-xl font-semibold">Módulos</h3>
          <button type="button" onClick={adicionarModulo} className="bg-blue-500 hover:bg-blue-600 text-white font-semibold py-2 px-4 rounded">Adicionar Módulo</button>
          {curso.modulos.map((mod, mi) => (
            <div key={mi} className="border border-gray-300 rounded-lg p-4">
              <div className="flex items-center justify-between mb-3">
                <input type="text" placeholder="Título do Módulo" value={mod.titulo} onChange={e => handleModuloTitulo(mi, e.target.value)} required className="flex-1 border rounded p-2 focus:outline-none focus:ring-2 focus:ring-blue-500 mr-2" />
                <button type="button" onClick={() => removerModulo(mi)} className="bg-red-500 hover:bg-red-600 text-white font-semibold py-1 px-3 rounded">Remover</button>
              </div>
              <h4 className="font-medium mb-2">Aulas</h4>
              <button type="button" onClick={() => adicionarAula(mi)} className="mb-4 bg-green-500 hover:bg-green-600 text-white font-semibold py-2 px-4 rounded">Adicionar Aula</button>
              {mod.aulas.map((aul, ai) => (
                <div key={ai} className="border border-dashed border-gray-400 rounded p-3 mb-3 space-y-2">
                  <input type="text" placeholder="Título da Aula" value={aul.titulo} onChange={e => handleAulaChange(mi, ai, 'titulo', e.target.value)} required className="w-full border rounded p-2 focus:outline-none focus:ring-2 focus:ring-blue-500" />
                  <textarea placeholder="Descrição da Aula" value={aul.descricao} onChange={e => handleAulaChange(mi, ai, 'descricao', e.target.value)} required rows={2} className="w-full border rounded p-2 focus:outline-none focus:ring-2 focus:ring-blue-500 resize-vertical" />
                  <input type="number" placeholder="Duração (min)" value={aul.duracaoEmMinutos ?? ""} onChange={e => handleAulaChange(mi, ai, 'duracaoEmMinutos', e.target.value)} required className="w-full border rounded p-2 focus:outline-none focus:ring-2 focus:ring-blue-500" />
                  <input type="file" accept="video/*" onChange={e => handleVideoChange(mi, ai, e)} required className="w-full text-sm shadow-lg" />
                  <button type="button" onClick={() => removerAula(mi, ai)} className="mt-2 bg-red-500 hover:bg-red-600 text-white font-semibold py-1 px-3 rounded">Remover Aula</button>
                </div>
              ))}
            </div>
          ))}
        </div>

        <button type="submit" disabled={status.carregando} className={`w-full py-3 font-semibold rounded ${status.carregando ? 'bg-green-300 cursor-not-allowed' : 'bg-green-500 hover:bg-green-600'} text-white`}>{status.carregando ? 'Cadastrando...' : 'Cadastrar Curso'}</button>
      </form>

      {status.sucesso !== null && (
        <div className={`mt-4 font-bold ${status.sucesso ? 'text-green-600' : 'text-red-600'}`}>${status.mensagem}</div>
      )}
    </div>
  );
}


