package com.smartplatform.user.views;

import com.smartplatform.user.model.Role;
import com.smartplatform.user.service.TenantService;
import com.smartplatform.user.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import java.util.Set;

@Route("register")
public class RegistrationView extends VerticalLayout {
    public RegistrationView(UserService userService) {
        TextField username = new TextField("Username");
        PasswordField password = new PasswordField("Password");
        EmailField email = new EmailField("Email");
        Button register =
                new Button(
                        "Register",
                        e -> {
                            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                                Notification.show("All fields are required");
                                return;
                            }
                            String tenantId =
                                    TenantService.getDefaultTenant() != null
                                            ? TenantService.getDefaultTenant().getId()
                                            : null;
                            boolean success =
                                    userService.register(
                                            username.getValue().trim(),
                                            password.getValue(),
                                            email.getValue().trim(),
                                            Set.of(Role.CLIENT),
                                            tenantId);
                            if (success) {
                                Notification.show(
                                        "Successfully registered: " + username.getValue());
                                username.clear();
                                password.clear();
                                email.clear();
                            } else {
                                Notification.show("Username or email already exists");
                            }
                        });
        add(username, password, email, register);
    }
}
