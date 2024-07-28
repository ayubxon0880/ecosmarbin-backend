package uz.ecobin.ecobin.mapper;

import org.springframework.stereotype.Service;
import uz.ecobin.ecobin.dto.BinCreateDTO;
import uz.ecobin.ecobin.dto.BinViewDTO;
import uz.ecobin.ecobin.model.Bin;

import java.time.LocalDateTime;

@Service
public class BinMapper {
    public Bin toEntity(BinCreateDTO bin) {
        if (bin == null) return null;

        return new Bin(
                bin.getId(),
                bin.getName(),
                bin.getDescription(),
                0,0,0,0,0,0,
                bin.getLat(),
                bin.getLang(),
                null,
                LocalDateTime.now(),
                null
        );
    }

    public BinViewDTO toViewDTO(Bin bin) {
        if (bin == null) return null;
        return new BinViewDTO(
                bin.getId(),
                bin.getName(),
                bin.getEnergy(),
                bin.getFullness(),
                bin.getPlastic(),
                bin.getPaper(),
                bin.getMetal(),
                bin.getFood(),
                bin.getLat(),
                bin.getLang(),
                bin.getCleanedAt(),
                bin.getCreatedAt(),
                bin.getUpdatedAt()
        );
    }
}
