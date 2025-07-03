import type { ModuloDTO } from "./modulo";

export interface CursoDTO {
  nome: string;
  preco: number;
  descricao: string;
  professor: string;
  categoria: string;
  modulos: ModuloDTO[];
  quantidadeModulos: number;
  duracaoTotalSegundos: number;
}