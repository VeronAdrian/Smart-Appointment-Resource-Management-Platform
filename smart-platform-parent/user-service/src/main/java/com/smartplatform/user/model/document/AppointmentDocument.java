package com.smartplatform.user.model.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.time.LocalDateTime;

/**
 * Elasticsearch document for Appointment
 * Used for fast searching and filtering of appointments
 */
@Document(indexName = "appointments")
public class AppointmentDocument {
    
    @Id
    private String id;
    
    @Field(type = FieldType.Keyword)
    private String clientUsername;
    
    @Field(type = FieldType.Keyword)
    private String staffUsername;
    
    @Field(type = FieldType.Keyword)
    private String slotId;
    
    @Field(type = FieldType.Date)
    private LocalDateTime appointmentDateTime;
    
    @Field(type = FieldType.Keyword)
    private String status; // PENDING, CONFIRMED, CANCELLED
    
    @Field(type = FieldType.Text)
    private String notes;
    
    @Field(type = FieldType.Keyword)
    private String tenantId;
    
    @Field(type = FieldType.Integer)
    private int durationMinutes;
    
    @Field(type = FieldType.Keyword)
    private String appointmentType;
    
    @Field(type = FieldType.Float)
    private double rating; // Post-appointment rating

    public AppointmentDocument() {}

    public AppointmentDocument(String id, String clientUsername, String staffUsername, String slotId,
                               LocalDateTime appointmentDateTime, String status, String notes,
                               String tenantId, int durationMinutes, String appointmentType) {
        this.id = id;
        this.clientUsername = clientUsername;
        this.staffUsername = staffUsername;
        this.slotId = slotId;
        this.appointmentDateTime = appointmentDateTime;
        this.status = status;
        this.notes = notes;
        this.tenantId = tenantId;
        this.durationMinutes = durationMinutes;
        this.appointmentType = appointmentType;
        this.rating = 0.0;
    }

    // Getters and Setters
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

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getAppointmentType() { return appointmentType; }
    public void setAppointmentType(String appointmentType) { this.appointmentType = appointmentType; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}
