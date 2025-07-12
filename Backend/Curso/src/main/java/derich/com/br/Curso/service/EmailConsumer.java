package derich.com.br.Curso.service;


import derich.com.br.Curso.DTO.EmailDTO;
import derich.com.br.Curso.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumer {

    private static final Logger logger = LoggerFactory.getLogger(EmailConsumer.class);

    private final JavaMailSender mailSender;

    public EmailConsumer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @RabbitListener(queues = RabbitConfig.EMAIL_QUEUE)
    public void listenAndSendEmail(EmailDTO dto) {
        logger.info("Recebido na fila: {}", dto);
        // Monta mensagem simples
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(dto.to());
        msg.setSubject(dto.subject());
        msg.setText(dto.body());

        // Envia
        mailSender.send(msg);
        logger.info("E‑mail enviado para {}", dto.to());
    }
}

