package com.smartplatform.user.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("staff-dashboard")
public class StaffDashboardView extends VerticalLayout implements BeforeEnterObserver {
    public StaffDashboardView() {
        add(new H2("Staff Dashboard: Welcome, Staff!"));
        add(new Button("Define Availability Slots", e -> getUI().ifPresent(ui -> ui.navigate("staff-availability"))));
    }
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("STAFF"))) {
            event.forwardTo(""); // redirect to main
        }
    }
}
