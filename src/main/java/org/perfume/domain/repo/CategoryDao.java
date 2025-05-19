package org.perfume.domain.repo;

import org.perfume.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryDao extends JpaRepository<Category, Long> {
    Optional<Category> findByNameIgnoreCase(String name);

    // Check if a category exists by name
    boolean existsByNameIgnoreCase(String name);

    // Search categories by name containing a string
    List<Category> findByNameContainingIgnoreCase(String searchTerm);
}
