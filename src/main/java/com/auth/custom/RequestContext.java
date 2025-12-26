package com.auth.custom;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@Component
@RequestScope
@Getter
public class RequestContext {
    private final Long userId;
    private final Long tenantId;
    private final List<String> roles;

    public RequestContext(HttpServletRequest req) {
        this.userId = Long.valueOf(req.getHeader("X-User-Id"));
        this.tenantId = Long.valueOf(req.getHeader("X-Tenant-Id"));
        this.roles = List.of(req.getHeader("X-User-Roles").split(","));
    }
}

