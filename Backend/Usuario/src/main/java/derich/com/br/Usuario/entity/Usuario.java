package derich.com.br.Usuario.entity;

import derich.com.br.Usuario.DTO.UsuarioDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    private String id;

    private String nome;

    private String documento;

    private LocalDate dataNascimento;

    private String email;

    private String senha;

    private String tipo; // ex: "aluno" ou "instrutor"

    private List<String> cursosMatriculados;

    // caso seja instrutor
    private List<String> cursosCriados;

    public Usuario (UsuarioDTO usuarioDTO) {
        this.nome = usuarioDTO.nome();
        this.documento = usuarioDTO.documento();
        this.dataNascimento = usuarioDTO.dataNascimento();
        this.email = usuarioDTO.email();
        this.senha = usuarioDTO.senha();
    }
}
