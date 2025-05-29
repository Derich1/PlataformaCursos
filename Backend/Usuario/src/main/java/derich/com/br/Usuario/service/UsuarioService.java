package derich.com.br.Usuario.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import derich.com.br.Usuario.DTO.LoginRequestDTO;
import derich.com.br.Usuario.DTO.LoginResponseDTO;
import derich.com.br.Usuario.DTO.UsuarioDTO;
import derich.com.br.Usuario.entity.Usuario;
import derich.com.br.Usuario.repository.IUsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    public Usuario cadastrarUsuario (UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario(usuarioDTO);
        return usuarioRepository.save(usuario);
    }

    public LoginResponseDTO login (LoginRequestDTO loginRequestDTO) throws JsonProcessingException {
        Usuario usuario = usuarioRepository
                .findByEmail(loginRequestDTO.email())
                .orElseThrow(() -> new RuntimeException("Nenhum usuário encontrado com este email."));

        ObjectMapper objectMapper = new ObjectMapper();
        logger.info(objectMapper.writeValueAsString(usuario));

        if (usuario.getSenha().equals(loginRequestDTO.senha())){
            return new LoginResponseDTO(
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getDataNascimento(),
                    usuario.getDocumento(),
                    usuario.getSenha()
            );
        }
        throw new RuntimeException("Erro ao fazer login");
    }
}
