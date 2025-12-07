package com.smartplatform.user.service;

import com.smartplatform.user.model.Appointment;
import com.smartplatform.user.model.AvailabilitySlot;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final List<Appointment> appointments = new ArrayList<>();
    private final UserService userService;
    private final TenantContext tenantContext;
    private final AvailabilityService availabilityService;

    public Appointment bookAppointment(
            String clientUsername,
            String staffUsername,
            String slotId,
            LocalDateTime appointmentDateTime) {
        AvailabilitySlot slot = availabilityService.findSlotById(slotId);
        if (slot == null || slot.isBooked()) {
            return null; // Slot not found or already booked
        }
        String tenantId = slot.getTenantId();
        if (checkConflict(staffUsername, appointmentDateTime, tenantId)) {
            return null; // Conflict detected
        }
        Appointment appointment = new Appointment(
                clientUsername, staffUsername, slotId, appointmentDateTime, tenantId);
        appointments.add(appointment);
        slot.setBooked(true);
        NotificationService.notifyAppointmentBooked(
                getClientEmail(clientUsername),
                null,
                staffUsername,
                appointmentDateTime.toString());
        return appointment;
    }

    public List<Appointment> getAllAppointments() {
        String tenantId = tenantContext.getCurrentTenantId();
        return appointments.stream()
                .filter(
                        apt -> tenantId == null
                                || apt.getTenantId() == null
                                || apt.getTenantId().equals(tenantId))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsByClient(String clientUsername) {
        String tenantId = tenantContext.getCurrentTenantId();
        return appointments.stream()
                .filter(apt -> apt.getClientUsername().equalsIgnoreCase(clientUsername))
                .filter(
                        apt -> tenantId == null
                                || apt.getTenantId() == null
                                || apt.getTenantId().equals(tenantId))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsByStaff(String staffUsername) {
        String tenantId = tenantContext.getCurrentTenantId();
        return appointments.stream()
                .filter(apt -> apt.getStaffUsername().equalsIgnoreCase(staffUsername))
                .filter(
                        apt -> tenantId == null
                                || apt.getTenantId() == null
                                || apt.getTenantId().equals(tenantId))
                .collect(Collectors.toList());
    }

    public boolean cancelAppointment(String appointmentId) {
        Appointment appointment = appointments.stream()
                .filter(apt -> apt.getId().equals(appointmentId))
                .findFirst()
                .orElse(null);
        if (appointment != null) {
            appointment.setStatus("CANCELLED");
            AvailabilitySlot slot = availabilityService.findSlotById(appointment.getSlotId());
            if (slot != null) {
                slot.setBooked(false);
            }
            NotificationService.notifyAppointmentCancelled(
                    getClientEmail(appointment.getClientUsername()),
                    null,
                    appointment.getStaffUsername(),
                    appointment.getAppointmentDateTime().toString());
            return true;
        }
        return false;
    }

    private boolean checkConflict(
            String staffUsername, LocalDateTime appointmentDateTime, String tenantId) {
        return appointments.stream()
                .filter(apt -> apt.getStaffUsername().equalsIgnoreCase(staffUsername))
                .filter(
                        apt -> tenantId == null
                                || apt.getTenantId() == null
                                || apt.getTenantId().equals(tenantId))
                .filter(apt -> !apt.getStatus().equals("CANCELLED"))
                .anyMatch(
                        apt -> {
                            LocalDateTime aptTime = apt.getAppointmentDateTime();
                            return aptTime.equals(appointmentDateTime)
                                    || (aptTime.isBefore(appointmentDateTime.plusHours(1))
                                            && aptTime.isAfter(appointmentDateTime.minusHours(1)));
                        });
    }

    private String getClientEmail(String username) {
        var user = userService.findByUsername(username);
        return user != null ? user.getEmail() : username + "@example.com";
    }
};
