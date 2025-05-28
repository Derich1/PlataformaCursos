package derich.com.br.Curso.DTO;

import java.math.BigDecimal;

public record CursoEditDTO(
        String id,
        String nome,
        BigDecimal preco,
        String descricao,
        String professor,
        String categoria
)
{ }
