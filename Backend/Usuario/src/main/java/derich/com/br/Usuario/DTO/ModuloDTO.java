package derich.com.br.Usuario.DTO;

import java.util.List;

public record ModuloDTO(
        String titulo,
        String descricao,
        List<AulaDTO> aulas
) { }

