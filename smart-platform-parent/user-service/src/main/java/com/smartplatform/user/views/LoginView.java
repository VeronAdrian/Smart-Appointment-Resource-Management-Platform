package com.smartplatform.user.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("login")
public class LoginView extends Div {
    public LoginView() {
        setText("Please use the Spring Security login form.");
    }
}
