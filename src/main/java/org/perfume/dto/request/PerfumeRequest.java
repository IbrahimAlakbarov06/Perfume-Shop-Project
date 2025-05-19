package org.perfume.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.perfume.enums.FragranceFamily;
import org.perfume.enums.Gender;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfumeRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer volumeMl;
    private Long brandId;
    private Long categoryId;
    private Integer stockQuantity;
    private String imageUrl;
    private Gender gender;
    private FragranceFamily fragranceFamily;
}