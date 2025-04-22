package org.perfume.perfumebackend.mapper;

import org.perfume.perfumebackend.dto.PerfumeDto;
import org.perfume.perfumebackend.entity.Perfume;
import org.perfume.perfumebackend.entity.Brand;
import org.perfume.perfumebackend.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class PerfumeMapper {

    public PerfumeDto toDto(Perfume perfume) {
        if (perfume == null) {
            return null;
        }

        PerfumeDto dto = new PerfumeDto();
        dto.setId(perfume.getId());
        dto.setName(perfume.getName());
        dto.setDescription(perfume.getDescription());
        dto.setPrice(perfume.getPrice());
        dto.setVolumeMl(perfume.getVolumeMl());

        if (perfume.getBrand() != null) {
            dto.setBrandId(perfume.getBrand().getId());
            dto.setBrandName(perfume.getBrand().getName());
        }

        if (perfume.getCategory() != null) {
            dto.setCategoryId(perfume.getCategory().getId());
            dto.setCategoryName(perfume.getCategory().getName());
        }

        dto.setStockQuantity(perfume.getStockQuantity());
        dto.setImageUrl(perfume.getImageUrl());
        dto.setFragrance(perfume.getFragrance());
        dto.setGender(perfume.getGender());

        return dto;
    }

    public Perfume toEntity(PerfumeDto dto, Brand brand, Category category) {
        if (dto == null) {
            return null;
        }

        Perfume perfume = new Perfume();
        perfume.setId(dto.getId());
        perfume.setName(dto.getName());
        perfume.setDescription(dto.getDescription());
        perfume.setPrice(dto.getPrice());
        perfume.setVolumeMl(dto.getVolumeMl());
        perfume.setBrand(brand);
        perfume.setCategory(category);
        perfume.setStockQuantity(dto.getStockQuantity());
        perfume.setImageUrl(dto.getImageUrl());
        perfume.setFragrance(dto.getFragrance());
        perfume.setGender(dto.getGender());

        return perfume;
    }
}