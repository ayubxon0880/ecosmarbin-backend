package uz.ecobin.ecobin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsViewDTO {
    private Long id;
    private String title;
    private String content;
    private List<String> images;
}
