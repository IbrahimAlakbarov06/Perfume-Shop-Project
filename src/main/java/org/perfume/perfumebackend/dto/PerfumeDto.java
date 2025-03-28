package org.perfume.perfumebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfumeDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer volumeMl;
    private String brandName;
    private String categoryName;
    private Integer stockQuantity;
    private String imageUrl;
}
