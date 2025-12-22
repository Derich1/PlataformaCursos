package derich.com.br.curso.repository;

import derich.com.br.curso.entity.Curso;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICursoRepository extends MongoRepository<Curso, String> {
    Optional<Curso> findByNome(String nome);
    List<Curso> findByIdIn(List<String> ids);
}
