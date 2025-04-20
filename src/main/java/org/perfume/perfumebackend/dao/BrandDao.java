package org.perfume.perfumebackend.dao;

import org.perfume.perfumebackend.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandDao extends JpaRepository<Brand, Long> {
}