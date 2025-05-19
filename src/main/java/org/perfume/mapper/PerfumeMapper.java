package org.perfume.mapper;

import org.perfume.dto.request.PerfumeRequest;
import org.perfume.dto.response.PerfumeResponse;
import org.perfume.entity.Brand;
import org.perfume.entity.Category;
import org.perfume.entity.Perfume;
import org.springframework.stereotype.Component;

@Component
public class PerfumeMapper {

    public PerfumeResponse toResponse(Perfume perfume) {
        if (perfume == null) {
            return null;
        }

        PerfumeResponse response = new PerfumeResponse();
        response.setId(perfume.getId());
        response.setName(perfume.getName());
        response.setDescription(perfume.getDescription());
        response.setPrice(perfume.getPrice());
        response.setVolumeMl(perfume.getVolumeMl());

        if (perfume.getBrand() != null) {
            response.setBrandId(perfume.getBrand().getId());
            response.setBrandName(perfume.getBrand().getName());
        }

        if (perfume.getCategory() != null) {
            response.setCategoryId(perfume.getCategory().getId());
            response.setCategoryName(perfume.getCategory().getName());
        }

        response.setStockQuantity(perfume.getStockQuantity());
        response.setImageUrl(perfume.getImageUrl());
        response.setFragranceFamily(perfume.getFragranceFamily());
        response.setGender(perfume.getGender());

        return response;
    }

    public Perfume toEntity(PerfumeRequest request, Brand brand, Category category) {
        if (request == null) {
            return null;
        }

        Perfume perfume = new Perfume();
        perfume.setName(request.getName());
        perfume.setDescription(request.getDescription());
        perfume.setPrice(request.getPrice());
        perfume.setVolumeMl(request.getVolumeMl());
        perfume.setBrand(brand);
        perfume.setCategory(category);
        perfume.setStockQuantity(request.getStockQuantity());
        perfume.setImageUrl(request.getImageUrl());
        perfume.setFragranceFamily(request.getFragranceFamily());
        perfume.setGender(request.getGender());

        return perfume;
    }
}