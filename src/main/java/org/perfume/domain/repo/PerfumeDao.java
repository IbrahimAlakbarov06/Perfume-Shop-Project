package org.perfume.domain.repo;

import org.perfume.domain.entity.Perfume;
import org.perfume.model.enums.FragranceFamily;
import org.perfume.model.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PerfumeDao extends JpaRepository<Perfume, Long> {

}

