package com.auth.DTO;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private Long tenantId;
    public String fullName;
}
