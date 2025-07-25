package derich.com.br.Usuario.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import derich.com.br.Usuario.DTO.*;
import derich.com.br.Usuario.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @PostMapping("/cadastrar")
    private LoginResponseDTO cadastrarUsuario (@RequestBody UsuarioDTO usuarioDTO) {
        LoginResponseDTO loginResponseDTO = usuarioService.cadastrarUsuario(usuarioDTO);
        return loginResponseDTO;
    }

    @PostMapping("/adicionarCurso")
    public ResponseEntity<?> adicionarCurso(@RequestBody AdicionarCursoRequestDTO dto) {
        try {
            usuarioService.adicionarCurso(dto);
            return ResponseEntity.ok("Curso adicionado a lista de cursos matriculados com sucesso");
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não foi possível adicionar o curso. " + e.getMessage());
        }
    }

    @PostMapping("/login")
    private ResponseEntity<?> login (@RequestBody LoginRequestDTO loginDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        logger.info(objectMapper.writeValueAsString(loginDTO));
        try {
            LoginResponseDTO response = usuarioService.login(loginDTO);
            return ResponseEntity.ok(response);
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/perfil/{id}")
    private UsuarioResponseDTO buscarPerfil(@PathVariable String id){
        return usuarioService.buscarPerfil(id);
    }
}
