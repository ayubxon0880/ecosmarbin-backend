package uz.ecobin.ecobin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.ecobin.ecobin.dto.BinCreateDTO;
import uz.ecobin.ecobin.dto.BinViewDTO;
import uz.ecobin.ecobin.service.BinService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bin")
@RequiredArgsConstructor
public class BinController {
    private final BinService binService;

    @GetMapping("/get-nearby-bin")
    public ResponseEntity<List<BinViewDTO>> getNearbyBin(@RequestParam Double latitude, @RequestParam Double longitude) {
        return binService.findByNearbyBin(latitude,longitude);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<BinViewDTO> createBin(@RequestBody BinCreateDTO binCreateDTO) {
        return binService.create(binCreateDTO);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<BinViewDTO> updateBin(@RequestBody BinCreateDTO binCreateDTO) {
        return binService.update(binCreateDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteNews(@PathVariable Long id) {
        return binService.delete(id);
    }
}