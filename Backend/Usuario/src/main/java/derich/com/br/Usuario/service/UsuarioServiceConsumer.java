package derich.com.br.Usuario.service;

import derich.com.br.Usuario.DTO.AdicionarCursoRequestDTO;
import derich.com.br.Usuario.DTO.AdicionarCursoResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class UsuarioServiceConsumer {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioServiceConsumer.class);

    private UsuarioService usuarioService;

    @RabbitListener(queues = "usuario.validacao.queue")
    public void receberResposta(AdicionarCursoResponseDTO dto) {
        if (dto.existeCurso()) {
            usuarioService.adicionarCurso(new AdicionarCursoRequestDTO(dto.email(), dto.nomeCurso()));
            logger.info("Mensagem recebida e curso existente.");
        } else {
            // tomar decisão
            logger.error("Mensagem recebida e curso inexistente.");
        }
    }
}
