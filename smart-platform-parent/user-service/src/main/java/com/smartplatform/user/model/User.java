package com.smartplatform.user.model;

import java.util.Set;

public class User {
    private String username;
    private String password;
    private String email;
    private Set<Role> roles;
    private String tenantId; // Multi-tenancy: discriminator column

    public User(String username, String password, String email, Set<Role> roles, String tenantId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
        this.tenantId = tenantId;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
}
