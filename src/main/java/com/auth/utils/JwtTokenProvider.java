package com.auth.utils;

import com.auth.models.Permission;
import com.auth.models.Role;
import com.auth.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;


    public JwtTokenProvider() {
        try {
            this.privateKey = RsaKeyUtil.loadPrivateKey("keys/private.pem");
            this.publicKey = RsaKeyUtil.loadPublicKey("keys/public.pem");
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to load RSA keys. Check src/main/resources/keys/",
                    e
            );
        }
    }

    public String generate(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("tenantId", user.getTenantId())
                .claim("roles",
                        user.getRoles().stream()
                                .map(Role::getName)
                                .toList()
                )
                .claim("permissions",
                        user.getPermissions().stream()
                                .map(Permission::getName)
                                .toList()
                )
                .claim("plan", user.getPlan())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }
}

