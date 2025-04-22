package org.perfume.perfumebackend.service;

import org.perfume.perfumebackend.dao.BrandDao;
import org.perfume.perfumebackend.dto.BrandDto;
import org.perfume.perfumebackend.entity.Brand;
import org.perfume.perfumebackend.exception.ResourceNotFoundException;
import org.perfume.perfumebackend.mapper.BrandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandService {

    private final BrandDao brandDao;
    private final BrandMapper brandMapper;

    @Autowired
    public BrandService(BrandDao brandDao, BrandMapper brandMapper) {
        this.brandDao = brandDao;
        this.brandMapper = brandMapper;
    }

    public List<BrandDto> getAllBrands() {
        return brandDao.findAll().stream()
                .map(brandMapper::toDto)
                .collect(Collectors.toList());
    }

    public BrandDto getBrandById(Long id) {
        Brand brand = brandDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));
        return brandMapper.toDto(brand);
    }

    public BrandDto createBrand(BrandDto brandDto) {
        Brand brand = brandMapper.toEntity(brandDto);
        Brand savedBrand = brandDao.save(brand);
        return brandMapper.toDto(savedBrand);
    }

    public BrandDto updateBrand(Long id, BrandDto brandDto) {
        Brand existingBrand = brandDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));

        existingBrand.setName(brandDto.getName());
        existingBrand.setDescription(brandDto.getDescription());
        existingBrand.setLogoUrl(brandDto.getLogoUrl());

        Brand updatedBrand = brandDao.save(existingBrand);
        return brandMapper.toDto(updatedBrand);
    }

    public void deleteBrand(Long id) {
        if (!brandDao.existsById(id)) {
            throw new ResourceNotFoundException("Brand not found with id: " + id);
        }
        brandDao.deleteById(id);
    }
}