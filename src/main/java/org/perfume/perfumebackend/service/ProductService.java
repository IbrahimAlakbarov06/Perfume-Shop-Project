package org.perfume.perfumebackend.service;

import org.perfume.perfumebackend.dao.ProductDao;
import org.perfume.perfumebackend.dto.ProductDto;
import org.perfume.perfumebackend.entity.Product;
import org.perfume.perfumebackend.exception.ResourceNotFoundException;
import org.perfume.perfumebackend.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    ProductDao productDao;
    ProductMapper productMapper;

//    public ProductService(ProductDao productDao, ProductMapper productMapper) {
//        this.productDao = productDao;
//        this.productMapper = productMapper;
//    }

    public List<ProductDto> findAll(int page, int size) {
        List<Product> products = productDao.findAll()
                .stream()
                .skip(page * size)
                .limit(size)
                .collect(Collectors.toList());

        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    public ProductDto findById(Long id) {
        return productDao.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public List<ProductDto> searchProducts(String name, String brandName, String categoryName, int page, int size) {
        return productDao.findAll().stream()
                .filter(product ->
                        (name == null || product.getName().toLowerCase().contains(name.toLowerCase())) &&
                                (brandName == null || product.getBrandName().equalsIgnoreCase(brandName)) &&
                                (categoryName == null || product.getCategoryName().equalsIgnoreCase(categoryName))
                )
                .skip(page * size)
                .limit(size)
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<String> findAllCategories() {
        return productDao.findAll().stream()
                .map(Product::getCategoryName)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> findAllBrands() {
        return productDao.findAll().stream()
                .map(Product::getBrandName)
                .distinct()
                .collect(Collectors.toList());
    }

    public ProductDto save(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        Product savedProduct = productDao.save(product);
        return productMapper.toDto(savedProduct);
    }

    public ProductDto update(ProductDto productDto) {
        findById(productDto.getId());

        Product product = productMapper.toEntity(productDto);
        productDao.update(product);
        return productDto;
    }

    public void deleteById(Long id) {
        findById(id);
        productDao.deleteById(id);
    }
}