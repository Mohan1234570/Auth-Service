package com.auth.custom;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class RoleCheckFilter
        extends AbstractGatewayFilterFactory<RoleCheckFilter.Config> {

    public RoleCheckFilter() {
        super(Config.class);
    }

    public static class Config {
        private String requiredRole;

        public String getRequiredRole() {
            return requiredRole;
        }

        public void setRequiredRole(String requiredRole) {
            this.requiredRole = requiredRole;
        }
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String rolesHeader =
                    exchange.getRequest().getHeaders().getFirst("X-User-Roles");

            if (rolesHeader == null ||
                    !rolesHeader.contains(config.getRequiredRole())) {

                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        };
    }
}
