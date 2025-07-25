package derich.com.br.Curso.service;

import derich.com.br.Curso.DTO.AdicionarCursoResponseDTO;
import derich.com.br.Curso.DTO.EmailDTO;
import derich.com.br.Curso.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class CursoServiceProducer {

    private final RabbitTemplate rabbitTemplate;

    public CursoServiceProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEmailToQueue(EmailDTO emailDTO) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EMAIL_EXCHANGE,
                RabbitConfig.EMAIL_ROUTING,
                emailDTO
        );
    }

    public void sendResponseToQueue(AdicionarCursoResponseDTO response) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.VALIDACAO_CURSO_EXCHANGE,
                RabbitConfig.VALIDACAO_CURSO_ROUTING_KEY,
                response
        );
    }
}
