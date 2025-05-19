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

        BrandDto dto = new BrandDto();
        dto.setId(brand.getId());
        dto.setName(brand.getName());
        dto.setDescription(brand.getDescription());
        dto.setLogoUrl(brand.getLogoUrl());

        return dto;
    }

    public Brand toEntity(BrandDto dto) {
        if (dto == null) {
            return null;
        }

        Brand brand = new Brand();
        brand.setId(dto.getId());
        brand.setName(dto.getName());
        brand.setDescription(dto.getDescription());
        brand.setLogoUrl(dto.getLogoUrl());

        return brand;
    }
}