package uz.ecobin.ecobin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.ecobin.ecobin.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String username);
    
    boolean existsByEmail(String email);
    List<User> findByRoleName(String role);

}
