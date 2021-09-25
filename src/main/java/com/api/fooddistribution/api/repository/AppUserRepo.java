package com.api.fooddistribution.api.repository;

import com.api.fooddistribution.api.domain.Models;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepo extends JpaRepository<Models.AppUser, Long>, JpaSpecificationExecutor<Models.AppUser> {
    Optional<Models.AppUser> findByUsername(String username);
}
