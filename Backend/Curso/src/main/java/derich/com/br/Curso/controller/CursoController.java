package derich.com.br.Curso.controller;

import derich.com.br.Curso.DTO.CursoDTO;
import derich.com.br.Curso.DTO.CursoEditDTO;
import derich.com.br.Curso.entity.Curso;
import derich.com.br.Curso.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/curso")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    private List<Curso> listarCursos () {
        return cursoService.listarCursos();
    }

    @PostMapping
    private Curso cadastrarCurso (CursoDTO cursoDTO) {
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
