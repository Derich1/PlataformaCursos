package derich.com.br.Pagamento.repository;

import derich.com.br.Pagamento.entity.Pagamento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends MongoRepository<Pagamento, String> {
}
