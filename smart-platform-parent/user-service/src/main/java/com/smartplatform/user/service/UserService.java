package com.smartplatform.user.service;

import com.smartplatform.user.model.Role;
import com.smartplatform.user.model.User;
import java.util.*;

public class UserService {
    private static final List<User> users = new ArrayList<>();
    static {
        preloadSampleUsers();
    }
    public static boolean register(String username, String password, String email, Set<Role> roles) {
        if (findByUsername(username) != null || findByEmail(email) != null) {
            return false;
        }
        users.add(new User(username, password, email, roles));
        return true;
    }
    public static User login(String username, String password) {
        User user = findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
    public static User findByUsername(String username) {
        return users.stream().filter(u -> u.getUsername().equalsIgnoreCase(username)).findFirst().orElse(null);
    }
    public static User findByEmail(String email) {
        return users.stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst().orElse(null);
    }
    public static void preloadSampleUsers() {
        users.clear();
        users.add(new User("admin", "admin", "admin@example.com", Set.of(Role.ADMIN)));
        users.add(new User("staff", "staff", "staff@example.com", Set.of(Role.STAFF)));
        users.add(new User("client", "client", "client@example.com", Set.of(Role.CLIENT)));
        users.add(new User("super", "super", "super@example.com", Set.of(Role.SUPER_ADMIN)));
    }
}
