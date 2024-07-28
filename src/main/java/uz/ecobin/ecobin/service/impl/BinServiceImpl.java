package uz.ecobin.ecobin.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.ecobin.ecobin.dto.BinCreateDTO;
import uz.ecobin.ecobin.dto.BinViewDTO;
import uz.ecobin.ecobin.exceptions.NotFoundException;
import uz.ecobin.ecobin.mapper.BinMapper;
import uz.ecobin.ecobin.model.Bin;
import uz.ecobin.ecobin.repository.BinRepository;
import uz.ecobin.ecobin.service.BinService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BinServiceImpl implements BinService {
    private final BinRepository binRepository;
    private final BinMapper binMapper;

    @Override
    public ResponseEntity<?> delete(Long id) {
        try {
            boolean b = binRepository.existsById(id);
            if (b) {
                binRepository.deleteById(id);
                return ResponseEntity.ok("Bin deleted successfully");
            } else {
                throw new NotFoundException("Bin not found !");
            }
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.ok("Something is wrong !");
        }
    }

    @Override
    public ResponseEntity<BinViewDTO> create(BinCreateDTO binCreateDTO) {
        Bin bin = binMapper.toEntity(binCreateDTO);
        bin = binRepository.save(bin);
        return ResponseEntity.ok(binMapper.toViewDTO(bin));
    }

    @Override
    public ResponseEntity<BinViewDTO> update(BinCreateDTO binCreateDTO) {
        Bin bin = binRepository.findById(binCreateDTO.getId()).orElseThrow(() -> new NotFoundException("Bin not found"));
        bin.setName(binCreateDTO.getName());
        bin.setDescription(binCreateDTO.getDescription());
        bin.setLang(bin.getLang());
        bin.setLat(bin.getLat());
        Bin save = binRepository.save(bin);
        return ResponseEntity.ok(binMapper.toViewDTO(save));
    }

    @Override
    public ResponseEntity<List<BinViewDTO>> findByNearbyBin(Double latitude, Double longitude) {
        return ResponseEntity.ok(calculateDistance(latitude,longitude));
    }

    public List<BinViewDTO> calculateDistance(double lat1, double lon1) {
        double laN = lat1 - 0.01;
        double laP = lat1 + 0.01;

        double loN = lon1 - 0.01;
        double loP = lon1 + 0.01;

        return binRepository.findByLatBetweenOrLangBetween(laN, laP, loN, loP).stream().map(binMapper::toViewDTO).toList();
    }


}
