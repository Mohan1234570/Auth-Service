package com.auth.utils;

import com.auth.models.Role;
import com.auth.repo.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityBootstrap implements ApplicationRunner {

    private final RoleRepository roleRepo;

    @Override
    public void run(ApplicationArguments args) {

        ensureRole("USER");
        ensureRole("ADMIN");
        ensureRole("SUPPORT");
    }

    private void ensureRole(String name) {
        roleRepo.findByName(name)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(name);   // REQUIRED
                    return roleRepo.save(role);
                });
    }


}
