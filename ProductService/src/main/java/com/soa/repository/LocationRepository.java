package com.soa.repository;

import com.soa.entity.LocationEntity;
import com.soa.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<LocationEntity, Integer> {
}
