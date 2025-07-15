package derich.com.br.Usuario.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record UsuarioResponseDTO(
        String nome,
        String documento,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate dataNascimento,
        String email
)
{ }
