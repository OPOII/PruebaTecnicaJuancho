package prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prueba.model.CryptoCoin;

public interface CryptoRepository extends JpaRepository<CryptoCoin,Long> {
    CryptoCoin findBySymbol(String symbol);
}
