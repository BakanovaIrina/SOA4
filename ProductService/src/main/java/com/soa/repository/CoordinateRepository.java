package com.soa.repository;

import com.soa.entity.CoordinatesEntity;
import com.soa.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordinateRepository extends JpaRepository<CoordinatesEntity, Integer> {
}
