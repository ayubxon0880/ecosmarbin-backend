package uz.ecobin.ecobin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BinHistoryCreateDto {
    private Long binId;
    private Integer energy;
    private Integer fullness;
    private Integer plastic;
    private Integer paper;
    private Integer metal;
    private Integer food;
    private Boolean isCleaned;
    private LocalDateTime createdAt;
}
