package org.perfume.perfumebackend.mapper;

import org.perfume.perfumebackend.dto.PerfumeDto;
import org.perfume.perfumebackend.entity.Perfume;
import org.springframework.stereotype.Component;

@Component // Ensure this annotation is present
public class PerfumeMapper {

    public PerfumeDto toDto(Perfume product) {
        if (product == null) {
            return null;
        }

        return new PerfumeDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getBrandName(),
                product.getCategoryName(),
                product.getStockQuantity(),
                product.getImageUrl()
        );
    }

    public Perfume toEntity(PerfumeDto productDto) {
        if (productDto == null) {
            return null;
        }

        return new Perfume(
                productDto.getId(),
                productDto.getName(),
                productDto.getDescription(),
                productDto.getPrice(),
                productDto.getBrandName(),
                productDto.getCategoryName(),
                productDto.getStockQuantity(),
                productDto.getImageUrl()
        );
    }
}