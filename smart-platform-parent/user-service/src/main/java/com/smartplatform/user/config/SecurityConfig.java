package com.smartplatform.user.config;

import com.smartplatform.user.model.Role;
import com.smartplatform.user.model.User;
import com.smartplatform.user.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import java.util.stream.Collectors;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = UserService.findByUsername(username);
            if (user == null) throw new UsernameNotFoundException("User not found");
            return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                    .password(user.getPassword())
                    .authorities(
                        user.getRoles().stream()
                            .map(Enum::name)
                            .toArray(String[]::new)
                    ).build();
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/analytics/**").hasAnyAuthority("ADMIN", "SUPER_ADMIN")
                .requestMatchers("/analytics-dashboard.html").hasAnyAuthority("ADMIN", "SUPER_ADMIN")
                .requestMatchers("/static/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(withDefaults())
            .logout(logout -> logout.permitAll());
        return http.build();
    }
    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // For demo only! Never use in production
    }
}
