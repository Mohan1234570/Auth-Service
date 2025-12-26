package com.auth.restcontroller;

import com.auth.utils.RsaKeyUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;

@RestController
public class JwksController {

    private final RSAKey rsaKey;

    public JwksController() throws Exception {
        PublicKey pub = RsaKeyUtil.loadPublicKey("keys/public.pem");
        this.rsaKey = new RSAKey.Builder((RSAPublicKey) pub)
                .keyID("auth-key")
                .build();
    }

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> jwks() {
        return Map.of("keys",
                List.of(rsaKey.toPublicJWK().toJSONObject()));
    }
}

