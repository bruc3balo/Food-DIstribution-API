package com.api.fooddistribution.api.repository;

import com.api.fooddistribution.api.domain.Models;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductCategoryRepo extends JpaRepository<Models.ProductCategory, Long>, JpaSpecificationExecutor<Models.ProductCategory> {
    Optional<Models.ProductCategory> findByName(String name);
}
