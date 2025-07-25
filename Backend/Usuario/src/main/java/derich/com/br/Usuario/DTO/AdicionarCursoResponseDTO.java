package derich.com.br.Usuario.DTO;

public record AdicionarCursoResponseDTO(
        String email,
        String nomeCurso,
        Boolean existeCurso
) {
}
