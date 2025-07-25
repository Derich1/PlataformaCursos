package derich.com.br.Curso.DTO;

public record AdicionarCursoResponseDTO(
        String email,
        String nomeCurso,
        Boolean existeCurso
) {
}
