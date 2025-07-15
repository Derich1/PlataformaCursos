package derich.com.br.Usuario.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record UsuarioDTO(
        String nome,
        String documento,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataNascimento,
        String email,
        String senha
)
{ }
