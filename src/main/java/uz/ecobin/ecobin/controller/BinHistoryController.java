package uz.ecobin.ecobin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ecobin.ecobin.dto.BinCreateDTO;
import uz.ecobin.ecobin.dto.BinHistoryCreateDto;
import uz.ecobin.ecobin.dto.BinViewDTO;
import uz.ecobin.ecobin.service.BinHistoryService;
import uz.ecobin.ecobin.service.BinService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bin-history")
@RequiredArgsConstructor
public class BinHistoryController {
    private final BinHistoryService binHistoryService;

    @GetMapping("/get-statistics")
    public ResponseEntity<?> statistics(@RequestParam Long binId,@RequestParam LocalDate from,@RequestParam LocalDate to) {
        return binHistoryService.findByBinStatistics(binId,from,to);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody BinHistoryCreateDto binHistoryCreateDto) {
        return binHistoryService.create(binHistoryCreateDto);
    }
}