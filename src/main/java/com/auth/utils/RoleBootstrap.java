package com.auth.utils;

import com.auth.models.Role;
import com.auth.repo.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RoleBootstrap {

    private final RoleRepository roleRepo;

    @PostConstruct
    @Transactional
    public void initRoles() {
        createIfMissing("USER");
        createIfMissing("ADMIN");
    }

    private void createIfMissing(String name) {
        if (!roleRepo.existsByName(name)) {
            Role role = new Role();
            role.setName(name);
            roleRepo.save(role);
        }
    }
}

