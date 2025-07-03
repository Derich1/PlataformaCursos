package derich.com.br.Curso;

import com.fasterxml.jackson.databind.ObjectMapper;
import derich.com.br.Curso.DTO.CursoDTO;
import derich.com.br.Curso.DTO.CursoEditDTO;
import derich.com.br.Curso.controller.CursoController;
import derich.com.br.Curso.entity.Aula;
import derich.com.br.Curso.entity.Curso;
import derich.com.br.Curso.entity.Modulo;
import derich.com.br.Curso.service.CursoService;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CursoController.class)
class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CursoService cursoService;

    @MockitoBean
    private Tracer tracer;

    @Test
    @DisplayName("GET /curso - deve listar todos os cursos")
    void deveListarCursos() throws Exception {
        Aula aula = new Aula();
        aula.setTitulo("Aula 1");
        aula.setDescricao("Descrição 1");
        aula.setDuracaoEmSegundos(180);

        Modulo modulo = new Modulo();
        modulo.setTitulo("Módulo A");
        modulo.setAulas(List.of(aula));

        Curso cursoSalvo = new Curso(
                "algum-id",
                "Curso Teste",
                BigDecimal.TEN,
                "Descrição",
                "Professor",
                "Categoria",
                List.of(modulo),
                1,
                180
        );
        when(cursoService.listarCursos()).thenReturn(Collections.singletonList(cursoSalvo));

        mockMvc.perform(get("/curso"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Curso Teste"));
    }

    @Test
    @DisplayName("GET /curso/{id} - deve buscar curso por ID")
    void deveBuscarCursoPorId() throws Exception {
        Aula aula = new Aula();
        aula.setTitulo("Aula 1");
        aula.setDescricao("Descrição 1");
        aula.setDuracaoEmSegundos(180);

        Modulo modulo = new Modulo();
        modulo.setTitulo("Módulo A");
        modulo.setAulas(List.of(aula));

        Curso cursoSalvo = new Curso(
                "id123",
                "Curso Teste",
                BigDecimal.TEN,
                "Descrição",
                "Professor",
                "Categoria",
                List.of(modulo),
                1,
                180
        );
        when(cursoService.buscarPorId("id123")).thenReturn(cursoSalvo);

        mockMvc.perform(get("/curso/id123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Curso Teste"));
    }

    @Test
    @DisplayName("POST /curso - deve cadastrar um novo curso")
    void deveCadastrarCurso() throws Exception {
        Aula aula = new Aula();
        aula.setTitulo("Aula 1");
        aula.setDescricao("Descrição 1");
        aula.setDuracaoEmSegundos(180);

        Modulo modulo = new Modulo();
        modulo.setTitulo("Módulo A");
        modulo.setAulas(List.of(aula));

        CursoDTO dto = new CursoDTO(
                "Curso Teste",
                BigDecimal.TEN,
                "Descrição",
                "Professor",
                "Categoria",
                List.of(modulo)
        );

        Curso cursoSalvo = new Curso(
                "id123",
                dto.nome(),
                dto.preco(),
                dto.descricao(),
                dto.professor(),
                dto.categoria(),
                dto.modulos(),
                1,
                180
        );
        when(cursoService.cadastrarCurso(any(CursoDTO.class))).thenReturn(cursoSalvo);

        mockMvc.perform(post("/curso")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Curso Teste"));
    }

    @Test
    @DisplayName("PUT /curso - deve editar um curso existente")
    void deveEditarCurso() throws Exception {
        Aula aula = new Aula("Aula 1", "Desc 1", 30, "video.mp4");
        Modulo modulo = new Modulo("Módulo Editado", List.of(aula));

        CursoEditDTO editDTO = new CursoEditDTO(
                "1",
                "Curso Editado",
                BigDecimal.valueOf(150),
                "Nova descrição",
                "Prof",
                "Cat",
                List.of(modulo)
        );

        Curso cursoEditado = new Curso(
                editDTO.id(),
                editDTO.nome(),
                editDTO.preco(),
                editDTO.descricao(),
                editDTO.professor(),
                editDTO.categoria(),
                editDTO.modulos(),
                null,
                null
        );
        when(cursoService.editarCurso(any(CursoEditDTO.class))).thenReturn(cursoEditado);

        mockMvc.perform(put("/curso")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Curso Editado"));
    }

    @Test
    @DisplayName("DELETE /curso/{id} - deve deletar curso existente")
    void deveDeletarCursoComSucesso() throws Exception {
        when(cursoService.deletarCurso("id1")).thenReturn(true);

        mockMvc.perform(delete("/curso/id1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deletado com sucesso"));
    }

    @Test
    @DisplayName("DELETE /curso/{id} - retorna 404 se curso não existir")
    void deveRetornar404AoTentarDeletarCursoInexistente() throws Exception {
        when(cursoService.deletarCurso("id999")).thenReturn(false);

        mockMvc.perform(delete("/curso/id999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Curso não encontrado"));
    }

    @Test
    @DisplayName("GET /curso/test - deve gerar span com sucesso")
    void deveGerarSpanComSucesso() throws Exception {
        Span mockSpan = Mockito.mock(Span.class);
        Tracer.SpanInScope mockScope = Mockito.mock(Tracer.SpanInScope.class);

        when(tracer.nextSpan()).thenReturn(mockSpan);
        when(tracer.withSpan(mockSpan)).thenReturn(mockScope);
        when(mockSpan.name(anyString())).thenReturn(mockSpan);
        when(mockSpan.start()).thenReturn(mockSpan);

        mockMvc.perform(get("/curso/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("Span gerado"));

        Mockito.verify(mockSpan).end(); // opcional, para garantir que terminou o span
    }

}
