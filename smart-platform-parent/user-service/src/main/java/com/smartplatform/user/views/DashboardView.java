package com.smartplatform.user.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("dashboard")
public class DashboardView extends VerticalLayout {
    public DashboardView() {
        add(new H2("Dashboard: Hello, user!"));
        add(new Button("Logout", e -> getUI().ifPresent(ui -> ui.navigate("login"))));
    }
}
