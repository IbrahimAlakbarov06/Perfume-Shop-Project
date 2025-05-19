package org.perfume.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.perfume.enums.FragranceFamily;
import org.perfume.enums.Gender;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfumeResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer volumeMl;
    private Long brandId;
    private String brandName;
    private Long categoryId;
    private String categoryName;
    private Integer stockQuantity;
    private String imageUrl;
    private Gender gender;
    private FragranceFamily fragranceFamily;
}