package derich.com.br.curso.DTO;

import java.io.Serializable;

public record EmailDTO(
        String to,
        String subject,
        String body
) implements Serializable
{ }
