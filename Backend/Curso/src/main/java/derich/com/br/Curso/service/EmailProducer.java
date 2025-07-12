package derich.com.br.Curso.service;

import derich.com.br.Curso.DTO.EmailDTO;
import derich.com.br.Curso.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailProducer {

    private final RabbitTemplate rabbitTemplate;

    public EmailProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEmailToQueue(EmailDTO emailDTO) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EMAIL_EXCHANGE,
                RabbitConfig.EMAIL_ROUTING,
                emailDTO
        );
    }
}
