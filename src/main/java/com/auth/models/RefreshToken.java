package com.auth.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter @Setter
public class RefreshToken {

    @Id @GeneratedValue
    private Long id;

    private Long userId;

    @Column(unique = true, nullable = false)
    private String token;

    private Instant expiry;

    private boolean revoked;

    private String replacedByToken; // 🔑 rotation chain
}
