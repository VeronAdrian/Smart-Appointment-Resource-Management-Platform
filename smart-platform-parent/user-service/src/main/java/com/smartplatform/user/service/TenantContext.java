package com.smartplatform.user.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class TenantContext {
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public static String getCurrentTenantId() {
        String tenantId = currentTenant.get();
        if (tenantId == null) {
            // Try to get tenant from logged-in user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String)) {
                var user = UserService.findByUsername(auth.getName());
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

    public static void setCurrentTenantId(String tenantId) {
        currentTenant.set(tenantId);
    }

    public static void clear() {
        currentTenant.remove();
    }
}

