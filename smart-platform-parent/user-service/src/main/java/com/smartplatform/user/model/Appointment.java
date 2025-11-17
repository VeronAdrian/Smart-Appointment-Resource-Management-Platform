package com.smartplatform.user.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Appointment {
    private String id;
    private String clientUsername;
    private String staffUsername;
    private String slotId;
    private LocalDateTime appointmentDateTime;
    private String status; // PENDING, CONFIRMED, CANCELLED
    private String notes;

    public Appointment(String clientUsername, String staffUsername, String slotId, LocalDateTime appointmentDateTime) {
        this.id = UUID.randomUUID().toString();
        this.clientUsername = clientUsername;
        this.staffUsername = staffUsername;
        this.slotId = slotId;
        this.appointmentDateTime = appointmentDateTime;
        this.status = "PENDING";
        this.notes = "";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClientUsername() { return clientUsername; }
    public void setClientUsername(String clientUsername) { this.clientUsername = clientUsername; }

    public String getStaffUsername() { return staffUsername; }
    public void setStaffUsername(String staffUsername) { this.staffUsername = staffUsername; }

    public String getSlotId() { return slotId; }
    public void setSlotId(String slotId) { this.slotId = slotId; }

    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) { this.appointmentDateTime = appointmentDateTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}

