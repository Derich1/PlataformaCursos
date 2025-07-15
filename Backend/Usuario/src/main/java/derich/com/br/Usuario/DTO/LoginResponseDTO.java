package derich.com.br.Usuario.DTO;

import java.time.LocalDate;

public record LoginResponseDTO(
        String token,
        String nome,
        String documento,
        LocalDate dataNascimento,
        String email
) { }
