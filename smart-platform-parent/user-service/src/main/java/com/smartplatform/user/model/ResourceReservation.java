package com.smartplatform.user.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class ResourceReservation {
    private String id;
    private String resourceId;
    private String resourceName;
    private String userId;
    private String userName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String priority; // LOW, MEDIUM, HIGH, URGENT
    private String status; // PENDING, APPROVED, REJECTED, COMPLETED, CANCELLED
    private String notes;
    private String approvedBy; // Admin/Staff who approved
    private String tenantId; // Multi-tenancy: discriminator column

    public ResourceReservation(
            String resourceId,
            String resourceName,
            String userId,
            String userName,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            String priority,
            String tenantId) {
        this.id = UUID.randomUUID().toString();
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.userId = userId;
        this.userName = userName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.priority = priority;
        this.status = "PENDING";
        this.notes = "";
        this.approvedBy = null;
        this.tenantId = tenantId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
