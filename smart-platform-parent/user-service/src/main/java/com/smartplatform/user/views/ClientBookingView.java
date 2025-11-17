package com.smartplatform.user.views;

import com.smartplatform.user.model.AvailabilitySlot;
import com.smartplatform.user.model.Appointment;
import com.smartplatform.user.service.AppointmentService;
import com.smartplatform.user.service.AvailabilityService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("client-booking")
public class ClientBookingView extends VerticalLayout implements BeforeEnterObserver {
    public ClientBookingView() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("CLIENT"))) {
            return;
        }
        String clientUsername = auth.getName();
        add(new H2("Book Appointment - " + clientUsername));
        refreshAvailableSlots(clientUsername);
    }
    private void refreshAvailableSlots(String clientUsername) {
        removeAll();
        add(new H2("Book Appointment - " + clientUsername));
        List<AvailabilitySlot> availableSlots = AvailabilityService.getAvailableSlots();
        if (availableSlots.isEmpty()) {
            add(new H3("No available slots at the moment. Please check back later."));
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            availableSlots.forEach(slot -> {
                HorizontalLayout slotLayout = new HorizontalLayout();
                slotLayout.add(new com.vaadin.flow.component.html.Span(
                    "Staff: " + slot.getStaffUsername() + 
                    " | " + slot.getStartDateTime().format(formatter) + 
                    " - " + slot.getEndDateTime().format(formatter)
                ));
                Button bookButton = new Button("Book", e -> {
                    Appointment appointment = AppointmentService.bookAppointment(
                        clientUsername,
                        slot.getStaffUsername(),
                        slot.getId(),
                        slot.getStartDateTime()
                    );
                    if (appointment != null) {
                        Notification.show("Appointment booked successfully! Check console for email/SMS notification.");
                        refreshAvailableSlots(clientUsername);
                    } else {
                        Notification.show("Failed to book appointment. Slot may have been taken.");
                        refreshAvailableSlots(clientUsername);
                    }
                });
                slotLayout.add(bookButton);
                add(slotLayout);
            });
        }
        List<Appointment> myAppointments = AppointmentService.getAppointmentsByClient(clientUsername);
        if (!myAppointments.isEmpty()) {
            add(new H3("My Appointments:"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            myAppointments.forEach(apt -> {
                HorizontalLayout aptLayout = new HorizontalLayout();
                aptLayout.add(new com.vaadin.flow.component.html.Span(
                    "Staff: " + apt.getStaffUsername() + 
                    " | " + apt.getAppointmentDateTime().format(formatter) + 
                    " | Status: " + apt.getStatus()
                ));
                if (!apt.getStatus().equals("CANCELLED")) {
                    Button cancelButton = new Button("Cancel", e -> {
                        if (AppointmentService.cancelAppointment(apt.getId())) {
                            Notification.show("Appointment cancelled. Check console for notification.");
                            refreshAvailableSlots(clientUsername);
                        }
                    });
                    aptLayout.add(cancelButton);
                }
                add(aptLayout);
            });
        }
    }
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("CLIENT"))) {
            event.forwardTo("");
        }
    }
}

