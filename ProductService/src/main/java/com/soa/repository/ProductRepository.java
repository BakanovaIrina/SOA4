package com.soa.repository;

import com.soa.entity.ProductEntity;
import com.soa.model.UnitOfMeasure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    List<ProductEntity> findByUnitOfMeasure(UnitOfMeasure unitOfMeasure);
    Optional<ProductEntity> findTopByOrderByPriceAsc();
    int countByManufactureCostGreaterThan(int cost);
}

