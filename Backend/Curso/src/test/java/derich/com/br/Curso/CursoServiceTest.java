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
		Curso curso1 = new Curso("1", "Curso A", BigDecimal.valueOf(100.00), "Descrição A", "Professor A", "Categoria A");
		Curso curso2 = new Curso("2", "Curso B", BigDecimal.valueOf(200.00), "Descrição B", "Professor B", "Categoria B");

		when(cursoRepository.findAll()).thenReturn(Arrays.asList(curso1, curso2));

		List<Curso> cursos = cursoService.listarCursos();

		assertEquals(2, cursos.size());
		assertEquals("Curso A", cursos.get(0).getNome());
		verify(cursoRepository, times(1)).findAll();
	}

	@Test
	void testCadastrarCurso() {
		CursoDTO cursoDTO = new CursoDTO("Curso Novo", BigDecimal.valueOf(150.00), "Descrição nova", "Professor X", "Categoria X");
		Curso cursoSalvo = new Curso(cursoDTO);

		when(cursoRepository.save(any(Curso.class))).thenReturn(cursoSalvo);

		Curso resultado = cursoService.cadastrarCurso(cursoDTO);

		assertEquals("Curso Novo", resultado.getNome());
		assertEquals(150.0, resultado.getPreco());
		verify(cursoRepository, times(1)).save(any(Curso.class));
	}

	@Test
	void testEditarCurso() {
		Curso cursoExistente = new Curso("1", "Curso Antigo", BigDecimal.valueOf(100.00), "Desc Antiga", "Prof A", "Cat A");
		CursoEditDTO cursoEditDTO = new CursoEditDTO("1", "Curso Atualizado", BigDecimal.valueOf(200.00), "Nova descrição", "Prof B", "Cat B");

		when(cursoRepository.findById("1")).thenReturn(Optional.of(cursoExistente));
		when(cursoRepository.save(any(Curso.class))).thenReturn(cursoExistente);

		Curso atualizado = cursoService.editarCurso(cursoEditDTO);

		assertEquals("Curso Atualizado", atualizado.getNome());
		assertEquals(200.0, atualizado.getPreco());
		verify(cursoRepository).save(cursoExistente);
	}

	@Test
	void testEditarCursoNaoEncontrado() {
		CursoEditDTO cursoEditDTO = new CursoEditDTO("999", "Qualquer", BigDecimal.valueOf(0.0), "", "", "");

		when(cursoRepository.findById("999")).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> cursoService.editarCurso(cursoEditDTO));
	}

	@Test
	void testDeletarCursoComSucesso() {
		Curso curso = new Curso("1", "Curso", BigDecimal.valueOf(100.00), "desc", "prof", "cat");

		when(cursoRepository.findById("1")).thenReturn(Optional.of(curso));

		boolean resultado = cursoService.deletarCurso("1");

		assertTrue(resultado);
		verify(cursoRepository, times(1)).delete(curso);
	}

	@Test
	void testDeletarCursoNaoEncontrado() {
		when(cursoRepository.findById("999")).thenReturn(Optional.empty());

		boolean resultado = cursoService.deletarCurso("999");

		assertFalse(resultado);
		verify(cursoRepository, never()).delete(any());
	}
}
