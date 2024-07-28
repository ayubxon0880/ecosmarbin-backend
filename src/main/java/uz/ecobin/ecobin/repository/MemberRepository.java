package uz.ecobin.ecobin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.ecobin.ecobin.model.Member;
import uz.ecobin.ecobin.model.News;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
