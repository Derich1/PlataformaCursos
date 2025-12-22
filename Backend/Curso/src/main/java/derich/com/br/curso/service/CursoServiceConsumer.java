package derich.com.br.curso.service;


import derich.com.br.curso.DTO.AdicionarCursoRequestDTO;
import derich.com.br.curso.DTO.AdicionarCursoResponseDTO;
import derich.com.br.curso.DTO.EmailDTO;
import derich.com.br.curso.config.RabbitConfig;
import derich.com.br.curso.repository.ICursoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class CursoServiceConsumer {

    private ICursoRepository cursoRepository;

    private CursoServiceProducer cursoServiceProducer;

    private static final Logger logger = LoggerFactory.getLogger(CursoServiceConsumer.class);

    private final JavaMailSender mailSender;

    public CursoServiceConsumer(JavaMailSender mailSender) {
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

    @RabbitListener(queues = RabbitConfig.VALIDACAO_CURSO_QUEUE)
    public void listenAndSendResponse(AdicionarCursoRequestDTO dto) {
        logger.info("Recebido na fila: {}", dto);

        boolean exist = cursoRepository.findByNome(dto.nomeCurso()).isPresent();
        AdicionarCursoResponseDTO resposta = exist ? new AdicionarCursoResponseDTO(dto.email(), dto.nomeCurso(), true)
                : new AdicionarCursoResponseDTO(dto.email(), dto.nomeCurso(), false);

        cursoServiceProducer.sendResponseToQueue(resposta);

        logger.info("Resposta enviada: {}", resposta);
    }
}

