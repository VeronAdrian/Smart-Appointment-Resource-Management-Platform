package com.smartplatform.user.views;

import com.smartplatform.user.model.Resource;
import com.smartplatform.user.model.ResourceReservation;
import com.smartplatform.user.service.ResourceService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("resource-reservation")
public class ResourceReservationView extends VerticalLayout implements BeforeEnterObserver {
    private final ResourceService resourceService;

    public ResourceReservationView(ResourceService resourceService) {
        this.resourceService = resourceService;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return;
        }
        String userId = auth.getName();
        add(new H2("Reserve Resource - " + userId));
        List<Resource> availableResources =
                resourceService.getAllResources().stream()
                        .filter(Resource::isAvailable)
                        .collect(Collectors.toList());
        ComboBox<Resource> resourceCombo = new ComboBox<>("Select Resource");
        resourceCombo.setItems(availableResources);
        resourceCombo.setItemLabelGenerator(Resource::getName);
        DatePicker datePicker = new DatePicker("Date");
        IntegerField startHour = new IntegerField("Start Hour (0-23)");
        IntegerField startMinute = new IntegerField("Start Minute");
        IntegerField durationHours = new IntegerField("Duration (hours)");
        durationHours.setValue(1);
        ComboBox<String> priorityCombo = new ComboBox<>("Priority");
        priorityCombo.setItems("LOW", "MEDIUM", "HIGH", "URGENT");
        priorityCombo.setValue("MEDIUM");
        TextArea notesField = new TextArea("Notes (optional)");
        Button reserveButton =
                new Button(
                        "Reserve",
                        e -> {
                            if (resourceCombo.getValue() == null || datePicker.getValue() == null) {
                                Notification.show("Please select resource and date");
                                return;
                            }
                            Resource resource = resourceCombo.getValue();
                            LocalDate date = datePicker.getValue();
                            LocalTime startTime =
                                    LocalTime.of(
                                            startHour.getValue() != null ? startHour.getValue() : 9,
                                            startMinute.getValue() != null
                                                    ? startMinute.getValue()
                                                    : 0);
                            LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
                            LocalDateTime endDateTime =
                                    startDateTime.plusHours(
                                            durationHours.getValue() != null
                                                    ? durationHours.getValue()
                                                    : 1);
                            ResourceReservation reservation =
                                    resourceService.createReservation(
                                            resource.getId(),
                                            userId,
                                            userId,
                                            startDateTime,
                                            endDateTime,
                                            priorityCombo.getValue());
                            if (reservation != null) {
                                Notification.show(
                                        "Reservation created! Pending approval. Check console for"
                                                + " email notification.");
                                refreshMyReservations(userId);
                            } else {
                                Notification.show(
                                        "Failed to create reservation. Conflict detected or invalid"
                                                + " time.");
                            }
                        });
        add(
                resourceCombo,
                datePicker,
                startHour,
                startMinute,
                durationHours,
                priorityCombo,
                notesField,
                reserveButton);
        add(new H3("My Reservations:"));
        refreshMyReservations(userId);
    }

    private void refreshMyReservations(String userId) {
        List<ResourceReservation> reservations = resourceService.getReservationsByUser(userId);
        if (reservations.isEmpty()) {
            add(new com.vaadin.flow.component.html.Span("No reservations yet."));
        } else {
            reservations.forEach(
                    reservation -> {
                        HorizontalLayout layout = new HorizontalLayout();
                        layout.add(
                                new com.vaadin.flow.component.html.Span(
                                        reservation.getResourceName()
                                                + " | "
                                                + reservation.getStartDateTime().toString()
                                                + " - "
                                                + reservation.getEndDateTime().toString()
                                                + " | Priority: "
                                                + reservation.getPriority()
                                                + " | Status: "
                                                + reservation.getStatus()));
                        if (reservation.getStatus().equals("PENDING")
                                || reservation.getStatus().equals("APPROVED")) {
                            Button cancelButton =
                                    new Button(
                                            "Cancel",
                                            e -> {
                                                if (resourceService.cancelReservation(
                                                        reservation.getId())) {
                                                    Notification.show("Reservation cancelled");
                                                    removeAll();
                                                    add(new H2("Reserve Resource - " + userId));
                                                    add(
                                                            new ComboBox<>("Select Resource"),
                                                            new DatePicker("Date"),
                                                            new IntegerField("Start Hour (0-23)"),
                                                            new IntegerField("Start Minute"),
                                                            new IntegerField("Duration (hours)"),
                                                            new ComboBox<>("Priority"),
                                                            new TextArea("Notes (optional)"),
                                                            new Button("Reserve")); // Re-adding
                                                    // components
                                                    // simplified for refresh
                                                    // logic
                                                    refreshMyReservations(userId);
                                                }
                                            });
                            layout.add(cancelButton);
                        }
                        add(layout);
                    });
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            event.forwardTo("");
        }
    }
}
