package org.perfume.service;

import lombok.RequiredArgsConstructor;

import org.perfume.domain.repo.CategoryDao;
import org.perfume.model.dto.response.CategoryResponse;
import org.perfume.domain.entity.Category;
import org.perfume.exception.ResourceNotFoundException;
import org.perfume.mapper.CategoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryDao categoryDao;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> getAllCategories() {
        return categoryDao.findAll().stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        return categoryMapper.toResponse(category);
    }

    public CategoryResponse getCategoryByName(String name) {
        Category category = categoryDao.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + name));

        return categoryMapper.toResponse(category);
    }

    public List<CategoryResponse> searchCategories(String searchTerm) {
        return categoryDao.findByNameContainingIgnoreCase(searchTerm).stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        if (categoryDao.existsByNameIgnoreCase(categoryRequest.getName())) {
            throw new IllegalArgumentException("Category with name '" + categoryRequest.getName() + "' already exists");
        }

        Category category = categoryMapper.toEntity(categoryRequest);
        Category savedCategory = categoryDao.save(category);
        return categoryMapper.toResponse(savedCategory);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        Category existingCategory = categoryDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (!existingCategory.getName().equalsIgnoreCase(categoryRequest.getName()) &&
                categoryDao.existsByNameIgnoreCase(categoryRequest.getName())) {
            throw new IllegalArgumentException("Category with name '" + categoryRequest.getName() + "' already exists");
        }

        existingCategory.setName(categoryRequest.getName());
        existingCategory.setDescription(categoryRequest.getDescription());

        Category updatedCategory = categoryDao.save(existingCategory);
        return categoryMapper.toResponse(updatedCategory);
    }

    @Transactional
    public void deleteCategoryById(Long id) {
        if (!categoryDao.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryDao.deleteById(id);
    }
}