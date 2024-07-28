package uz.ecobin.ecobin.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import uz.ecobin.ecobin.dto.NewsViewDTO;
import uz.ecobin.ecobin.model.News;

import java.util.List;

public interface NewsService {
    ResponseEntity<List<News>> findByLatestNews();

    ResponseEntity<NewsViewDTO> create(MultipartFile[] images, String newsCreateDTO);

    ResponseEntity<?> deleteById(Long id);
}
