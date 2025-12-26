package com.auth.utils;

import com.auth.models.Permission;
import com.auth.models.Role;
import com.auth.models.User;
import com.auth.models.UserEvent;

import java.time.Instant;
import java.util.stream.Collectors;

public class UserEventBuilder {

    public static UserEvent created(User user) {
        return base(user, "USER_CREATED");
    }

    public static UserEvent updated(User user) {
        return base(user, "USER_UPDATED");
    }

    public static UserEvent authChanged(User user) {
        return base(user, "USER_AUTH_CHANGED");
    }

    public static UserEvent statusChanged(User user) {
        return base(user, "USER_STATUS_CHANGED");
    }

    private static UserEvent base(User user, String type) {
        UserEvent e = new UserEvent();
        e.setEventType(type);
        e.setUserId(user.getId());
        e.setTenantId(user.getTenantId());
        e.setFullName(user.getFullName());
        e.setProfileImageUrl(user.getProfileImageUrl());
        e.setStatus(user.getStatus().name());
        e.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        e.setPermissions(user.getPermissions().stream().map(Permission::getName).collect(Collectors.toSet()));
        e.setOccurredAt(Instant.now());
        return e;
    }
}

