package derich.com.br.Usuario.DTO;

public record UsuarioResponseDTO(
        String nome,
        String documento,
        String dataNascimento,
        String email
)
{ }
