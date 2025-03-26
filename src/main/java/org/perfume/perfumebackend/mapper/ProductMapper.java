package org.perfume.perfumebackend.mapper;

import org.perfume.perfumebackend.dto.ProductDto;
import org.perfume.perfumebackend.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }

        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getBrandName(),
                product.getCategoryName(),
                product.getStockQuantity(),
                product.getImageUrl()
        );
    }

    public Product toEntity(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }

        return new Product(
                productDto.getId(),
                productDto.getName(),
                productDto.getDescription(),
                productDto.getPrice(),
                productDto.getBrandName(),
                productDto.getCategoryName(),
                productDto.getStockQuantity(),
                productDto.getImageUrl()
        );
    }
}