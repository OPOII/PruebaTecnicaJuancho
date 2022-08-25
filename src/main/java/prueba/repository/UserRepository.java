package prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prueba.model.User;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsername(String username);
}
