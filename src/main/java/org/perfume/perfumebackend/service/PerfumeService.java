package org.perfume.perfumebackend.service;

import org.perfume.perfumebackend.dao.PerfumeDao;
import org.perfume.perfumebackend.dto.PerfumeDto;
import org.perfume.perfumebackend.entity.Perfume;
import org.perfume.perfumebackend.exception.ResourceNotFoundException;
import org.perfume.perfumebackend.mapper.PerfumeMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PerfumeService {

    private final PerfumeDao perfumeDao;
    private final PerfumeMapper perfumeMapper;

    public PerfumeService(PerfumeDao perfumeDao, PerfumeMapper perfumeMapper) {
        this.perfumeDao = perfumeDao;
        this.perfumeMapper = perfumeMapper;
    }

    public List<PerfumeDto> findAll(int page, int size) {
        List<Perfume> perfumes = perfumeDao.findAll()
                .stream()
                .skip(page * size)
                .limit(size)
                .collect(Collectors.toList());

        return perfumes.stream()
                .map(perfumeMapper::toDto)
                .collect(Collectors.toList());
    }

    public PerfumeDto findById(Long id) {
        return perfumeDao.findById(id)
                .map(perfumeMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Perfume not found with id: " + id));
    }

    public List<PerfumeDto> searchPerfumes(String name, String brandName, String categoryName, int page, int size) {
        return perfumeDao.findAll().stream()
                .filter(perfume ->
                        (name == null || perfume.getName().toLowerCase().contains(name.toLowerCase())) &&
                                (brandName == null || perfume.getBrandName().equalsIgnoreCase(brandName)) &&
                                (categoryName == null || perfume.getCategoryName().equalsIgnoreCase(categoryName))
                )
                .skip(page * size)
                .limit(size)
                .map(perfumeMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<String> findAllCategories() {
        return perfumeDao.findAll().stream()
                .map(Perfume::getCategoryName)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> findAllBrands() {
        return perfumeDao.findAll().stream()
                .map(Perfume::getBrandName)
                .distinct()
                .collect(Collectors.toList());
    }

    public PerfumeDto save(PerfumeDto perfumeDto) {
        Perfume perfume = perfumeMapper.toEntity(perfumeDto);
        Perfume savedPerfume = perfumeDao.save(perfume);
        return perfumeMapper.toDto(savedPerfume);
    }

    public PerfumeDto update(PerfumeDto perfumeDto) {
        findById(perfumeDto.getId());

        Perfume perfume = perfumeMapper.toEntity(perfumeDto);
        perfumeDao.update(perfume);
        return perfumeDto;
    }

    public void deleteById(Long id) {
        findById(id);
        perfumeDao.deleteById(id);
    }

    public void updatePerfumeImage(Long perfumeId, String imageUrl) {
        // Find the perfume first to ensure it exists
        PerfumeDto existingPerfume = findById(perfumeId);

        // Create a new PerfumeDto with the updated image URL
        existingPerfume.setImageUrl(imageUrl);

        // Update the perfume
        update(existingPerfume);
    }
}