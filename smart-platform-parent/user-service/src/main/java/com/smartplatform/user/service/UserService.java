package com.smartplatform.user.service;

import com.smartplatform.user.model.Role;
import com.smartplatform.user.model.User;
import java.util.*;

public class UserService {
    private static final List<User> users = new ArrayList<>();
    static {
        preloadSampleUsers();
    }
    public static boolean register(String username, String password, String email, Set<Role> roles, String tenantId) {
        if (findByUsername(username, tenantId) != null || findByEmail(email, tenantId) != null) {
            return false;
        }
        users.add(new User(username, password, email, roles, tenantId));
        return true;
    }
    public static User login(String username, String password) {
        String tenantId = TenantContext.getCurrentTenantId();
        User user = findByUsername(username, tenantId);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
    public static User findByUsername(String username) {
        String tenantId = TenantContext.getCurrentTenantId();
        return findByUsername(username, tenantId);
    }
    public static User findByUsername(String username, String tenantId) {
        return users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .filter(u -> tenantId == null || u.getTenantId() == null || u.getTenantId().equals(tenantId))
                .findFirst()
                .orElse(null);
    }
    public static User findByEmail(String email) {
        String tenantId = TenantContext.getCurrentTenantId();
        return findByEmail(email, tenantId);
    }
    public static User findByEmail(String email, String tenantId) {
        return users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .filter(u -> tenantId == null || u.getTenantId() == null || u.getTenantId().equals(tenantId))
                .findFirst()
                .orElse(null);
    }
    public static List<User> getUsersByTenant(String tenantId) {
        return users.stream()
                .filter(u -> u.getTenantId() != null && u.getTenantId().equals(tenantId))
                .collect(java.util.stream.Collectors.toList());
    }
    public static void preloadSampleUsers() {
        users.clear();
        var defaultTenant = TenantService.getDefaultTenant();
        String defaultTenantId = defaultTenant != null ? defaultTenant.getId() : null;
        users.add(new User("admin", "admin", "admin@example.com", Set.of(Role.ADMIN), defaultTenantId));
        users.add(new User("staff", "staff", "staff@example.com", Set.of(Role.STAFF), defaultTenantId));
        users.add(new User("client", "client", "client@example.com", Set.of(Role.CLIENT), defaultTenantId));
        // Super Admin is tenant-agnostic (can access all tenants)
        users.add(new User("super", "super", "super@example.com", Set.of(Role.SUPER_ADMIN), null));
    }
}
