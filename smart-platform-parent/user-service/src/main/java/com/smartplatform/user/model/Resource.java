package com.smartplatform.user.model;

import java.util.UUID;

public class Resource {
    private String id;
    private String name;
    private String type; // e.g., "Gym Equipment", "Meeting Room", "Vehicle"
    private String description;
    private boolean isAvailable;
    private int maxReservationHours;

    public Resource(String name, String type, String description, int maxReservationHours) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.type = type;
        this.description = description;
        this.isAvailable = true;
        this.maxReservationHours = maxReservationHours;
    }

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
}

