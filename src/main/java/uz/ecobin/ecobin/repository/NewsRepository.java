package uz.ecobin.ecobin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.ecobin.ecobin.model.News;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
}
