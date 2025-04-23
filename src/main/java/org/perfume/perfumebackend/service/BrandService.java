package org.perfume.perfumebackend.service;

import lombok.RequiredArgsConstructor;
import org.perfume.perfumebackend.dao.BrandDao;
import org.perfume.perfumebackend.dto.BrandDto;
import org.perfume.perfumebackend.entity.Brand;
import org.perfume.perfumebackend.exception.ResourceNotFoundException;
import org.perfume.perfumebackend.mapper.BrandMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandDao brandDao;
    private final BrandMapper brandMapper;

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

    public BrandDto getBrandByName(String name) {
        Brand brand = brandDao.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with name: " + name));
        return brandMapper.toDto(brand);
    }

    public List<BrandDto> searchBrands(String searchTerm) {
        return brandDao.findByNameContainingIgnoreCase(searchTerm).stream()
                .map(brandMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public BrandDto createBrand(BrandDto brandDto) {
        if (brandDao.existsByNameIgnoreCase(brandDto.getName())) {
            throw new IllegalArgumentException("Brand with name '" + brandDto.getName() + "' already exists");
        }

        Brand brand = brandMapper.toEntity(brandDto);
        Brand savedBrand = brandDao.save(brand);
        return brandMapper.toDto(savedBrand);
    }

    @Transactional
    public BrandDto updateBrand(Long id, BrandDto brandDto) {
        Brand existingBrand = brandDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));

        if (!existingBrand.getName().equalsIgnoreCase(brandDto.getName()) &&
                brandDao.existsByNameIgnoreCase(brandDto.getName())) {
            throw new IllegalArgumentException("Brand with name '" + brandDto.getName() + "' already exists");
        }

        existingBrand.setName(brandDto.getName());
        existingBrand.setDescription(brandDto.getDescription());
        existingBrand.setLogoUrl(brandDto.getLogoUrl());

        Brand updatedBrand = brandDao.save(existingBrand);
        return brandMapper.toDto(updatedBrand);
    }

    @Transactional
    public void deleteBrand(Long id) {
        if (!brandDao.existsById(id)) {
            throw new ResourceNotFoundException("Brand not found with id: " + id);
        }
        brandDao.deleteById(id);
    }

}