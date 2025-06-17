package derich.com.br.Curso.DTO;

import derich.com.br.Curso.entity.Modulo;

import java.math.BigDecimal;
import java.util.List;

public record CursoEditDTO(
        String id,
        String nome,
        BigDecimal preco,
        String descricao,
        String professor,
        String categoria,
        List<Modulo> modulos
)
{ }
