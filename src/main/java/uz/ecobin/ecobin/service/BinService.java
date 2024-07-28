package uz.ecobin.ecobin.service;

import org.springframework.http.ResponseEntity;
import uz.ecobin.ecobin.dto.BinCreateDTO;
import uz.ecobin.ecobin.dto.BinViewDTO;

import java.util.List;

public interface BinService {
    ResponseEntity<List<BinViewDTO>> findByNearbyBin(Double latitude, Double longitude);

    ResponseEntity<?> delete(Long id);

    ResponseEntity<BinViewDTO> create(BinCreateDTO binCreateDTO);

    ResponseEntity<BinViewDTO> update(BinCreateDTO binCreateDTO);
}
