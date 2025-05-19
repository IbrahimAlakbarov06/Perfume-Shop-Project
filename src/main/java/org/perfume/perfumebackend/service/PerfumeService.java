package org.perfume.perfumebackend.service;

import lombok.RequiredArgsConstructor;
import org.perfume.perfumebackend.dao.BrandDao;
import org.perfume.perfumebackend.dao.CategoryDao;
import org.perfume.perfumebackend.dao.PerfumeDao;
import org.perfume.perfumebackend.dto.PerfumeDto;
import org.perfume.perfumebackend.entity.Brand;
import org.perfume.perfumebackend.entity.Category;
import org.perfume.perfumebackend.entity.Perfume;
import org.perfume.perfumebackend.enums.FragranceFamily;
import org.perfume.perfumebackend.enums.Gender;
import org.perfume.perfumebackend.exception.ResourceNotFoundException;
import org.perfume.perfumebackend.mapper.PerfumeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerfumeService {

    private final PerfumeDao perfumeDao;
    private final CategoryDao categoryDao;
    private final BrandDao brandDao;
    private final PerfumeMapper perfumeMapper;

    public List<PerfumeDto> getAllPerfumes() {
        return perfumeDao.findAll().stream()
                .map(perfumeMapper::toDto)
                .collect(Collectors.toList());
    }

    public PerfumeDto getPerfumeById(Long id) {
        Perfume perfume = perfumeDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfume with id " + id + " not found"));
        return perfumeMapper.toDto(perfume);
    }

    public List<PerfumeDto> getPerfumesByBrandId(Long brandId) {
        if (!brandDao.existsById(brandId)) {
            throw new ResourceNotFoundException("Brand not found with id: " + brandId);
        }

        return perfumeDao.findByBrand_Id(brandId).stream()
                .map(perfumeMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PerfumeDto> getPerfumesByBrandName(String brandName) {
        if (!brandDao.existsByNameIgnoreCase(brandName)) {
            throw new ResourceNotFoundException("Brand not found with name: " + brandName);
        }

        return perfumeDao.findByBrand_NameIgnoreCase(brandName).stream()
                .map(perfumeMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PerfumeDto> getPerfumesByCategoryId(Long categoryId) {
        if (!categoryDao.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }

        return perfumeDao.findByCategory_Id(categoryId).stream()
                .map(perfumeMapper::toDto)
                .collect(Collectors.toList());
    }


    public List<PerfumeDto> searchPerfumes(String searchTerm) {
        return perfumeDao.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchTerm, searchTerm).stream()
                .map(perfumeMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PerfumeDto> getPerfumesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return perfumeDao.findByPriceBetween(minPrice, maxPrice).stream()
                .map(perfumeMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PerfumeDto> filterPerfumes(String brandName, BigDecimal minPrice, BigDecimal maxPrice) {
        if (brandName != null && minPrice != null && maxPrice != null) {
            return perfumeDao.findByBrand_NameIgnoreCaseAndPriceBetween(brandName, minPrice, maxPrice).stream()
                    .map(perfumeMapper::toDto)
                    .collect(Collectors.toList());
        } else if (brandName != null) {
            return perfumeDao.findByBrand_NameIgnoreCase(brandName).stream()
                    .map(perfumeMapper::toDto)
                    .collect(Collectors.toList());
        } else if (minPrice != null && maxPrice != null) {
            return getPerfumesByPriceRange(minPrice, maxPrice);
        }

        return getAllPerfumes();
    }

    public List<PerfumeDto> getPerfumesByGender(Gender gender) {
        return perfumeDao.findByGenderIgnoreCase(gender).stream()
                .map(perfumeMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PerfumeDto> getPerfumesByFragranceType(FragranceFamily fragrance) {
        return perfumeDao.findByFragranceContainingIgnoreCase(fragrance).stream()
                .map(perfumeMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PerfumeDto> getPerfumesInStock(Integer minStock) {
        return perfumeDao.findByStockQuantityGreaterThan(minStock).stream()
                .map(perfumeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public PerfumeDto createPerfume(PerfumeDto perfumeDto) {
        Brand brand = brandDao.findById(perfumeDto.getBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + perfumeDto.getBrandId()));

        Category category = categoryDao.findById(perfumeDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + perfumeDto.getCategoryId()));

        Perfume perfume = perfumeMapper.toEntity(perfumeDto, brand, category);
        Perfume savedPerfume = perfumeDao.save(perfume);

        return perfumeMapper.toDto(savedPerfume);
    }

    @Transactional
    public PerfumeDto updatePerfume(Long id, PerfumeDto perfumeDto) {
        if (!perfumeDao.existsById(id)) {
            throw new ResourceNotFoundException("Perfume not found with id: " + id);
        }

        Brand brand = getBrandEntity(perfumeDto.getBrandId());
        Category category = getCategoryEntity(perfumeDto.getCategoryId());

        perfumeDto.setId(id);
        Perfume perfume = perfumeMapper.toEntity(perfumeDto, brand, category);
        Perfume updatedPerfume = perfumeDao.save(perfume);

        return perfumeMapper.toDto(updatedPerfume);
    }

    @Transactional
    public void deletePerfume(Long id) {
        if (!perfumeDao.existsById(id)) {
            throw new ResourceNotFoundException("Perfume not found with id: " + id);
        }
        perfumeDao.deleteById(id);
    }

    private Brand getBrandEntity(Long brandId) {
        return brandDao.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + brandId));
    }

    private Category getCategoryEntity(Long categoryId) {
        return categoryDao.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
    }

}