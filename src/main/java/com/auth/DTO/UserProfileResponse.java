package com.auth.DTO;

import com.auth.models.Permission;
import com.auth.models.Role;
import com.auth.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class UserProfileResponse {

    private Long id;
    private String email;
    private String fullName;
    private String profileImageUrl;
    private Long tenantId;
    private Set<String> roles;
    private Set<String> permissions;

    public static UserProfileResponse from(User u) {
        return new UserProfileResponse(
                u.getId(),
                u.getEmail(),
                u.getFullName(),
                u.getProfileImageUrl(),
                u.getTenantId(),
                u.getRoles().stream().map(Role::getName).collect(Collectors.toSet()),
                u.getPermissions().stream().map(Permission::getName).collect(Collectors.toSet())
        );
    }
}

