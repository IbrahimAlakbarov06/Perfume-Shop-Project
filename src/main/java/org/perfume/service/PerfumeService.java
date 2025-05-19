package org.perfume.service;

import lombok.RequiredArgsConstructor;

import org.perfume.domain.repo.BrandDao;
import org.perfume.domain.repo.CategoryDao;
import org.perfume.domain.repo.PerfumeDao;
import org.perfume.model.dto.response.PerfumeResponse;
import org.perfume.domain.entity.Brand;
import org.perfume.domain.entity.Category;
import org.perfume.domain.entity.Perfume;
import org.perfume.model.enums.FragranceFamily;
import org.perfume.model.enums.Gender;
import org.perfume.exception.ResourceNotFoundException;
import org.perfume.mapper.PerfumeMapper;
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

    public List<PerfumeResponse> getAllPerfumes() {
        return perfumeDao.findAll().stream()
                .map(perfumeMapper::toResponse)
                .collect(Collectors.toList());
    }

    public PerfumeResponse getPerfumeById(Long id) {
        Perfume perfume = perfumeDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfume with id " + id + " not found"));
        return perfumeMapper.toResponse(perfume);
    }

    public List<PerfumeResponse> getPerfumesByBrandId(Long brandId) {
        if (!brandDao.existsById(brandId)) {
            throw new ResourceNotFoundException("Brand not found with id: " + brandId);
        }

        return perfumeDao.findByBrand_Id(brandId).stream()
                .map(perfumeMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<PerfumeResponse> getPerfumesByBrandName(String brandName) {
        if (!brandDao.existsByNameIgnoreCase(brandName)) {
            throw new ResourceNotFoundException("Brand not found with name: " + brandName);
        }

        return perfumeDao.findByBrand_NameIgnoreCase(brandName).stream()
                .map(perfumeMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<PerfumeResponse> getPerfumesByCategoryId(Long categoryId) {
        if (!categoryDao.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }

        return perfumeDao.findByCategory_Id(categoryId).stream()
                .map(perfumeMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<PerfumeResponse> searchPerfumes(String searchTerm) {
        return perfumeDao.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchTerm, searchTerm).stream()
                .map(perfumeMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<PerfumeResponse> getPerfumesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return perfumeDao.findByPriceBetween(minPrice, maxPrice).stream()
                .map(perfumeMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<PerfumeResponse> filterPerfumes(String brandName, BigDecimal minPrice, BigDecimal maxPrice) {
        if (brandName != null && minPrice != null && maxPrice != null) {
            return perfumeDao.findByBrand_NameIgnoreCaseAndPriceBetween(brandName, minPrice, maxPrice).stream()
                    .map(perfumeMapper::toResponse)
                    .collect(Collectors.toList());
        } else if (brandName != null) {
            return perfumeDao.findByBrand_NameIgnoreCase(brandName).stream()
                    .map(perfumeMapper::toResponse)
                    .collect(Collectors.toList());
        } else if (minPrice != null && maxPrice != null) {
            return getPerfumesByPriceRange(minPrice, maxPrice);
        }

        return getAllPerfumes();
    }

    public List<PerfumeResponse> getPerfumesByGender(Gender gender) {
        return perfumeDao.findByGenderIgnoreCase(gender).stream()
                .map(perfumeMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<PerfumeResponse> getPerfumesByFragranceType(FragranceFamily fragrance) {
        return perfumeDao.findByFragranceContainingIgnoreCase(fragrance).stream()
                .map(perfumeMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<PerfumeResponse> getPerfumesInStock(Integer minStock) {
        return perfumeDao.findByStockQuantityGreaterThan(minStock).stream()
                .map(perfumeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PerfumeResponse createPerfume(PerfumeRequest perfumeRequest) {
        Brand brand = brandDao.findById(perfumeRequest.getBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + perfumeRequest.getBrandId()));

        Category category = categoryDao.findById(perfumeRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + perfumeRequest.getCategoryId()));

        Perfume perfume = perfumeMapper.toEntity(perfumeRequest, brand, category);
        Perfume savedPerfume = perfumeDao.save(perfume);

        return perfumeMapper.toResponse(savedPerfume);
    }

    @Transactional
    public PerfumeResponse updatePerfume(Long id, PerfumeRequest perfumeRequest) {
        if (!perfumeDao.existsById(id)) {
            throw new ResourceNotFoundException("Perfume not found with id: " + id);
        }

        Brand brand = getBrandEntity(perfumeRequest.getBrandId());
        Category category = getCategoryEntity(perfumeRequest.getCategoryId());

        Perfume perfume = perfumeMapper.toEntity(perfumeRequest, brand, category);
        perfume.setId(id);
        Perfume updatedPerfume = perfumeDao.save(perfume);

        return perfumeMapper.toResponse(updatedPerfume);
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