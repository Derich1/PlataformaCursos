package derich.com.br.Curso;

import derich.com.br.Curso.DTO.CursoDTO;
import derich.com.br.Curso.DTO.CursoEditDTO;
import derich.com.br.Curso.entity.Aula;
import derich.com.br.Curso.entity.Curso;
import derich.com.br.Curso.entity.Modulo;
import derich.com.br.Curso.repository.ICursoRepository;
import derich.com.br.Curso.service.CursoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.*;

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
		Aula aula1 = new Aula();
		aula1.setTitulo("Aula 1.");
		aula1.setDescricao("Descricao 1.");
		aula1.setDuracaoEmSegundos(20);

		Modulo modulo = new Modulo();
		modulo.setTitulo("Modulo A.");
		modulo.setAulas(List.of(aula1));

		Curso curso = new Curso();
		curso.setId("1");
		curso.setNome("Curso A.");
		curso.setQuantidadeModulos(1);
		curso.setDuracaoTotalSegundos(20);
		curso.setModulos(List.of(modulo));

		when(cursoRepository.findAll()).thenReturn(List.of(curso));

		List<Curso> cursos = cursoService.listarCursos();

		assertEquals(1, cursos.size());
		assertEquals(1, cursos.get(0).getQuantidadeModulos());
		assertEquals(20, cursos.get(0).getDuracaoTotalSegundos());
		assertEquals(1, cursos.get(0).getModulos().get(0).getAulas().size());
		assertEquals("Aula 1.", cursos.get(0).getModulos().get(0).getAulas().get(0).getTitulo());

		verify(cursoRepository, times(1)).findAll();
	}

	@Test
	void testCadastrarCurso() {
		Aula aula = new Aula();
		aula.setTitulo("Aula 1.");
		aula.setDescricao("Descricao 1.");
		aula.setDuracaoEmSegundos(30);

		Modulo modulo = new Modulo();
		modulo.setTitulo("Modulo A.");
		modulo.setAulas(List.of(aula));

		CursoDTO cursoDTO = new CursoDTO("nome", BigDecimal.valueOf(100), "desc", "prof", "cat", List.of(modulo));

		Curso cursoSalvo = new Curso(
				"algum-id-gerado",
				cursoDTO.nome(),
				cursoDTO.preco(),
				cursoDTO.descricao(),
				cursoDTO.professor(),
				cursoDTO.categoria(),
				cursoDTO.modulos(),
				1,
				30
		);

		when(cursoRepository.save(any(Curso.class))).thenReturn(cursoSalvo);

		Curso resultado = cursoService.cadastrarCurso(cursoDTO);

		assertEquals("nome", resultado.getNome());
		assertEquals(1, resultado.getQuantidadeModulos());
		assertEquals(30, resultado.getDuracaoTotalSegundos());

		verify(cursoRepository, times(1)).save(any(Curso.class));
	}

	@Test
	void testEditarCurso() {

		Modulo modulo = new Modulo("moduloteste", Collections.emptyList());

		Curso cursoExistente = new Curso(
				"1",
				"Curso Antigo",
				BigDecimal.valueOf(100.00),
				"Desc Antiga",
				"Prof A",
				"Cat A",
				List.of(modulo),
				null,
				null
		);

		Aula aula1 = new Aula("Aula 1", "Desc 1", 30, "videoteste.mp4");
		Aula aula2 = new Aula("Aula 2", "Desc 2", 40, "videoteste2.mp4");

		Modulo moduloEdit = new Modulo("moduloteste", List.of(aula1, aula2));
		Modulo moduloEdit2 = new Modulo("moduloteste2", List.of(aula1, aula2));

		CursoEditDTO dto = new CursoEditDTO(
				"1",
				"Curso Atualizado",
				BigDecimal.valueOf(150.00),
				"Nova descrição",
				"Prof B",
				"Cat B",
				List.of(moduloEdit, moduloEdit2)
		);

		when(cursoRepository.findById("1")).thenReturn(Optional.of(cursoExistente));
		when(cursoRepository.save(any(Curso.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Curso atualizado = cursoService.editarCurso(dto);

		assertEquals("Curso Atualizado", atualizado.getNome());
		assertEquals(BigDecimal.valueOf(150.00), atualizado.getPreco());
		assertEquals(2, atualizado.getModulos().size());
		assertEquals("moduloteste", atualizado.getModulos().get(0).getTitulo());
		assertEquals("moduloteste2", atualizado.getModulos().get(1).getTitulo());
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

		Modulo modulo = new Modulo("moduloteste", Collections.emptyList());


		Curso curso = new Curso(
				"1",
				"Curso",
				BigDecimal.valueOf(100.00),
				"desc",
				"prof",
				"cat",
				List.of(modulo),
				null,
				null
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
