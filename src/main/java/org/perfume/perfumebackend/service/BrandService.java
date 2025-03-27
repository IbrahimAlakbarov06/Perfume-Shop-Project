package org.perfume.perfumebackend.service;

import org.perfume.perfumebackend.dao.BrandDao;
import org.perfume.perfumebackend.dao.PerfumeDao;
import org.perfume.perfumebackend.dto.BrandDto;
import org.perfume.perfumebackend.dto.PerfumeDto;
import org.perfume.perfumebackend.entity.Brand;
import org.perfume.perfumebackend.entity.Perfume;
import org.perfume.perfumebackend.exception.ResourceNotFoundException;
import org.perfume.perfumebackend.mapper.BrandMapper;
import org.perfume.perfumebackend.mapper.PerfumeMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandService {

    private final BrandDao brandDao;
    private final PerfumeDao perfumeDao;
    private final BrandMapper brandMapper;
    private final PerfumeMapper perfumeMapper;

    public BrandService(BrandDao brandDao, PerfumeDao perfumeDao, BrandMapper brandMapper, PerfumeMapper perfumeMapper) {
        this.brandDao = brandDao;
        this.perfumeDao = perfumeDao;
        this.brandMapper = brandMapper;
        this.perfumeMapper = perfumeMapper;
    }

    public List<BrandDto> findAll(int page, int size) {
        List<Brand> brands = brandDao.findAll()
                .stream()
                .skip(page * size)
                .limit(size)
                .collect(Collectors.toList());

        return brands.stream()
                .map(brandMapper::toDto)
                .collect(Collectors.toList());
    }

    public BrandDto findById(Long id) {
        return brandDao.findById(id)
                .map(brandMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));
    }

    public List<PerfumeDto> findPerfumesByBrand(Long brandId, int page, int size) {
        findById(brandId);

        return perfumeDao.findAll().stream()
                .filter(perfume -> perfume.getBrandName().equals(findById(brandId).getName()))
                .skip(page * size)
                .limit(size)
                .map(perfumeMapper::toDto)
                .collect(Collectors.toList());
    }

    public BrandDto save(BrandDto brandDto) {
        Brand brand = brandMapper.toEntity(brandDto);
        Brand savedBrand = brandDao.save(brand);
        return brandMapper.toDto(savedBrand);
    }

    public BrandDto update(BrandDto brandDto) {
        findById(brandDto.getId());

        Brand brand = brandMapper.toEntity(brandDto);
        brandDao.update(brand);
        return brandDto;
    }

    public void deleteById(Long id) {
        findById(id);
        brandDao.deleteById(id);
    }

    public void updateBrandLogo(Long brandId, String logoUrl) {
        BrandDto existingBrand = findById(brandId);

        existingBrand.setLogoUrl(logoUrl);

        update(existingBrand);
    }
}