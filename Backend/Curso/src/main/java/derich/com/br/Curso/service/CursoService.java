package derich.com.br.Curso.service;

import derich.com.br.Curso.DTO.CursoDTO;
import derich.com.br.Curso.DTO.CursoEditDTO;
import derich.com.br.Curso.DTO.CursoResponseDTO;
import derich.com.br.Curso.entity.Aula;
import derich.com.br.Curso.entity.Curso;
import derich.com.br.Curso.entity.Modulo;
import derich.com.br.Curso.repository.ICursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

    @Autowired
    private ICursoRepository cursoRepository;

    public List<Curso> listarCursos () {
        return cursoRepository.findAll();
    }

    public Curso buscarPorId(String id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
    }

    public List<CursoResponseDTO> buscarCursosPorIds(List<String> ids) {
        List<Curso> cursos = cursoRepository.findByIdIn(ids);
        return cursos.stream()
                .map(this::converterParaDTO)
                .toList();
    }

    private CursoResponseDTO converterParaDTO(Curso curso) {
        return new CursoResponseDTO(
                curso.getId(),
                curso.getNome(),
                curso.getDescricao(),
                curso.getProfessor(),
                curso.getModulos()
        );
    }

    public Curso cadastrarCurso (CursoDTO cursoDTO) {
        Curso curso = new Curso(cursoDTO);
        int duracao = 0;
        for (Modulo m : curso.getModulos()) {
            for (Aula a : m.getAulas()) {
                duracao += a.getDuracaoEmSegundos();
            }
        }
        curso.setDuracaoTotalSegundos(duracao);
        return cursoRepository.save(curso);
    }

    public Curso editarCurso (CursoEditDTO cursoEditDTO) {
        Curso curso = cursoRepository.findById(cursoEditDTO.id())
                .orElseThrow(() -> (new RuntimeException("Curso não encontrado")));
        curso.setNome(cursoEditDTO.nome());
        curso.setPreco(cursoEditDTO.preco());
        curso.setDescricao(cursoEditDTO.descricao());
        curso.setProfessor(cursoEditDTO.professor());
        curso.setCategoria(cursoEditDTO.categoria());
        curso.setModulos(cursoEditDTO.modulos());
        return cursoRepository.save(curso);
    }

    public boolean deletarCurso (String id) {
        Optional<Curso> curso = cursoRepository.findById(id);
        if (curso.isPresent()){
            cursoRepository.delete(curso.get());
            return true;
        }
        return false;
    }
}
