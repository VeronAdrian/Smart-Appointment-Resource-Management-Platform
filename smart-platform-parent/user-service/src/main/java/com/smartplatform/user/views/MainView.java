package com.smartplatform.user.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.H1;

@Route("") // root URL
public class MainView extends VerticalLayout {
    public MainView() {
        add(new H1("Welcome to Smart Appointment & Resource Management Platform"));
        add(new Button("Login", e -> getUI().ifPresent(ui -> ui.navigate("login"))));
        add(new Button("Dashboard", e -> getUI().ifPresent(ui -> ui.navigate("dashboard"))));
    }
}
