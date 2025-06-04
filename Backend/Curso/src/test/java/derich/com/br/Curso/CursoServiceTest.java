package derich.com.br.Curso;

import derich.com.br.Curso.DTO.CursoDTO;
import derich.com.br.Curso.DTO.CursoEditDTO;
import derich.com.br.Curso.entity.Curso;
import derich.com.br.Curso.repository.ICursoRepository;
import derich.com.br.Curso.service.CursoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.MockitoAnnotations;

class CursoServiceTest {

	@Mock
	private ICursoRepository cursoRepository;

	@InjectMocks
	private CursoService cursoService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testListarCursos() {
		// Criamos dois cursos com listas de vídeo-keys (uma pode ser null ou vazia)
		Curso curso1 = new Curso(
				"1",
				"Curso A",
				BigDecimal.valueOf(100.00),
				"Descrição A",
				"Professor A",
				"Categoria A",
				Arrays.asList("videos/cursoA1.mp4", "videos/cursoA2.mp4")
		);
		Curso curso2 = new Curso(
				"2",
				"Curso B",
				BigDecimal.valueOf(200.00),
				"Descrição B",
				"Professor B",
				"Categoria B",
				List.of("videos/cursoB1.mp4")
		);

		when(cursoRepository.findAll()).thenReturn(Arrays.asList(curso1, curso2));

		List<Curso> cursos = cursoService.listarCursos();

		assertEquals(2, cursos.size());
		assertEquals("Curso A", cursos.get(0).getNome());
		assertEquals(2, cursos.get(0).getVideoKey().size());
		assertEquals("videos/cursoB1.mp4", cursos.get(1).getVideoKey().get(0));
		verify(cursoRepository, times(1)).findAll();
	}

	@Test
	void testCadastrarCurso() {
		// Agora o DTO recebe uma lista de vídeo-keys
		CursoDTO cursoDTO = new CursoDTO(
				"Curso Novo",
				BigDecimal.valueOf(150.00),
				"Descrição nova",
				"Professor X",
				"Categoria X",
				Arrays.asList("videos/novo1.mp4", "videos/novo2.mp4")
		);

		// O construtor de Curso(CursoDTO) deve criar um Curso com essa lista
		Curso cursoSalvo = new Curso(
				"some-id",
				cursoDTO.nome(),
				cursoDTO.preco(),
				cursoDTO.descricao(),
				cursoDTO.professor(),
				cursoDTO.categoria(),
				cursoDTO.videoKey()
		);

		when(cursoRepository.save(any(Curso.class))).thenReturn(cursoSalvo);

		Curso resultado = cursoService.cadastrarCurso(cursoDTO);

		assertEquals("Curso Novo", resultado.getNome());
		assertEquals(BigDecimal.valueOf(150.00), resultado.getPreco());
		assertEquals(2, resultado.getVideoKey().size());
		assertTrue(resultado.getVideoKey().contains("videos/novo2.mp4"));
		verify(cursoRepository, times(1)).save(any(Curso.class));
	}

	@Test
	void testEditarCurso() {
		// Curso existente já com uma lista de vídeo-keys prévia
		Curso cursoExistente = new Curso(
				"1",
				"Curso Antigo",
				BigDecimal.valueOf(100.00),
				"Desc Antiga",
				"Prof A",
				"Cat A",
				Arrays.asList("videos/antigo.mp4")
		);
		// DTO de edição agora inclui nova lista (substituindo a anterior)
		CursoEditDTO dto = new CursoEditDTO(
				"1",
				"Curso Atualizado",
				BigDecimal.valueOf(150.00),
				"Nova descrição",
				"Prof B",
				"Cat B",
				Arrays.asList("videos/atualizado1.mp4", "videos/atualizado2.mp4")
		);

		when(cursoRepository.findById("1")).thenReturn(Optional.of(cursoExistente));
		when(cursoRepository.save(any(Curso.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Curso atualizado = cursoService.editarCurso(dto);

		assertEquals("Curso Atualizado", atualizado.getNome());
		assertEquals(BigDecimal.valueOf(150.00), atualizado.getPreco());
		assertEquals(2, atualizado.getVideoKey().size());
		assertTrue(atualizado.getVideoKey().contains("videos/atualizado2.mp4"));
		verify(cursoRepository).save(cursoExistente);
	}

	@Test
	void testEditarCursoNaoEncontrado() {
		CursoEditDTO dto = new CursoEditDTO(
				"999",
				"Qualquer",
				BigDecimal.valueOf(0.0),
				"",
				"",
				"",
				List.of() // lista vazia de vídeo-keys
		);

		when(cursoRepository.findById("999")).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> cursoService.editarCurso(dto));
	}

	@Test
	void testDeletarCursoComSucesso() {
		Curso curso = new Curso(
				"1",
				"Curso",
				BigDecimal.valueOf(100.00),
				"desc",
				"prof",
				"cat",
				Arrays.asList("videos/curso1.mp4", "videos/curso2.mp4")
		);

		when(cursoRepository.findById("1")).thenReturn(Optional.of(curso));

		boolean resultado = cursoService.deletarCurso("1");

		assertTrue(resultado);
		verify(cursoRepository, times(1)).delete(curso);
		// Se o delete gera também exclusão no S3, podemos mockar o S3Service e verificar aqui
	}

	@Test
	void testDeletarCursoNaoEncontrado() {
		when(cursoRepository.findById("999")).thenReturn(Optional.empty());

		boolean resultado = cursoService.deletarCurso("999");

		assertFalse(resultado);
		verify(cursoRepository, never()).delete(any());
	}
}
