package com.auth.service;

import com.auth.models.RefreshToken;
import com.auth.repo.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repo;

    public RefreshToken create(Long userId) {
        RefreshToken rt = new RefreshToken();
        rt.setUserId(userId);
        rt.setToken(UUID.randomUUID().toString());
        rt.setExpiry(Instant.now().plus(30, ChronoUnit.DAYS));
        rt.setRevoked(false);
        return repo.save(rt);
    }

    @Transactional
    public RefreshToken rotate(String oldToken) {
        RefreshToken existing = repo.findByToken(oldToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (existing.isRevoked() || existing.getExpiry().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired/revoked");
        }

        existing.setRevoked(true);

        RefreshToken next = create(existing.getUserId());
        existing.setReplacedByToken(next.getToken());

        repo.save(existing);
        return next;
    }
}

