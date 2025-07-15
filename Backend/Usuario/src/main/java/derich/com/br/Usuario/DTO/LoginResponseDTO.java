package derich.com.br.Usuario.DTO;

public record LoginResponseDTO(
        String id,
        String token,
        String nome,
        String documento,
        String dataNascimento,
        String email,
        String tipo
) { }
