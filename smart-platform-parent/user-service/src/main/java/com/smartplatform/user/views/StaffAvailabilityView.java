package com.smartplatform.user.views;

import com.smartplatform.user.model.AvailabilitySlot;
import com.smartplatform.user.service.AvailabilityService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Route("staff-availability")
public class StaffAvailabilityView extends VerticalLayout implements BeforeEnterObserver {
    public StaffAvailabilityView() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("STAFF"))) {
            return;
        }
        String staffUsername = auth.getName();
        add(new H2("Define Availability Slots - " + staffUsername));
        DatePicker datePicker = new DatePicker("Date");
        IntegerField startHour = new IntegerField("Start Hour (0-23)");
        IntegerField startMinute = new IntegerField("Start Minute (0-59)");
        IntegerField endHour = new IntegerField("End Hour (0-23)");
        IntegerField endMinute = new IntegerField("End Minute (0-59)");
        Button createSlot = new Button("Create Availability Slot", e -> {
            if (datePicker.getValue() == null) {
                Notification.show("Please select a date");
                return;
            }
            LocalDate date = datePicker.getValue();
            LocalTime startTime = LocalTime.of(startHour.getValue() != null ? startHour.getValue() : 9, startMinute.getValue() != null ? startMinute.getValue() : 0);
            LocalTime endTime = LocalTime.of(endHour.getValue() != null ? endHour.getValue() : 10, endMinute.getValue() != null ? endMinute.getValue() : 0);
            LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
            LocalDateTime endDateTime = LocalDateTime.of(date, endTime);
            String tenantId = com.smartplatform.user.service.TenantContext.getCurrentTenantId();
            AvailabilitySlot slot = AvailabilityService.createSlot(staffUsername, startDateTime, endDateTime, tenantId);
            if (slot != null) {
                Notification.show("Availability slot created successfully!");
                datePicker.clear();
                startHour.clear();
                startMinute.clear();
                endHour.clear();
                endMinute.clear();
            } else {
                Notification.show("Error: Conflict detected or invalid time range");
            }
        });
        add(datePicker, startHour, startMinute, endHour, endMinute, createSlot);
    }
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("STAFF"))) {
            event.forwardTo("");
        }
    }
}

