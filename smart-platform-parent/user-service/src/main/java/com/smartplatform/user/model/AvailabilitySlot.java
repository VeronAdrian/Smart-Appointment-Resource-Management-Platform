package com.smartplatform.user.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class AvailabilitySlot {
    private String id;
    private String staffUsername;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private boolean isBooked;
    private String tenantId; // Multi-tenancy: discriminator column

    public AvailabilitySlot(String staffUsername, LocalDateTime startDateTime, LocalDateTime endDateTime, String tenantId) {
        this.id = UUID.randomUUID().toString();
        this.staffUsername = staffUsername;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.isBooked = false;
        this.tenantId = tenantId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStaffUsername() { return staffUsername; }
    public void setStaffUsername(String staffUsername) { this.staffUsername = staffUsername; }

    public LocalDateTime getStartDateTime() { return startDateTime; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }

    public LocalDateTime getEndDateTime() { return endDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }

    public boolean isBooked() { return isBooked; }
    public void setBooked(boolean booked) { isBooked = booked; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
}

