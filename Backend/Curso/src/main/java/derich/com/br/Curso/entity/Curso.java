package derich.com.br.Curso.entity;

import derich.com.br.Curso.DTO.CursoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Document(collection = "curso")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Curso {

    @Id
    private String id;

    private String nome;

    private BigDecimal preco;

    private String descricao;

    private String professor;

    private String categoria;

    // armazena a key (path) do vídeo no bucket S3
    private List<String> videoKey;

    public Curso(CursoDTO cursoDTO){
        this.id = UUID.randomUUID().toString();
        this.nome = cursoDTO.nome();
        this.preco = cursoDTO.preco();
        this.descricao = cursoDTO.descricao();
        this.professor = cursoDTO.professor();
        this.categoria = cursoDTO.categoria();
        this.videoKey = cursoDTO.videoKey();
    }
}
