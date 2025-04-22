package org.perfume.perfumebackend.dao;

import org.perfume.perfumebackend.entity.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PerfumeDao extends JpaRepository<Perfume, Long> {
    List<Perfume> findByBrand_Id(Long brandId);

    // Find perfumes by brand name (case-insensitive)
    List<Perfume> findByBrand_NameIgnoreCase(String brandName);

    // Find perfumes by category
    List<Perfume> findByCategory_Id(Long categoryId);

    // Search by name or description containing term
    List<Perfume> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);

    // Price range queries
    List<Perfume> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Combined filters - by brand and price range
    List<Perfume> findByBrand_NameIgnoreCaseAndPriceBetween(String brandName, BigDecimal minPrice, BigDecimal maxPrice);

    // Find by gender
    List<Perfume> findByGenderIgnoreCase(String gender);

    // Find by fragrance type
    List<Perfume> findByFragranceContainingIgnoreCase(String fragrance);

    // Stock availability
    List<Perfume> findByStockQuantityGreaterThan(Integer minStock);
}

