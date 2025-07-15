import type { Usuario } from "./usuario";

export interface LoginResponseDTO {
  token: string;
  usuario: Usuario;
}