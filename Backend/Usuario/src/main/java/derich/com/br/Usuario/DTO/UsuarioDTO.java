package derich.com.br.Usuario.DTO;

import java.time.LocalDate;

public record UsuarioDTO(
        String nome,
        String documento,
        LocalDate dataNascimento,
        String email,
        String senha
)
{ }
