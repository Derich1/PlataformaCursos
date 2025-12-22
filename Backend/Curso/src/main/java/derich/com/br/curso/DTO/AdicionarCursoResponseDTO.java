package derich.com.br.curso.DTO;

public record AdicionarCursoResponseDTO(
        String email,
        String nomeCurso,
        Boolean existeCurso
) {
}
