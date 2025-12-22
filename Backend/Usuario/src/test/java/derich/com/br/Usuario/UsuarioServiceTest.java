package derich.com.br.Usuario;

import com.fasterxml.jackson.core.JsonProcessingException;
import derich.com.br.Usuario.DTO.LoginRequestDTO;
import derich.com.br.Usuario.DTO.LoginResponseDTO;
import derich.com.br.Usuario.DTO.UsuarioDTO;
import derich.com.br.Usuario.entity.Usuario;
import derich.com.br.Usuario.repository.IUsuarioRepository;
import derich.com.br.Usuario.service.JwtService;
import derich.com.br.Usuario.service.UsuarioService;
import derich.com.br.Usuario.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

	//	Cria um mock (objeto falso/simulado) de uma dependência.
	//	Não tem comportamento real; você programa como ele deve se comportar com when(...).thenReturn(...).
	@Mock
	private IUsuarioRepository usuarioRepository;

	//	Cria uma instância real da classe que você está testando
	//	e injeta automaticamente os mocks (feitos com @Mock) nas dependências da classe.
	@InjectMocks
	private UsuarioService usuarioService;

	@Mock
	private JwtService jwtService;

	@Mock
	private Util util;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCadastrarUsuario() {
		UsuarioDTO dto = new UsuarioDTO(
				"João", "99999999999", LocalDate.of(1990, 1, 1), "joao@email.com", "senha123"
		);
		Usuario usuario = new Usuario(dto);

		when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

		LoginResponseDTO resultado = usuarioService.cadastrarUsuario(dto);

		assertEquals("João", resultado.nome());
		verify(usuarioRepository, times(1)).save(any(Usuario.class));
	}

	@Test
	void testLoginComSucesso() throws JsonProcessingException {
		String email = "joao@email.com";
		String senha = "senha123";

		Usuario usuario = new Usuario();
		usuario.setEmail(email);
		usuario.setSenha(senha);
		usuario.setNome("João");
		usuario.setDocumento("12345678");
		usuario.setDataNascimento(LocalDate.of(1990, 1, 1));

		// No seu código real, usuarioRepository.findByEmail(...) retorna: Optional<Usuario>
		// Ou seja, um Optional com um Usuario dentro — ou vazio (Optional.empty()), caso não exista.
		// "Quando chamarem findByEmail(email), retorne um Optional que contém este usuario."
		when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

		LoginRequestDTO loginRequest = new LoginRequestDTO(email, senha);
		var response = usuarioService.login(loginRequest);

		assertEquals(email, response.email());
		assertEquals("João", response.nome());
	}

	@Test
	void testLoginComEmailInvalido() {
		String email = "naoexiste@email.com";
		when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

		LoginRequestDTO loginRequest = new LoginRequestDTO(email, "qualquerSenha");

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			usuarioService.login(loginRequest);
		});

		assertEquals("Nenhum usuário encontrado com este email.", exception.getMessage());
	}

	@Test
	void testLoginComSenhaIncorreta() {
		Usuario usuario = new Usuario();
		usuario.setEmail("joao@email.com");
		usuario.setSenha("senhaCorreta");

		when(usuarioRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(usuario));

		LoginRequestDTO loginRequest = new LoginRequestDTO("joao@email.com", "senhaErrada");

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			usuarioService.login(loginRequest);
		});

		assertEquals("Erro ao fazer login", exception.getMessage());
	}
}
