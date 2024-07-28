package uz.ecobin.ecobin.mapper;

import org.springframework.stereotype.Service;
import uz.ecobin.ecobin.dto.NewsCreateDTO;
import uz.ecobin.ecobin.dto.NewsViewDTO;
import uz.ecobin.ecobin.model.Image;
import uz.ecobin.ecobin.model.News;

import java.time.LocalDateTime;

@Service
public class NewsMapper {
    public News toEntity(NewsCreateDTO dto) {
        if (dto == null) return null;
        return new News(
                null,
                dto.getTitle(),
                dto.getContent(),
                null,
                LocalDateTime.now()
        );
    }

    public NewsViewDTO toViewDto(News entity) {
        if (entity == null) return null;

        return new NewsViewDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getImages() == null ?  null : entity.getImages().stream().map(Image::getUrl).toList()
        );
    }
}
