package com.smartplatform.user.service;

import com.smartplatform.user.model.AvailabilitySlot;
import com.smartplatform.user.model.document.AppointmentDocument;
import com.smartplatform.user.model.document.ResourceDocument;
import com.smartplatform.user.model.dto.ResourceRecommendation;
import com.smartplatform.user.model.dto.SlotRecommendation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Recommendation Engine for Smart Appointment & Resource Management Platform
 * Provides intelligent recommendations for:
 * 1. Best available appointment slots based on staff ratings and availability
 * 2. Best available resources based on ratings, usage patterns, and availability
 */
@Service
public class RecommendationEngine {
    
    @Autowired
    private AppointmentSearchService appointmentSearchService;
    
    @Autowired
    private ResourceSearchService resourceSearchService;
    
    @Autowired
    private AvailabilityService availabilityService;
    
    /**
     * Recommend best available slots for an appointment
     * Scoring factors:
     * - Staff member rating (40%)
     * - Slot availability (30%)
     * - Appointment duration fit (20%)
     * - User preferences (10%)
     */
    public List<SlotRecommendation> recommendSlots(String appointmentType, int durationMinutes,
                                                    LocalDateTime preferredStart, LocalDateTime preferredEnd,
                                                    String tenantId, int maxRecommendations) {
        long startTime = System.currentTimeMillis();
        
        // Get all available slots in the preferred date range
        List<AvailabilitySlot> availableSlots = availabilityService
                .getAvailableSlotsInRange(preferredStart, preferredEnd, tenantId);
        
        // Convert to recommendations with scoring
        List<SlotRecommendation> recommendations = availableSlots.stream()
                .map(slot -> scoreSlot(slot, appointmentType, durationMinutes, tenantId))
                .filter(rec -> rec.getRecommendationScore() > 0)
                .sorted(Comparator.comparingDouble(SlotRecommendation::getRecommendationScore).reversed())
                .limit(maxRecommendations)
                .collect(Collectors.toList());
        
        long executionTime = System.currentTimeMillis() - startTime;
        // Add execution context if needed
        
        return recommendations;
    }
    
    /**
     * Recommend best resources based on multiple criteria
     * Scoring factors:
     * - Resource rating (35%)
     * - Low utilization (30%) - less busy = better experience
     * - Type match (20%)
     * - Location convenience (15%)
     */
    public List<ResourceRecommendation> recommendResources(String resourceType, String location,
                                                          LocalDateTime requiredFrom, LocalDateTime requiredUntil,
                                                          String tenantId, int maxRecommendations) {
        long startTime = System.currentTimeMillis();
        
        // Get available resources of the specified type
        List<ResourceDocument> availableResources = resourceSearchService
                .searchByTypeAndAvailability(resourceType, true, tenantId);
        
        // Score and sort recommendations
        List<ResourceRecommendation> recommendations = availableResources.stream()
                .map(resource -> scoreResource(resource, location, tenantId))
                .sorted(Comparator.comparingDouble(ResourceRecommendation::getRecommendationScore).reversed())
                .limit(maxRecommendations)
                .collect(Collectors.toList());
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        return recommendations;
    }
    
    /**
     * Score a single slot based on multiple factors
     */
    private SlotRecommendation scoreSlot(AvailabilitySlot slot, String appointmentType, 
                                        int requestedDuration, String tenantId) {
        SlotRecommendation recommendation = new SlotRecommendation();
        recommendation.setSlotId(slot.getId());
        recommendation.setStaffUsername(slot.getStaffUsername());
        recommendation.setStartTime(slot.getStartTime());
        recommendation.setEndTime(slot.getEndTime());
        recommendation.setDurationMinutes(requestedDuration);
        
        // Calculate component scores
        double staffRatingScore = calculateStaffRatingScore(slot.getStaffUsername(), tenantId);
        double availabilityScore = calculateAvailabilityScore(slot);
        double durationFitScore = calculateDurationFitScore(slot, requestedDuration);
        double timeProximityScore = calculateTimeProximityScore(slot);
        
        // Weighted average score
        double totalScore = (staffRatingScore * 0.40) +
                           (availabilityScore * 0.30) +
                           (durationFitScore * 0.20) +
                           (timeProximityScore * 0.10);
        
        recommendation.setRecommendationScore(totalScore);
        
        // Generate reason
        StringBuilder reason = new StringBuilder();
        if (staffRatingScore > 0.8) {
            reason.append("Highly-rated staff. ");
        }
        if (availabilityScore > 0.9) {
            reason.append("Few concurrent bookings. ");
        }
        if (durationFitScore > 0.95) {
            reason.append("Perfect duration fit. ");
        }
        
        recommendation.setReason(reason.toString().trim());
        
        return recommendation;
    }
    
    /**
     * Score a resource based on multiple factors
     */
    private ResourceRecommendation scoreResource(ResourceDocument resource, String preferredLocation, String tenantId) {
        ResourceRecommendation recommendation = new ResourceRecommendation();
        recommendation.setResourceId(resource.getId());
        recommendation.setResourceName(resource.getName());
        recommendation.setResourceType(resource.getType());
        recommendation.setLocation(resource.getLocation());
        recommendation.setAvailable(resource.isAvailable());
        recommendation.setAverageRating(resource.getRating());
        recommendation.setCurrentReservations(resource.getCurrentReservations());
        
        // Calculate component scores
        double ratingScore = calculateResourceRatingScore(resource.getRating());
        double utilizationScore = calculateUtilizationScore(resource);
        double locationScore = calculateLocationScore(resource.getLocation(), preferredLocation);
        double availabilityScore = calculateResourceAvailabilityScore(resource);
        
        // Weighted average score
        double totalScore = (ratingScore * 0.35) +
                           (utilizationScore * 0.30) +
                           (locationScore * 0.15) +
                           (availabilityScore * 0.20);
        
        recommendation.setRecommendationScore(totalScore);
        
        // Generate reason
        StringBuilder reason = new StringBuilder();
        if (ratingScore > 0.8) {
            reason.append("Highly rated by users. ");
        }
        if (utilizationScore > 0.7) {
            reason.append("Less occupied. ");
        }
        if (availabilityScore > 0.9) {
            reason.append("Currently available. ");
        }
        
        recommendation.setReason(reason.toString().trim());
        
        return recommendation;
    }
    
    /**
     * Calculate staff rating score (0-1)
     */
    private double calculateStaffRatingScore(String staffUsername, String tenantId) {
        List<AppointmentDocument> staffAppointments = appointmentSearchService.getHighRatedAppointments(staffUsername, tenantId);
        
        if (staffAppointments.isEmpty()) {
            return 0.5; // Neutral score if no history
        }
        
        double avgRating = staffAppointments.stream()
                .mapToDouble(AppointmentDocument::getRating)
                .average()
                .orElse(0.0);
        
        // Normalize to 0-1 scale
        return Math.min(avgRating / 5.0, 1.0);
    }
    
    /**
     * Calculate slot availability score based on concurrent bookings
     */
    private double calculateAvailabilityScore(AvailabilitySlot slot) {
        // This is a simplified calculation; in production, check actual bookings
        // For now, return a good score for slots that have more duration
        int slotDuration = (int) java.time.temporal.ChronoUnit.MINUTES.between(slot.getStartTime(), slot.getEndTime());
        return Math.min(slotDuration / 120.0, 1.0); // 120 min = max score
    }
    
    /**
     * Calculate how well the slot duration fits the requested duration
     */
    private double calculateDurationFitScore(AvailabilitySlot slot, int requestedDuration) {
        int slotDuration = (int) java.time.temporal.ChronoUnit.MINUTES.between(slot.getStartTime(), slot.getEndTime());
        
        if (slotDuration < requestedDuration) {
            return 0.0; // Slot is too short
        }
        
        // Return 1.0 if exact fit, scale down for larger slots
        double ratio = (double) requestedDuration / slotDuration;
        return Math.min(ratio, 1.0);
    }
    
    /**
     * Calculate time proximity score - prefer slots closer to now
     */
    private double calculateTimeProximityScore(AvailabilitySlot slot) {
        LocalDateTime now = LocalDateTime.now();
        long minutesFromNow = java.time.temporal.ChronoUnit.MINUTES.between(now, slot.getStartTime());
        
        if (minutesFromNow < 0) {
            return 0.0; // Past slot
        }
        
        // Prefer slots within 7 days (10080 minutes)
        // Score decreases after 7 days
        return Math.max(1.0 - (minutesFromNow / 10080.0), 0.0);
    }
    
    /**
     * Calculate resource rating score (0-1)
     */
    private double calculateResourceRatingScore(double rating) {
        // Normalize 0-5 rating to 0-1 scale
        return Math.min(rating / 5.0, 1.0);
    }
    
    /**
     * Calculate utilization score - prefer less utilized resources
     */
    private double calculateUtilizationScore(ResourceDocument resource) {
        // Lower utilization = higher score
        // Assuming max reasonable reservations is 10
        double utilizationRate = Math.min((double) resource.getCurrentReservations() / 10.0, 1.0);
        return 1.0 - utilizationRate; // Invert so lower usage = higher score
    }
    
    /**
     * Calculate location score - prefer matching locations
     */
    private double calculateLocationScore(String resourceLocation, String preferredLocation) {
        if (preferredLocation == null || resourceLocation == null) {
            return 0.5;
        }
        
        return resourceLocation.equalsIgnoreCase(preferredLocation) ? 1.0 : 0.5;
    }
    
    /**
     * Calculate resource availability score
     */
    private double calculateResourceAvailabilityScore(ResourceDocument resource) {
        return resource.isAvailable() ? 1.0 : 0.0;
    }
}
