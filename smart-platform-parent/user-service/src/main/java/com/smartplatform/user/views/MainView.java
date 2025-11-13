package com.smartplatform.user.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("") // root URL
public class MainView extends VerticalLayout {
    public MainView() {
        add(new H1("Welcome to Smart Appointment & Resource Management Platform"));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String && auth.getPrincipal().equals("anonymousUser"));
        if (isAuthenticated) {
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                add(new Button("Admin Dashboard", e -> getUI().ifPresent(ui -> ui.navigate("admin-dashboard"))));
            }
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("STAFF"))) {
                add(new Button("Staff Dashboard", e -> getUI().ifPresent(ui -> ui.navigate("staff-dashboard"))));
            }
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("CLIENT"))) {
                add(new Button("Client Dashboard", e -> getUI().ifPresent(ui -> ui.navigate("client-dashboard"))));
            }
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("SUPER_ADMIN"))) {
                add(new Button("Super Admin Dashboard", e -> getUI().ifPresent(ui -> ui.navigate("super-dashboard"))));
            }
        } else {
            add(new Button("Login", e -> getUI().ifPresent(ui -> ui.navigate("login"))));
            add(new Button("Register", e -> getUI().ifPresent(ui -> ui.navigate("register"))));
        }
    }
}
