package uz.ecobin.ecobin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BinViewDTO {
    private Long id;
    private String name;
    private Integer energy;
    private Integer fullness;
    private Integer plastic;
    private Integer paper;
    private Integer metal;
    private Integer food;
    private Double lat;
    private Double lang;
    private LocalDateTime cleanedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
