package com.smartplatform.user.views;

import com.smartplatform.user.model.ResourceReservation;
import com.smartplatform.user.service.ResourceService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("resource-approval")
public class ResourceApprovalView extends VerticalLayout implements BeforeEnterObserver {
    public ResourceApprovalView() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || 
            (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN")) &&
             auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("STAFF")))) {
            return;
        }
        String approverName = auth.getName();
        add(new H2("Resource Reservation Approval - " + approverName));
        refreshPendingReservations(approverName);
    }
    private void refreshPendingReservations(String approverName) {
        removeAll();
        add(new H2("Resource Reservation Approval - " + approverName));
        List<ResourceReservation> pendingReservations = ResourceService.getPendingReservations();
        if (pendingReservations.isEmpty()) {
            add(new H3("No pending reservations."));
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            pendingReservations.forEach(reservation -> {
                VerticalLayout card = new VerticalLayout();
                card.add(new com.vaadin.flow.component.html.Span(
                    "Resource: " + reservation.getResourceName() + " | " +
                    "User: " + reservation.getUserName() + " | " +
                    "Priority: " + reservation.getPriority() + " | " +
                    "Time: " + reservation.getStartDateTime().format(formatter) + " - " + 
                    reservation.getEndDateTime().format(formatter)
                ));
                HorizontalLayout buttonLayout = new HorizontalLayout();
                Button approveButton = new Button("Approve", e -> {
                    if (ResourceService.approveReservation(reservation.getId(), approverName)) {
                        Notification.show("Reservation approved! Check console for email notification.");
                        refreshPendingReservations(approverName);
                    }
                });
                TextArea rejectReason = new TextArea("Rejection Reason");
                rejectReason.setWidth("300px");
                Button rejectButton = new Button("Reject", e -> {
                    if (rejectReason.isEmpty()) {
                        Notification.show("Please provide a rejection reason");
                        return;
                    }
                    if (ResourceService.rejectReservation(reservation.getId(), approverName, rejectReason.getValue())) {
                        Notification.show("Reservation rejected! Check console for email notification.");
                        refreshPendingReservations(approverName);
                    }
                });
                buttonLayout.add(approveButton, rejectReason, rejectButton);
                card.add(buttonLayout);
                add(card);
            });
        }
    }
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || 
            (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN")) &&
             auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("STAFF")))) {
            event.forwardTo("");
        }
    }
}

