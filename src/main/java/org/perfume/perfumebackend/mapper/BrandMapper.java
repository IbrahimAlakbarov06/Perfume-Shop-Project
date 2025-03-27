package org.perfume.perfumebackend.mapper;

import org.perfume.perfumebackend.dto.BrandDto;
import org.perfume.perfumebackend.entity.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    public BrandDto toDto(Brand brand) {
        if (brand == null) {
            return null;
        }

        return new BrandDto(
                brand.getId(),
                brand.getName(),
                brand.getDescription(),
                brand.getCountry(),
                brand.getFoundedYear(),
                brand.getLogoUrl()
        );
    }

    public Brand toEntity(BrandDto brandDto) {
        if (brandDto == null) {
            return null;
        }

        return new Brand(
                brandDto.getId(),
                brandDto.getName(),
                brandDto.getDescription(),
                brandDto.getCountry(),
                brandDto.getFoundedYear(),
                brandDto.getLogoUrl()
        );
    }
}