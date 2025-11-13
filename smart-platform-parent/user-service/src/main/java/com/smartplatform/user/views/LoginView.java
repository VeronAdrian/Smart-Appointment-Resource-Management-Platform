package com.smartplatform.user.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("login")
public class LoginView extends VerticalLayout {
    public LoginView() {
        TextField username = new TextField("Username");
        PasswordField password = new PasswordField("Password");
        Button login = new Button("Login", e -> {
            // mock, skip real auth
            if (!username.getValue().isEmpty()) {
                getUI().ifPresent(ui -> ui.navigate("dashboard"));
            } else {
                Notification.show("Enter a username");
            }
        });
        Button registerBtn = new Button("Register", e -> getUI().ifPresent(ui -> ui.navigate("register")));
        add(username, password, login, registerBtn);
    }
}
