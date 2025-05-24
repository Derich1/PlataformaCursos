package derich.com.br.Usuario.repository;

import derich.com.br.Usuario.entity.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
}
