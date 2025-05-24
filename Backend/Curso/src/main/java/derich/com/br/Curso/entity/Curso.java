package derich.com.br.Curso.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "curso")
@Getter
@Setter
@AllArgsConstructor
public class Curso {

    private String id;

    private String nome;

    private BigDecimal preco;

    private String descricao;

    private String professor;

    private String categoria;
}
