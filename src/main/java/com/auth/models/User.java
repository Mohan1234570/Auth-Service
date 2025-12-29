package com.auth.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@Table(name = "auth_users")
public class User {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password; // ONLY HERE

    private String fullName;
    private String profileImageUrl;

    private Long tenantId;

    private String plan;


    @Enumerated(EnumType.STRING)
    private AccountStatus status; // ACTIVE, LOCKED, DELETED

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();


    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Permission> permissions;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;
}

