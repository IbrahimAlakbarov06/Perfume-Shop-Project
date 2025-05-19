package org.perfume.mapper;

<<<<<<< HEAD
import org.perfume.dto.request.BrandRequest;
import org.perfume.dto.response.BrandResponse;
import org.perfume.entity.Brand;
=======
import org.perfume.model.dto.response.BrandDto;
import org.perfume.domain.entity.Brand;
>>>>>>> 37e4850 (packages updated)
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    public BrandResponse toResponse(Brand brand) {
        if (brand == null) {
            return null;
        }

        BrandResponse response = new BrandResponse();
        response.setId(brand.getId());
        response.setName(brand.getName());
        response.setDescription(brand.getDescription());
        response.setLogoUrl(brand.getLogoUrl());

        return response;
    }

    public Brand toEntity(BrandRequest request) {
        if (request == null) {
            return null;
        }

        Brand brand = new Brand();
        brand.setName(request.getName());
        brand.setDescription(request.getDescription());
        brand.setLogoUrl(request.getLogoUrl());

        return brand;
    }
}