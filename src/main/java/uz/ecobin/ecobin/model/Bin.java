package uz.ecobin.ecobin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Bin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
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
