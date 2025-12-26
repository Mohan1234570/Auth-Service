package com.auth.models;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
public class UserEvent {

    private String eventType;        // USER_CREATED, USER_UPDATED
    private Long userId;
    private Long tenantId;

    private String fullName;
    private String profileImageUrl;
    private String status;

    private Set<String> roles;
    private Set<String> permissions;

    private Instant occurredAt;
    private String version = "v1";
}

