package com.smartplatform.user.service;

import com.smartplatform.user.model.Tenant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TenantService {
    private static final List<Tenant> tenants = new ArrayList<>();

    static {
        // Create default tenant for demo
        Tenant defaultTenant = new Tenant("Default Organization", "default", "admin@default.com");
        tenants.add(defaultTenant);
    }

    public static Tenant createTenant(String name, String domain, String contactEmail) {
        if (findByDomain(domain) != null) {
            return null; // Domain already exists
        }
        Tenant tenant = new Tenant(name, domain, contactEmail);
        tenants.add(tenant);
        return tenant;
    }

    public static List<Tenant> getAllTenants() {
        return new ArrayList<>(tenants);
    }

    public static Tenant findTenantById(String tenantId) {
        return tenants.stream()
                .filter(t -> t.getId().equals(tenantId))
                .findFirst()
                .orElse(null);
    }

    public static Tenant findByDomain(String domain) {
        return tenants.stream()
                .filter(t -> t.getDomain().equalsIgnoreCase(domain))
                .findFirst()
                .orElse(null);
    }

    public static boolean updateTenantStatus(String tenantId, String status) {
        Tenant tenant = findTenantById(tenantId);
        if (tenant != null) {
            tenant.setStatus(status);
            return true;
        }
        return false;
    }

    public static boolean deleteTenant(String tenantId) {
        return tenants.removeIf(t -> t.getId().equals(tenantId));
    }

    public static Tenant getDefaultTenant() {
        return tenants.isEmpty() ? null : tenants.get(0);
    }
}

