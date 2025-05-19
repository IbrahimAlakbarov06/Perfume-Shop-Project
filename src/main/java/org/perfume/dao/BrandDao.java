package org.perfume.dao;

import org.perfume.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandDao extends JpaRepository<Brand, Long> {
    Optional<Brand> findByNameIgnoreCase(String name);

    // Check if a brand exists by name (helpful for validation)
    boolean existsByNameIgnoreCase(String name);

    // Search brands by name containing a string (for search functionality)
    List<Brand> findByNameContainingIgnoreCase(String searchTerm);
}