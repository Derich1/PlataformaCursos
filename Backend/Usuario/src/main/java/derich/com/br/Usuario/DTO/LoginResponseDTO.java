package derich.com.br.Usuario.DTO;

public record LoginResponseDTO(
        String nome,
        String documento,
        String dataNascimento,
        String email,
        String senha
) { }
