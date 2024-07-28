package uz.ecobin.ecobin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.ecobin.ecobin.model.Bin;
import uz.ecobin.ecobin.model.BinHistory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface BinHistoryRepository extends JpaRepository<BinHistory, Long> {
    List<BinHistory> findByBinAndCreatedAtBetween(Bin bin, LocalDateTime createdAt, LocalDateTime createdAt2);
}
