package uz.ecobin.ecobin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,length = 300)
    private String title;
    @Column(nullable = false,columnDefinition = "TEXT")
    private String content;
    @OneToMany
    private List<Image> images;
    private LocalDateTime createdAt;
}
