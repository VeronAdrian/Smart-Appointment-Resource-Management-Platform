package com.smartplatform.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TenantContext {
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();
    private final UserService userService;

    public String getCurrentTenantId() {
        String tenantId = currentTenant.get();
        if (tenantId == null) {
            // Try to get tenant from logged-in user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null
                    && auth.isAuthenticated()
                    && !(auth.getPrincipal() instanceof String)) {
                var user = userService.findByUsername(auth.getName());
                if (user != null && user.getTenantId() != null) {
                    return user.getTenantId();
                }
            }
            // Default to first tenant if no user context
            var defaultTenant = TenantService.getDefaultTenant();
            return defaultTenant != null ? defaultTenant.getId() : null;
        }
        return tenantId;
    }

    public void setCurrentTenantId(String tenantId) {
        currentTenant.set(tenantId);
    }

    public void clear() {
        currentTenant.remove();
    }
}
