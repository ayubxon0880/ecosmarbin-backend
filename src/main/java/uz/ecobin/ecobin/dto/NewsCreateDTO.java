package uz.ecobin.ecobin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsCreateDTO {
    private String title;
    private String content;
}
