package com.smartplatform.user.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("super-dashboard")
public class SuperDashboardView extends VerticalLayout implements BeforeEnterObserver {
    public SuperDashboardView() {
        add(new H2("Super Admin Dashboard: Welcome, Super Admin!"));
        add(new Button("Tenant Management", e -> getUI().ifPresent(ui -> ui.navigate("tenant-management"))));
        add(new Button("Resource Analytics", e -> getUI().ifPresent(ui -> ui.navigate("resource-analytics"))));
        add(new Button("Analytics Dashboard", e -> getUI().ifPresent(ui -> ui.navigate("analytics-dashboard"))));
    }
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("SUPER_ADMIN"))) {
            event.forwardTo(""); // redirect to main
        }
    }
}
