package uz.ecobin.ecobin.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.ecobin.ecobin.dto.BinHistoryCreateDto;
import uz.ecobin.ecobin.exceptions.NotFoundException;
import uz.ecobin.ecobin.model.BinHistory;
import uz.ecobin.ecobin.repository.BinHistoryRepository;
import uz.ecobin.ecobin.repository.BinRepository;
import uz.ecobin.ecobin.service.BinHistoryService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BinHistoryServiceImpl implements BinHistoryService {
    private final BinHistoryRepository binHistoryRepository;
    private final BinRepository binRepository;

    @Override
    public ResponseEntity<?> findByBinStatistics(Long binId, LocalDate from, LocalDate to) {
        List<BinHistory> bins = binHistoryRepository.findByBinAndCreatedAtBetween(binRepository.findById(binId).orElseThrow(() -> new NotFoundException("Bin not found")), from.atStartOfDay(), to.atStartOfDay());
//        CommonResponse<Object> commonResponse = new CommonResponse<>();
//        commonResponse.add("histories", bins);

        return ResponseEntity.ok().body(bins);
    }

    @Override
    public ResponseEntity<?> create(BinHistoryCreateDto binHistoryCreateDto) {
        binHistoryRepository.save(new BinHistory(
                null,
                binRepository.findById(binHistoryCreateDto.getBinId()).orElseThrow(() -> new NotFoundException("Bin not found")),
                binHistoryCreateDto.getEnergy(),
                binHistoryCreateDto.getFullness(),
                binHistoryCreateDto.getPlastic(),
                binHistoryCreateDto.getPaper(),
                binHistoryCreateDto.getMetal(),
                binHistoryCreateDto.getFood(),
                binHistoryCreateDto.getIsCleaned(),
                LocalDateTime.now()
        ));
        return ResponseEntity.ok("Bin history successfully added");
    }
}
