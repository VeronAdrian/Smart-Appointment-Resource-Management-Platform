package com.smartplatform.user.views;

import com.smartplatform.user.service.AnalyticsService;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

@Route("resource-analytics")
public class ResourceAnalyticsView extends VerticalLayout implements BeforeEnterObserver {
    public ResourceAnalyticsView() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || 
            (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN")) &&
             auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("SUPER_ADMIN")))) {
            return;
        }
        add(new H2("Resource Usage Analytics"));
        Map<String, Object> stats = AnalyticsService.getResourceUsageStats();
        add(new H3("Overview:"));
        add(new com.vaadin.flow.component.html.Span("Total Resources: " + stats.get("totalResources")));
        add(new com.vaadin.flow.component.html.Span("Total Reservations: " + stats.get("totalReservations")));
        add(new com.vaadin.flow.component.html.Span("Approved Reservations: " + stats.get("approvedReservations")));
        add(new com.vaadin.flow.component.html.Span("Pending Reservations: " + stats.get("pendingReservations")));
        add(new com.vaadin.flow.component.html.Span("Active Reservations: " + stats.get("activeReservations")));
        add(new H3("Top 5 Most Used Resources:"));
        @SuppressWarnings("unchecked")
        java.util.List<Map.Entry<String, Long>> topResources = (java.util.List<Map.Entry<String, Long>>) stats.get("topResources");
        if (topResources != null && !topResources.isEmpty()) {
            topResources.forEach(entry -> {
                add(new com.vaadin.flow.component.html.Span(entry.getKey() + ": " + entry.getValue() + " reservations"));
            });
        } else {
            add(new com.vaadin.flow.component.html.Span("No usage data yet."));
        }
        add(new H3("Reservations by Priority:"));
        @SuppressWarnings("unchecked")
        Map<String, Long> priorityStats = (Map<String, Long>) stats.get("reservationsByPriority");
        if (priorityStats != null) {
            priorityStats.forEach((priority, count) -> {
                add(new com.vaadin.flow.component.html.Span(priority + ": " + count));
            });
        }
        add(new H3("Reservations by Status:"));
        @SuppressWarnings("unchecked")
        Map<String, Long> statusStats = (Map<String, Long>) stats.get("reservationsByStatus");
        if (statusStats != null) {
            statusStats.forEach((status, count) -> {
                add(new com.vaadin.flow.component.html.Span(status + ": " + count));
            });
        }
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

