package derich.com.br.Usuario.DTO;

import java.time.LocalDate;

public record LoginResponseDTO(
        String nome,
        String documento,
        LocalDate dataNascimento,
        String email,
        String senha
) { }
