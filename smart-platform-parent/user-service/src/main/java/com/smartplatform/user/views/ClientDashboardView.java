package com.smartplatform.user.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("client-dashboard")
public class ClientDashboardView extends VerticalLayout implements BeforeEnterObserver {
    public ClientDashboardView() {
        add(new H2("Client Dashboard: Welcome, Client!"));
        add(new Button("Book Appointment", e -> getUI().ifPresent(ui -> ui.navigate("client-booking"))));
    }
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("CLIENT"))) {
            event.forwardTo(""); // redirect to main
        }
    }
}
