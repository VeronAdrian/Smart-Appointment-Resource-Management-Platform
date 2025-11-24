package com.smartplatform.user.views;

import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("analytics-dashboard")
public class AnalyticsDashboardView extends VerticalLayout implements BeforeEnterObserver {
    public AnalyticsDashboardView() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || 
            (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN")) &&
             auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("SUPER_ADMIN")))) {
            return;
        }
        add(new H2("Analytics Dashboard"));
        IFrame dashboardFrame = new IFrame();
        dashboardFrame.setSrc("/analytics-dashboard.html");
        dashboardFrame.setWidth("100%");
        dashboardFrame.setHeight("1200px");
        dashboardFrame.setStyle("border", "none");
        add(dashboardFrame);
    }
    
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || 
            (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN")) &&
             auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("SUPER_ADMIN")))) {
            event.forwardTo("");
        }
    }
}

