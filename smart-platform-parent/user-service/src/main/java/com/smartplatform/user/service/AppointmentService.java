package com.smartplatform.user.service;

import com.smartplatform.user.model.Appointment;
import com.smartplatform.user.model.AvailabilitySlot;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentService {
    private static final List<Appointment> appointments = new ArrayList<>();

    public static Appointment bookAppointment(String clientUsername, String staffUsername, String slotId, LocalDateTime appointmentDateTime) {
        AvailabilitySlot slot = AvailabilityService.findSlotById(slotId);
        if (slot == null || slot.isBooked()) {
            return null; // Slot not found or already booked
        }
        if (checkConflict(staffUsername, appointmentDateTime)) {
            return null; // Conflict detected
        }
        Appointment appointment = new Appointment(clientUsername, staffUsername, slotId, appointmentDateTime);
        appointments.add(appointment);
        slot.setBooked(true);
        NotificationService.notifyAppointmentBooked(
            getClientEmail(clientUsername), 
            null, 
            staffUsername, 
            appointmentDateTime.toString()
        );
        return appointment;
    }

    public static List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    public static List<Appointment> getAppointmentsByClient(String clientUsername) {
        return appointments.stream()
                .filter(apt -> apt.getClientUsername().equalsIgnoreCase(clientUsername))
                .collect(Collectors.toList());
    }

    public static List<Appointment> getAppointmentsByStaff(String staffUsername) {
        return appointments.stream()
                .filter(apt -> apt.getStaffUsername().equalsIgnoreCase(staffUsername))
                .collect(Collectors.toList());
    }

    public static boolean cancelAppointment(String appointmentId) {
        Appointment appointment = appointments.stream()
                .filter(apt -> apt.getId().equals(appointmentId))
                .findFirst()
                .orElse(null);
        if (appointment != null) {
            appointment.setStatus("CANCELLED");
            AvailabilitySlot slot = AvailabilityService.findSlotById(appointment.getSlotId());
            if (slot != null) {
                slot.setBooked(false);
            }
            NotificationService.notifyAppointmentCancelled(
                getClientEmail(appointment.getClientUsername()),
                null,
                appointment.getStaffUsername(),
                appointment.getAppointmentDateTime().toString()
            );
            return true;
        }
        return false;
    }

    private static boolean checkConflict(String staffUsername, LocalDateTime appointmentDateTime) {
        return appointments.stream()
                .filter(apt -> apt.getStaffUsername().equalsIgnoreCase(staffUsername))
                .filter(apt -> !apt.getStatus().equals("CANCELLED"))
                .anyMatch(apt -> {
                    LocalDateTime aptTime = apt.getAppointmentDateTime();
                    return aptTime.equals(appointmentDateTime) || 
                           (aptTime.isBefore(appointmentDateTime.plusHours(1)) && aptTime.isAfter(appointmentDateTime.minusHours(1)));
                });
    }

    private static String getClientEmail(String username) {
        var user = com.smartplatform.user.service.UserService.findByUsername(username);
        return user != null ? user.getEmail() : username + "@example.com";
    }
}

