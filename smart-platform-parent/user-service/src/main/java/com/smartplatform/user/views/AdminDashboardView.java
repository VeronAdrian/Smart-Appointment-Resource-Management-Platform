package com.smartplatform.user.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("admin-dashboard")
public class AdminDashboardView extends VerticalLayout implements BeforeEnterObserver {
    public AdminDashboardView() {
        add(new H2("Admin Dashboard: Welcome, Admin!"));
        add(new Button("Resource Management", e -> getUI().ifPresent(ui -> ui.navigate("resource-management"))));
        add(new Button("Approve Reservations", e -> getUI().ifPresent(ui -> ui.navigate("resource-approval"))));
        add(new Button("Resource Analytics", e -> getUI().ifPresent(ui -> ui.navigate("resource-analytics"))));
    }
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN"))) {
            event.forwardTo(""); // redirect to main
        }
    }
}
