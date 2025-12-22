package derich.com.br.curso.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Aula {

    private String titulo;
    private String descricao;
    private Integer duracaoEmSegundos;

    // armazena a key (path) do vídeo no bucket S3
    private String videoKey;
}
