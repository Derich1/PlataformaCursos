package derich.com.br.Usuario.DTO;

public record UsuarioDTO(
        String nome,
        String documento,
        String dataNascimento,
        String email,
        String senha
)
{ }
