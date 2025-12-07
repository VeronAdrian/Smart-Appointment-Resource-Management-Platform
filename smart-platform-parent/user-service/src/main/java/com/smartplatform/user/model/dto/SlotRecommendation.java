package com.smartplatform.user.model.dto;

import java.time.LocalDateTime;

/** DTO for slot recommendations Contains information about recommended appointment slots */
public class SlotRecommendation {

    private String slotId;
    private String staffUsername;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int durationMinutes;
    private double recommendationScore; // 0.0 to 1.0, higher is better
    private String reason; // Why this slot is recommended
    private int staffAvgRating; // Average rating of the staff member
    private int availableSlots; // Number of consecutive available slots
    private String staffSpecialties; // Comma-separated specialties

    public SlotRecommendation() {}

    public SlotRecommendation(
            String slotId,
            String staffUsername,
            LocalDateTime startTime,
            LocalDateTime endTime,
            int durationMinutes,
            double recommendationScore,
            String reason) {
        this.slotId = slotId;
        this.staffUsername = staffUsername;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = durationMinutes;
        this.recommendationScore = recommendationScore;
        this.reason = reason;
        this.availableSlots = 1;
        this.staffAvgRating = 0;
    }

    // Getters and Setters
    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getStaffUsername() {
        return staffUsername;
    }

    public void setStaffUsername(String staffUsername) {
        this.staffUsername = staffUsername;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
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

    public int getStaffAvgRating() {
        return staffAvgRating;
    }

    public void setStaffAvgRating(int staffAvgRating) {
        this.staffAvgRating = staffAvgRating;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public String getStaffSpecialties() {
        return staffSpecialties;
    }

    public void setStaffSpecialties(String staffSpecialties) {
        this.staffSpecialties = staffSpecialties;
    }
}
