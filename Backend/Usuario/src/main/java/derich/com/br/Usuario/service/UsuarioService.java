package derich.com.br.Usuario.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import derich.com.br.Usuario.DTO.LoginRequestDTO;
import derich.com.br.Usuario.DTO.LoginResponseDTO;
import derich.com.br.Usuario.DTO.UsuarioDTO;
import derich.com.br.Usuario.DTO.UsuarioResponseDTO;
import derich.com.br.Usuario.entity.Usuario;
import derich.com.br.Usuario.repository.IUsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    public LoginResponseDTO cadastrarUsuario (UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario(usuarioDTO);
        String token = jwtService.generateToken(usuario);
        usuarioRepository.save(usuario);
        return new LoginResponseDTO(
                token,
                usuario.getNome(),
                usuario.getDocumento(),
                usuario.getDataNascimento(),
                usuario.getEmail()
        );
    }

    public LoginResponseDTO login (LoginRequestDTO loginRequestDTO) throws JsonProcessingException {
        Usuario usuario = usuarioRepository
                .findByEmail(loginRequestDTO.email())
                .orElseThrow(() -> new RuntimeException("Nenhum usuário encontrado com este email."));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        logger.info(objectMapper.writeValueAsString(usuario));

        String token = jwtService.generateToken(usuario);

        if (usuario.getSenha().equals(loginRequestDTO.senha())){
            return new LoginResponseDTO(
                    token,
                    usuario.getNome(),
                    usuario.getDocumento(),
                    usuario.getDataNascimento(),
                    usuario.getEmail()
            );
        }
        throw new RuntimeException("Erro ao fazer login");
    }

    public UsuarioResponseDTO buscarPerfil(@RequestBody String id){
        Usuario usuario = usuarioRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Não foi possível encontrar o usuário"));
        return new UsuarioResponseDTO(
                usuario.getNome(),
                usuario.getDocumento(),
                usuario.getDataNascimento(),
                usuario.getEmail()
        );
    }
}
