package derich.com.br.Usuario.service;

import derich.com.br.Usuario.DTO.AdicionarCursoRequestDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class UsuarioServiceProducer {

    private final RabbitTemplate rabbitTemplate;

    public UsuarioServiceProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void solicitarValidacao(AdicionarCursoRequestDTO adicionarCursoRequestDTO) {
        rabbitTemplate.convertAndSend(
                "curso.validacao.exchange",
                "curso.validacao.routing",
                adicionarCursoRequestDTO
        );
    }
}
