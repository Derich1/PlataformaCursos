package derich.com.br.Curso.controller;

import derich.com.br.Curso.DTO.EmailDTO;
import derich.com.br.Curso.service.CursoServiceProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notificacao")
public class NotificacaoController {

    private final CursoServiceProducer producer;

    public NotificacaoController(CursoServiceProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/enviar")
    public ResponseEntity<String> enviarNotificacao(@RequestBody EmailDTO dto) {
        producer.sendEmailToQueue(dto);
        return ResponseEntity.ok("E‑mail enfileirado para envio");
    }
}

