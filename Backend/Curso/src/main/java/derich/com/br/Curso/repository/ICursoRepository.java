package derich.com.br.Curso.repository;

import derich.com.br.Curso.entity.Curso;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICursoRepository extends MongoRepository<Curso, String> {
    Optional<Curso> findByNome(String nome);
}
