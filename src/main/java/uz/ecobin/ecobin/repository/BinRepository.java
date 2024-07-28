package uz.ecobin.ecobin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.ecobin.ecobin.model.Bin;

import java.util.List;

@Repository
public interface BinRepository extends JpaRepository<Bin, Long> {
    List<Bin> findByLatBetweenOrLangBetween(Double lat, Double lat2, Double lang, Double lang2);
}
