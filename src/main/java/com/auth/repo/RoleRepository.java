package com.auth.repo;

import com.auth.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Set<Role> findByNameIn(Set<String> roles);

    Optional<Role> findByName(String user);

    boolean existsByName(String name);
}
