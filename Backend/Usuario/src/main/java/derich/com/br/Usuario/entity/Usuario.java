package derich.com.br.Usuario.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "usuario")
@Getter
@Setter
@AllArgsConstructor
public class Usuario {

    private String id;

    private String nome;

    private String documento;

    private String dataNascimento;

    private String email;

    private String senha;

    private String tipo; // ex: "aluno" ou "instrutor"

    private List<String> cursosMatriculados;

    // caso seja instrutor
    private List<String> cursosCriados;
}
