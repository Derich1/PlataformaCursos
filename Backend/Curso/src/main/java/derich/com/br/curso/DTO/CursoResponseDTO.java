package derich.com.br.curso.DTO;

import derich.com.br.curso.entity.Modulo;

import java.util.List;

public record CursoResponseDTO(
        String nome,
        String descricao,
        String professor,
        String categoria,
        List<Modulo> modulos
) {
}
