package com.soa.repository;

import com.soa.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<PersonEntity, Integer> {
}
