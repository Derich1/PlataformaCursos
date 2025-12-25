import axios from "axios";

export const api = axios.create({
  baseURL: "https://plataformacursos.onrender.com",
});
