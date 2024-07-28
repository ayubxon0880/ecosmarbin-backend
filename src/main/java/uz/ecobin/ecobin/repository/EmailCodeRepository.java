package uz.ecobin.ecobin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.ecobin.ecobin.model.EmailCode;

import java.util.Optional;

@Repository
public interface EmailCodeRepository extends JpaRepository<EmailCode,Long> {
    Optional<EmailCode> findByUserId(Long user_id);
}
