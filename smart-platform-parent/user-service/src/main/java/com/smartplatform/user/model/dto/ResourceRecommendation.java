package com.smartplatform.user.model.dto;

/** DTO for resource recommendations Contains information about recommended resources */
public class ResourceRecommendation {

    private String resourceId;
    private String resourceName;
    private String resourceType;
    private String location;
    private double recommendationScore; // 0.0 to 1.0, higher is better
    private String reason; // Why this resource is recommended
    private double averageRating; // Average user rating
    private int currentReservations; // Current number of reservations
    private int maxCapacity; // Maximum concurrent users/reservations
    private double utilizationRate; // Percentage of how much it's used
    private boolean available; // Is it available now

    public ResourceRecommendation() {}

    public ResourceRecommendation(
            String resourceId,
            String resourceName,
            String resourceType,
            String location,
            double recommendationScore,
            String reason) {
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.location = location;
        this.recommendationScore = recommendationScore;
        this.reason = reason;
        this.available = true;
    }

    // Getters and Setters
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

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getRecommendationScore() {
        return recommendationScore;
    }

    public void setRecommendationScore(double recommendationScore) {
        this.recommendationScore = recommendationScore;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getCurrentReservations() {
        return currentReservations;
    }

    public void setCurrentReservations(int currentReservations) {
        this.currentReservations = currentReservations;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public double getUtilizationRate() {
        return utilizationRate;
    }

    public void setUtilizationRate(double utilizationRate) {
        this.utilizationRate = utilizationRate;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
