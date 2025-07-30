package derich.com.br.Curso.DTO;

import derich.com.br.Curso.entity.Modulo;

import java.util.List;

public record CursoResponseDTO(
        String nome,
        String descricao,
        String professor,
        String categoria,
        List<Modulo> modulos
) {
}
