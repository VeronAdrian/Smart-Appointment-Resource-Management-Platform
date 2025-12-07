package com.smartplatform.user.repository;

import com.smartplatform.user.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByTenantId(String tenantId);

    // Custom query to handle tenant isolation logic if needed,
    // but standard methods + service logic might be cleaner.
    // For now, standard methods are sufficient as service handles logic.
}
