package uz.ecobin.ecobin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.ecobin.ecobin.dto.NewsViewDTO;
import uz.ecobin.ecobin.model.News;
import uz.ecobin.ecobin.service.NewsService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @GetMapping("/latest")
    public ResponseEntity<List<News>> getLatestNews() {
        return newsService.findByLatestNews();
    }


    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<NewsViewDTO> createNews(@RequestParam("images") MultipartFile[] images, @RequestParam String newsCreateDTO) {
        return newsService.create(images,newsCreateDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteNews(@PathVariable Long id) {
        return newsService.deleteById(id);
    }
}
