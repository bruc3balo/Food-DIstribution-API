package com.api.fooddistribution.api.repository;

import com.api.fooddistribution.api.domain.Models;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AppPermissionRepo extends JpaRepository<Models.Permissions, Long>, JpaSpecificationExecutor<Models.Permissions> {
    Models.Permissions findByName(String permissionName);
}
