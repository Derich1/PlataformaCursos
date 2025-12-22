package derich.com.br.curso.service;

import derich.com.br.curso.DTO.AdicionarCursoResponseDTO;
import derich.com.br.curso.DTO.EmailDTO;
import derich.com.br.curso.config.RabbitConfig;
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
