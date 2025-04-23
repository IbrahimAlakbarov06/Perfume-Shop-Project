package org.perfume.perfumebackend.service;

import lombok.RequiredArgsConstructor;
import org.perfume.perfumebackend.dao.CategoryDao;
import org.perfume.perfumebackend.dto.CategoryDto;
import org.perfume.perfumebackend.entity.Category;
import org.perfume.perfumebackend.exception.ResourceNotFoundException;
import org.perfume.perfumebackend.mapper.CategoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryDao categoryDao;
    private final CategoryMapper categoryMapper;

    public List<CategoryDto> getAllCategories() {
        return categoryDao.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long id) {
        Category category = categoryDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        return categoryMapper.toDto(category);
    }

    public CategoryDto getCategoryByName(String name) {
        Category category = categoryDao.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + name));

        return categoryMapper.toDto(category);
    }

    public List<CategoryDto> searchCategories(String searchTerm) {
        return categoryDao.findByNameContainingIgnoreCase(searchTerm).stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        if (categoryDao.existsByNameIgnoreCase(categoryDto.getName())) {
            throw new IllegalArgumentException("Category with name '" + categoryDto.getName() + "' already exists");
        }

        Category category = categoryMapper.toEntity(categoryDto);
        Category savedCategory = categoryDao.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category existingCategory = categoryDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (!existingCategory.getName().equalsIgnoreCase(categoryDto.getName()) &&
                categoryDao.existsByNameIgnoreCase(categoryDto.getName())) {
            throw new IllegalArgumentException("Category with name '" + categoryDto.getName() + "' already exists");
        }

        existingCategory.setName(categoryDto.getName());
        existingCategory.setDescription(categoryDto.getDescription());

        Category updatedCategory = categoryDao.save(existingCategory);
        return categoryMapper.toDto(updatedCategory);
    }

    @Transactional
    public void deleteCategoryById(Long id) {
        if (!categoryDao.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryDao.deleteById(id);
    }
}
