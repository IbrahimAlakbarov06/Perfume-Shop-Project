package org.perfume.mapper;

<<<<<<< HEAD
import org.perfume.dto.request.CategoryRequest;
import org.perfume.dto.response.CategoryResponse;
import org.perfume.entity.Category;
=======
import org.perfume.model.dto.response.CategoryDto;
import org.perfume.domain.entity.Category;
>>>>>>> 37e4850 (packages updated)
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {
        if (category == null) {
            return null;
        }

        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());

        return response;
    }

    public Category toEntity(CategoryRequest request) {
        if (request == null) {
            return null;
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        return category;
    }
}