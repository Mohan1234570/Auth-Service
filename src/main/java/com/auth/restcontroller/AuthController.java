package com.auth.restcontroller;

import com.auth.DTO.AuthResponse;
import com.auth.DTO.LoginRequest;
import com.auth.DTO.RegisterRequest;
import com.auth.DTO.UserProfileResponse;
import com.auth.models.AccountStatus;
import com.auth.models.RefreshToken;
import com.auth.models.Role;
import com.auth.models.User;
import com.auth.repo.RoleRepository;
import com.auth.repo.UserRepository;
import com.auth.service.RefreshTokenService;
import com.auth.utils.JwtTokenProvider;
import com.auth.utils.UserEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserEventPublisher userEventPublisher;


    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest req,
            @RequestHeader("X-Tenant-Id") Long tenantId
    ) {

        if (userRepo.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email exists");
        }

        User user = new User();
        user.setEmail(req.getEmail());
        user.setFullName(req.getFullName());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setTenantId(tenantId);
        user.setPlan("FREE");

        // ASSIGN DEFAULT ROLE
        Role userRole = roleRepo.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default role USER not found"));

        user.setRoles(Set.of(userRole));
        User saved = userRepo.save(user);
        userEventPublisher.publishUserCreated(saved);

        return ResponseEntity.ok("Registered");
    }



    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {

        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid"));

        if (!encoder.matches(req.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid");

        String token = tokenProvider.generate(user);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@CookieValue("refresh_token") String token) {

        RefreshToken newToken = refreshTokenService.rotate(token);

        User user = userRepo.findById(newToken.getUserId()).orElseThrow();

        String jwt = tokenProvider.generate(user);

        return new AuthResponse(jwt);
    }

    @GetMapping("/me")
    public UserProfileResponse me(
            @RequestHeader("X-User-Id") Long userId) {

        User user = userRepo.findById(userId)
                .orElseThrow();

        return UserProfileResponse.from(user);
    }

    @PostMapping("/admin/users/{id}/roles")
    public void assignRoles(
            @PathVariable Long id,
            @RequestBody Set<String> roles) {

        User user = userRepo.findById(id).orElseThrow();

        Set<Role> roleEntities = roleRepo.findByNameIn(roles);
        user.setRoles(roleEntities);

        userRepo.save(user);
    }

    @PostMapping("/admin/users/{id}/lock")
    public void lock(@PathVariable Long id) {
        User user = userRepo.findById(id).orElseThrow();
        user.setStatus(AccountStatus.LOCKED);
        userRepo.save(user);
    }


}

