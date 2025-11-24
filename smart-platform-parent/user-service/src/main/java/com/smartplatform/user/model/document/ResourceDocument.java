package com.smartplatform.user.model.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Elasticsearch document for Resource
 * Used for fast searching and filtering of resources
 */
@Document(indexName = "resources")
public class ResourceDocument {
    
    @Id
    private String id;
    
    @Field(type = FieldType.Text)
    private String name;
    
    @Field(type = FieldType.Keyword)
    private String type; // e.g., "Gym Equipment", "Meeting Room", "Vehicle"
    
    @Field(type = FieldType.Text)
    private String description;
    
    @Field(type = FieldType.Keyword)
    private boolean isAvailable;
    
    @Field(type = FieldType.Integer)
    private int maxReservationHours;
    
    @Field(type = FieldType.Keyword)
    private String tenantId;
    
    @Field(type = FieldType.Integer)
    private int currentReservations; // Count of active reservations
    
    @Field(type = FieldType.Float)
    private double usageScore; // Score based on historical usage
    
    @Field(type = FieldType.Float)
    private double rating; // Average rating from users
    
    @Field(type = FieldType.Keyword)
    private String location; // Physical location or identifier

    public ResourceDocument() {}

    public ResourceDocument(String id, String name, String type, String description,
                           boolean isAvailable, int maxReservationHours, String tenantId,
                           String location) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.isAvailable = isAvailable;
        this.maxReservationHours = maxReservationHours;
        this.tenantId = tenantId;
        this.location = location;
        this.currentReservations = 0;
        this.usageScore = 0.0;
        this.rating = 0.0;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public int getMaxReservationHours() { return maxReservationHours; }
    public void setMaxReservationHours(int maxReservationHours) { this.maxReservationHours = maxReservationHours; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public int getCurrentReservations() { return currentReservations; }
    public void setCurrentReservations(int currentReservations) { this.currentReservations = currentReservations; }

    public double getUsageScore() { return usageScore; }
    public void setUsageScore(double usageScore) { this.usageScore = usageScore; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
