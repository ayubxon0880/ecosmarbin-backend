package uz.ecobin.ecobin.service;

import org.springframework.http.ResponseEntity;
import uz.ecobin.ecobin.dto.BinHistoryCreateDto;

import java.time.LocalDate;

public interface BinHistoryService {
    ResponseEntity<?> findByBinStatistics(Long binId, LocalDate from, LocalDate to);

    ResponseEntity<?> create(BinHistoryCreateDto binHistoryCreateDto);
}
