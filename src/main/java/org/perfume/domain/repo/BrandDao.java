package org.perfume.domain.repo;

import org.perfume.domain.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandDao extends JpaRepository<Brand, Long> {

}