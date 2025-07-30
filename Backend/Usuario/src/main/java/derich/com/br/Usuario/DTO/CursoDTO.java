package derich.com.br.Usuario.DTO;

import java.math.BigDecimal;
import java.util.List;

public record CursoDTO(
        String nome,
        BigDecimal preco,
        String descricao,
        String professor,
        String categoria,
        List<Modulo> modulos
)
{ }
