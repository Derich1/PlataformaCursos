package derich.com.br.Curso.repository;

import derich.com.br.Curso.entity.Curso;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends MongoRepository<Curso, String> {
}
