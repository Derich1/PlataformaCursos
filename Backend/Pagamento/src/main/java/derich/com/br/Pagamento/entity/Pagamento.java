package derich.com.br.Pagamento.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "pagamento")
@Getter
@Setter
@AllArgsConstructor
public class Pagamento {

    private String id;

    private String status;

    private String idCurso;

    private String idComprador;

    private String metodoPagamento;

    private BigDecimal valor;

    private Integer parcelas;
}
