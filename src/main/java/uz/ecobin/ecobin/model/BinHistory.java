package uz.ecobin.ecobin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BinHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Bin bin;
    private Integer energy;
    private Integer fullness;
    private Integer plastic;
    private Integer paper;
    private Integer metal;
    private Integer food;
    private Boolean isCleaned;
    private LocalDateTime createdAt;
}
