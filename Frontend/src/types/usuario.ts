export interface Usuario {
  id: string;
  nome: string;
  documento: string;
  dataNascimento: string; // no formato dd/MM/yyyy vindo do backend
  email: string;
  tipo: 'aluno' | 'professor';
  cursosMatriculados?: string[];
  cursosCriados?: string[];
}
