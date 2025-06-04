// src/pages/CadastrarCurso.tsx
import axios from "axios";
import React, { useState, type FormEvent } from "react";

interface CursoDTO {
  nome: string;
  preco: number;
  descricao: string;
  professor: string;
  categoria: string;
  videoKey?: string[]; 
}

export const CadastroCurso: React.FC = () => {
  const [curso, setCurso] = useState<CursoDTO>({
    nome: "",
    preco: 0,
    descricao: "",
    professor: "",
    categoria: "",
  });

  const [file, setFile] = useState<File | null>(null);

  const [status, setStatus] = useState<{
    carregando: boolean;
    sucesso: boolean | null;
    mensagem: string;
  }>({ carregando: false, sucesso: null, mensagem: "" });

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setCurso((prev) => ({
      ...prev,
      // Se for o campo “preco”, convertemos para número flutuante
      [name]: name === "preco" ? parseFloat(value) : value,
    }));
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      setFile(e.target.files[0]);
    }
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    if (!file) {
      setStatus({
        carregando: false,
        sucesso: false,
        mensagem: "Selecione um arquivo de vídeo antes de cadastrar.",
      });
      return;
    }
    setStatus({ carregando: true, sucesso: null, mensagem: "" });

    try {
      const keyDoVideo = await enviarVideoParaS3(file);
      const payload: CursoDTO = {
        nome: curso.nome,
        preco: curso.preco,
        descricao: curso.descricao,
        professor: curso.professor,
        categoria: curso.categoria,
        videoKey: [keyDoVideo]
      };

      const response = await axios.post("http://localhost:8082/curso", 
        payload,
        {
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (response.status === 200) {
        setStatus({
          carregando: false,
          sucesso: true,
          mensagem: "Curso cadastrado com sucesso!",
        });
        // Limpa o formulário (opcional)
        setCurso({
          nome: "",
          preco: 0,
          descricao: "",
          professor: "",
          categoria: "",
        });
      } else {
        const erroText = await response.data();
        setStatus({
          carregando: false,
          sucesso: false,
          mensagem: `Falha ao cadastrar: ${erroText}`,
        });
      }
    } catch (err) {
      setStatus({
        carregando: false,
        sucesso: false,
        mensagem: "Erro de rede ou no servidor.",
      });
      console.error(err);
    }
  };

  async function enviarVideoParaS3(videoFile: File): Promise<string> {
    const formData = new FormData();
    formData.append("file", videoFile);

    // /videosCursos é o nome do folder escolhido
    const response = await axios.post<string>(
      "http://localhost:8082/s3/upload/videosCursos",
      formData,
      {
        headers: { "Content-Type": "multipart/form-data" },
      }
    );
    return response.data; // Ex.: "videosCursos/162738271238-meu-video.mp4"
  }


  return (
    <div style={styles.container}>
      <h2 style={styles.titulo}>Cadastrar Novo Curso</h2>

      <form onSubmit={handleSubmit} style={styles.form}>
        {/* Nome */}
        <div style={styles.formGroup}>
          <label htmlFor="nome" style={styles.label}>
            Nome do Curso
          </label>
          <input
            type="text"
            id="nome"
            name="nome"
            value={curso.nome}
            onChange={handleChange}
            required
            style={styles.input}
            placeholder="Digite o nome do curso"
          />
        </div>

        {/* Preço */}
        <div style={styles.formGroup}>
          <label htmlFor="preco" style={styles.label}>
            Preço (R$)
          </label>
          <input
            type="number"
            id="preco"
            name="preco"
            value={curso.preco}
            onChange={handleChange}
            required
            step="0.01"
            min="0"
            style={styles.input}
            placeholder="Ex: 150.00"
          />
        </div>

        {/* Descrição */}
        <div style={styles.formGroup}>
          <label htmlFor="descricao" style={styles.label}>
            Descrição
          </label>
          <textarea
            id="descricao"
            name="descricao"
            value={curso.descricao}
            onChange={handleChange}
            required
            rows={4}
            style={styles.textarea}
            placeholder="Descreva brevemente o curso"
          />
        </div>

        {/* Professor */}
        <div style={styles.formGroup}>
          <label htmlFor="professor" style={styles.label}>
            Professor
          </label>
          <input
            type="text"
            id="professor"
            name="professor"
            value={curso.professor}
            onChange={handleChange}
            required
            style={styles.input}
            placeholder="Nome do professor"
          />
        </div>

        {/* Categoria */}
        <div style={styles.formGroup}>
          <label htmlFor="categoria" style={styles.label}>
            Categoria
          </label>
          <input
            type="text"
            id="categoria"
            name="categoria"
            value={curso.categoria}
            onChange={handleChange}
            required
            style={styles.input}
            placeholder="Ex: Programação, Marketing..."
          />
        </div>

        <div style={styles.formGroup}>
          <label htmlFor="video" style={styles.label}>
            Vídeo do Curso
          </label>
          <input
            type="file"
            id="video"
            name="video"
            accept="video/*"
            onChange={handleFileChange}
            required
            style={styles.input}
          />
        </div>

        {/* Botão de Enviar */}
        <button
          type="submit"
          disabled={status.carregando}
          style={{
            ...styles.botao,
            ...(status.carregando ? styles.botaoDesabilitado : {}),
          }}
        >
          {status.carregando ? "Cadastrando..." : "Cadastrar Curso"}
        </button>
      </form>

      {/* Feedback ao usuário */}
      {status.sucesso !== null && (
        <div
          style={{
            marginTop: "1rem",
            color: status.sucesso ? "green" : "red",
            fontWeight: "bold",
          }}
        >
          {status.mensagem}
        </div>
      )}
    </div>
  );
};

const styles: { [key: string]: React.CSSProperties } = {
  container: {
    maxWidth: "480px",
    margin: "2rem auto",
    padding: "1rem 2rem",
    border: "1px solid #ddd",
    borderRadius: "8px",
    boxShadow: "0 2px 8px rgba(0,0,0,0.1)",
    fontFamily: "sans-serif",
  },
  titulo: {
    textAlign: "center",
    marginBottom: "1.5rem",
  },
  form: {
    display: "flex",
    flexDirection: "column",
  },
  formGroup: {
    marginBottom: "1rem",
  },
  label: {
    display: "block",
    marginBottom: "0.25rem",
    fontWeight: 500,
  },
  input: {
    width: "100%",
    padding: "0.5rem",
    borderRadius: "4px",
    border: "1px solid #ccc",
    fontSize: "1rem",
  },
  textarea: {
    width: "100%",
    padding: "0.5rem",
    borderRadius: "4px",
    border: "1px solid #ccc",
    fontSize: "1rem",
    resize: "vertical",
  },
  botao: {
    padding: "0.75rem",
    backgroundColor: "#4CAF50",
    color: "#fff",
    fontSize: "1rem",
    border: "none",
    borderRadius: "4px",
    cursor: "pointer",
  },
  botaoDesabilitado: {
    backgroundColor: "#a5d6a7",
    cursor: "not-allowed",
  },
};
