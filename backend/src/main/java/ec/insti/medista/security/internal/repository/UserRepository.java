package ec.insti.medista.security.internal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ec.insti.medista.security.internal.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
}
