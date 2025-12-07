package com.smartplatform.user.service;

import com.smartplatform.user.model.Role;
import com.smartplatform.user.model.User;
import com.smartplatform.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing users. Refactored to use Spring Data JPA for persistence and dependency
 * injection. Replaces static in-memory storage with a database-backed repository.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public boolean register(
            String username, String password, String email, Set<Role> roles, String tenantId) {
        if (userRepository.findByUsername(username).isPresent()
                || userRepository.findByEmail(email).isPresent()) {
            return false;
        }
        userRepository.save(new User(username, password, email, roles, tenantId));
        return true;
    }

    public User login(String username, String password) {
        // In a real app, use Spring Security. For now, simple check.
        return userRepository
                .findByUsername(username)
                .filter(u -> u.getPassword().equals(password))
                .orElse(null);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public List<User> getUsersByTenant(String tenantId) {
        return userRepository.findByTenantId(tenantId);
    }

    @PostConstruct
    public void preloadSampleUsers() {
        if (userRepository.count() == 0) {
            var defaultTenant = TenantService.getDefaultTenant();
            String defaultTenantId = defaultTenant != null ? defaultTenant.getId() : null;

            userRepository.save(
                    new User(
                            "admin",
                            "admin",
                            "admin@example.com",
                            Set.of(Role.ADMIN),
                            defaultTenantId));
            userRepository.save(
                    new User(
                            "staff",
                            "staff",
                            "staff@example.com",
                            Set.of(Role.STAFF),
                            defaultTenantId));
            userRepository.save(
                    new User(
                            "client",
                            "client",
                            "client@example.com",
                            Set.of(Role.CLIENT),
                            defaultTenantId));
            userRepository.save(
                    new User(
                            "super", "super", "super@example.com", Set.of(Role.SUPER_ADMIN), null));
        }
    }
}
