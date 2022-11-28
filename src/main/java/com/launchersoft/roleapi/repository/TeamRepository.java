package com.launchersoft.roleapi.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.launchersoft.roleapi.model.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Page<Team> findAll(Pageable pageable);

    Optional<Team> findById(UUID uuid);

}

