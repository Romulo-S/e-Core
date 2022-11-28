package com.launchersoft.roleapi.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.launchersoft.roleapi.model.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {

    Page<Roles> findAll(Pageable pageable);

    Optional<Roles> findById(UUID uuid);
}
