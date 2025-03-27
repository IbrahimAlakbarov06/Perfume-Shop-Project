package org.perfume.perfumebackend.mapper;

import org.perfume.perfumebackend.dto.PerfumeDto;
import org.perfume.perfumebackend.entity.Perfume;
import org.springframework.stereotype.Component;

@Component
public class PerfumeMapper {

    public PerfumeDto toDto(Perfume perfume) {
        if (perfume == null) {
            return null;
        }

        return new PerfumeDto(
                perfume.getId(),
                perfume.getName(),
                perfume.getDescription(),
                perfume.getPrice(),
                perfume.getVolumeMl(),
                perfume.getBrandName(),
                perfume.getCategoryName(),
                perfume.getStockQuantity(),
                perfume.getImageUrl()
        );
    }

    public Perfume toEntity(PerfumeDto perfumeDto) {
        if (perfumeDto == null) {
            return null;
        }

        return new Perfume(
                perfumeDto.getId(),
                perfumeDto.getName(),
                perfumeDto.getDescription(),
                perfumeDto.getPrice(),
                perfumeDto.getVolumeMl(),
                perfumeDto.getBrandName(),
                perfumeDto.getCategoryName(),
                perfumeDto.getStockQuantity(),
                perfumeDto.getImageUrl()
        );
    }
}