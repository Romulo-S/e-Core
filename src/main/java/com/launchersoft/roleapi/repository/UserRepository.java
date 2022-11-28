package com.launchersoft.roleapi.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.launchersoft.roleapi.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Page<User> findAll(Pageable pageable);

    Optional<User> findById(UUID uuid);
}
