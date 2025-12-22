package derich.com.br.Curso.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;
import com.stripe.model.StripeError;
import com.stripe.param.PaymentIntentCreateParams;
import derich.com.br.Curso.DTO.*;
import com.stripe.model.PaymentIntent;
import derich.com.br.Curso.service.CursoServiceProducer;
import derich.com.br.Curso.entity.Curso;
import derich.com.br.Curso.service.CursoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/curso")
public class CursoController {

    private CursoService cursoService;

    private final CursoServiceProducer cursoServiceProducer;

    private static final Logger logger = LoggerFactory.getLogger(CursoController.class);

    public CursoController(CursoServiceProducer cursoServiceProducer, CursoService cursoService) {
        this.cursoServiceProducer = cursoServiceProducer;
        this.cursoService = cursoService;
    }

    @GetMapping
    public List<Curso> listarCursos () {
        return cursoService.listarCursos();
    }

    @PostMapping("/criarPagamento")
    public ResponseEntity<?> criarPagamento(@RequestBody PaymentRequest paymentRequest) {
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(paymentRequest.amount())
                    .setCurrency(paymentRequest.currency())
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);
            logger.info(intent.getClientSecret());
            cursoServiceProducer.sendEmailToQueue(new EmailDTO(paymentRequest.email(), "Iniciando pagamento", "Parabéns pela compra do seu curso! Estamos verificando o pagamento"));
            return ResponseEntity.ok().body(
                    java.util.Map.of("clientSecret", intent.getClientSecret())
            );
        } catch (StripeException e) {
            logger.error("Erro ao criar o pagamento: {}", e.getMessage(), e);

            StripeError stripeError = e.getStripeError();

            if (stripeError != null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        Map.of(
                                "message: ", stripeError.getMessage(),
                                "code: ", stripeError.getCode(),
                                "type: ", stripeError.getType()
                        )
                );
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> buscarPorId(@PathVariable String id) {
        Curso curso = cursoService.buscarPorId(id);
        return ResponseEntity.ok(curso);
    }

    @PostMapping("/buscarCursosPorIdUsuario")
    public ResponseEntity<List<CursoResponseDTO>> buscarCursosPorIdUsuario(@RequestBody List<String> ids) {
        List<CursoResponseDTO> curso = cursoService.buscarCursosPorIds(ids);
        return ResponseEntity.ok(curso);
    }

    @PostMapping
    private Curso cadastrarCurso (@RequestBody CursoDTO cursoDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        logger.info(objectMapper.writeValueAsString(cursoDTO));
        return cursoService.cadastrarCurso(cursoDTO);
    }

    @PutMapping
    private Curso editarCurso (CursoEditDTO cursoEditDTO) {
        return cursoService.editarCurso(cursoEditDTO);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<String> deletarCurso (@PathVariable String id) {
        boolean isDeletado = cursoService.deletarCurso(id);
        if (isDeletado) {
            return ResponseEntity.ok("Deletado com sucesso");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Curso não encontrado");
    }
}
