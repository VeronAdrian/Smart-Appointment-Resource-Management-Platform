package com.smartplatform.user.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("register")
public class RegistrationView extends VerticalLayout {
    public RegistrationView() {
        TextField username = new TextField("Username");
        PasswordField password = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Confirm Password");
        EmailField email = new EmailField("Email");
        Button register = new Button("Register", e -> {
            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
                Notification.show("All fields are required.");
                return;
            }
            if (!password.getValue().equals(confirmPassword.getValue())) {
                Notification.show("Passwords do not match.");
                return;
            }
            Notification.show("Registered (mock). Welcome, " + username.getValue() + "!");
        });
        add(username, password, confirmPassword, email, register);
    }
}
