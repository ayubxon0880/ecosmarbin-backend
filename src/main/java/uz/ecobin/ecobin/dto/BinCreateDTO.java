package uz.ecobin.ecobin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BinCreateDTO {
    private Long id;
    private String name;
    private String description;
    private Double lat;
    private Double lang;
}
