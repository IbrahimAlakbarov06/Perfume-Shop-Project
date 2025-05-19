package org.perfume.service;

import lombok.RequiredArgsConstructor;
import org.perfume.domain.repo.BrandDao;
import org.perfume.model.dto.response.BrandResponse;
import org.perfume.domain.entity.Brand;
import org.perfume.exception.ResourceNotFoundException;
import org.perfume.mapper.BrandMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandDao brandDao;
    private final BrandMapper brandMapper;

    public List<BrandResponse> getAllBrands() {
        return brandDao.findAll().stream()
                .map(brandMapper::toResponse)
                .collect(Collectors.toList());
    }

    public BrandResponse getBrandById(Long id) {
        Brand brand = brandDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));

        return brandMapper.toResponse(brand);
    }

    public BrandResponse getBrandByName(String name) {
        Brand brand = brandDao.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with name: " + name));
        return brandMapper.toResponse(brand);
    }

    public List<BrandResponse> searchBrands(String searchTerm) {
        return brandDao.findByNameContainingIgnoreCase(searchTerm).stream()
                .map(brandMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BrandResponse createBrand(BrandRequest brandRequest) {
        if (brandDao.existsByNameIgnoreCase(brandRequest.getName())) {
            throw new IllegalArgumentException("Brand with name '" + brandRequest.getName() + "' already exists");
        }

        Brand brand = brandMapper.toEntity(brandRequest);
        Brand savedBrand = brandDao.save(brand);
        return brandMapper.toResponse(savedBrand);
    }

    @Transactional
    public BrandResponse updateBrand(Long id, BrandRequest brandRequest) {
        Brand existingBrand = brandDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));

        if (!existingBrand.getName().equalsIgnoreCase(brandRequest.getName()) &&
                brandDao.existsByNameIgnoreCase(brandRequest.getName())) {
            throw new IllegalArgumentException("Brand with name '" + brandRequest.getName() + "' already exists");
        }

        existingBrand.setName(brandRequest.getName());
        existingBrand.setDescription(brandRequest.getDescription());
        existingBrand.setLogoUrl(brandRequest.getLogoUrl());

        Brand updatedBrand = brandDao.save(existingBrand);
        return brandMapper.toResponse(updatedBrand);
    }

    @Transactional
    public void deleteBrand(Long id) {
        if (!brandDao.existsById(id)) {
            throw new ResourceNotFoundException("Brand not found with id: " + id);
        }
        brandDao.deleteById(id);
    }
}