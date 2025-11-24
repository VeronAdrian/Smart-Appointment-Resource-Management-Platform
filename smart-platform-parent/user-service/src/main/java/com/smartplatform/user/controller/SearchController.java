package com.smartplatform.user.controller;

import com.smartplatform.user.model.document.AppointmentDocument;
import com.smartplatform.user.model.document.ResourceDocument;
import com.smartplatform.user.model.dto.ResourceRecommendation;
import com.smartplatform.user.model.dto.RecommendationResponse;
import com.smartplatform.user.model.dto.SlotRecommendation;
import com.smartplatform.user.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Search and Recommendation endpoints
 * Provides fast searching for appointments and resources using Elasticsearch
 * Provides intelligent recommendations for best slots and resources
 */
@RestController
@RequestMapping("/api/search")
public class SearchController {
    
    @Autowired
    private AppointmentSearchService appointmentSearchService;
    
    @Autowired
    private ResourceSearchService resourceSearchService;
    
    @Autowired
    private RecommendationEngine recommendationEngine;
    
    @Autowired
    private TenantContext tenantContext;

    // ==================== APPOINTMENT SEARCH ENDPOINTS ====================
    
    /**
     * Search appointments by client
     * GET /api/search/appointments/client/{clientUsername}
     */
    @GetMapping("/appointments/client/{clientUsername}")
    public ResponseEntity<Map<String, Object>> searchAppointmentsByClient(@PathVariable String clientUsername) {
        String tenantId = tenantContext.getCurrentTenantId();
        List<AppointmentDocument> results = appointmentSearchService.searchByClient(clientUsername, tenantId);
        
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "count", results.size(),
                "data", results
        ));
    }
    
    /**
     * Search appointments by staff member
     * GET /api/search/appointments/staff/{staffUsername}
     */
    @GetMapping("/appointments/staff/{staffUsername}")
    public ResponseEntity<Map<String, Object>> searchAppointmentsByStaff(@PathVariable String staffUsername) {
        String tenantId = tenantContext.getCurrentTenantId();
        List<AppointmentDocument> results = appointmentSearchService.searchByStaff(staffUsername, tenantId);
        
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "count", results.size(),
                "data", results
        ));
    }
    
    /**
     * Search appointments by status
     * GET /api/search/appointments/status/{status}
     */
    @GetMapping("/appointments/status/{status}")
    public ResponseEntity<Map<String, Object>> searchAppointmentsByStatus(@PathVariable String status) {
        String tenantId = tenantContext.getCurrentTenantId();
        List<AppointmentDocument> results = appointmentSearchService.searchByStatus(status, tenantId);
        
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "count", results.size(),
                "data", results
        ));
    }
    
    /**
     * Search appointments by type
     * GET /api/search/appointments/type/{type}
     */
    @GetMapping("/appointments/type/{type}")
    public ResponseEntity<Map<String, Object>> searchAppointmentsByType(@PathVariable String type) {
        String tenantId = tenantContext.getCurrentTenantId();
        List<AppointmentDocument> results = appointmentSearchService.searchByType(type, tenantId);
        
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "count", results.size(),
                "data", results
        ));
    }
    
    /**
     * Search appointments by date range
     * GET /api/search/appointments/date-range?startDate=2024-01-01T00:00:00&endDate=2024-01-31T23:59:59
     */
    @GetMapping("/appointments/date-range")
    public ResponseEntity<Map<String, Object>> searchAppointmentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        String tenantId = tenantContext.getCurrentTenantId();
        List<AppointmentDocument> results = appointmentSearchService.searchByDateRange(startDate, endDate, tenantId);
        
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "count", results.size(),
                "data", results
        ));
    }
    
    /**
     * Search staff schedule within a date range
     * GET /api/search/appointments/staff-schedule/{staffUsername}?startDate=2024-01-01T00:00:00&endDate=2024-01-31T23:59:59
     */
    @GetMapping("/appointments/staff-schedule/{staffUsername}")
    public ResponseEntity<Map<String, Object>> searchStaffSchedule(
            @PathVariable String staffUsername,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        String tenantId = tenantContext.getCurrentTenantId();
        List<AppointmentDocument> results = appointmentSearchService.searchStaffSchedule(staffUsername, startDate, endDate, tenantId);
        
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "count", results.size(),
                "data", results
        ));
    }

    // ==================== RESOURCE SEARCH ENDPOINTS ====================
    
    /**
     * Search resources by type
     * GET /api/search/resources/type/{type}
     */
    @GetMapping("/resources/type/{type}")
    public ResponseEntity<Map<String, Object>> searchResourcesByType(@PathVariable String type) {
        String tenantId = tenantContext.getCurrentTenantId();
        List<ResourceDocument> results = resourceSearchService.searchByType(type, tenantId);
        
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "count", results.size(),
                "data", results
        ));
    }
    
    /**
     * Search available resources
     * GET /api/search/resources/available
     */
    @GetMapping("/resources/available")
    public ResponseEntity<Map<String, Object>> searchAvailableResources() {
        String tenantId = tenantContext.getCurrentTenantId();
        List<ResourceDocument> results = resourceSearchService.searchAvailableResources(tenantId);
        
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "count", results.size(),
                "data", results
        ));
    }
    
    /**
     * Search resources by name or description
     * GET /api/search/resources/search?term=meeting
     */
    @GetMapping("/resources/search")
    public ResponseEntity<Map<String, Object>> searchResourcesByNameOrDescription(
            @RequestParam String term) {
        
        String tenantId = tenantContext.getCurrentTenantId();
        List<ResourceDocument> results = resourceSearchService.searchByNameOrDescription(term, tenantId);
        
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "count", results.size(),
                "data", results
        ));
    }
    
    /**
     * Get high-rated resources
     * GET /api/search/resources/high-rated
     */
    @GetMapping("/resources/high-rated")
    public ResponseEntity<Map<String, Object>> getHighRatedResources() {
        String tenantId = tenantContext.getCurrentTenantId();
        List<ResourceDocument> results = resourceSearchService.getHighRatedResources(tenantId);
        
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "count", results.size(),
                "data", results
        ));
    }
    
    /**
     * Get least used resources
     * GET /api/search/resources/least-used
     */
    @GetMapping("/resources/least-used")
    public ResponseEntity<Map<String, Object>> getLeastUsedResources() {
        String tenantId = tenantContext.getCurrentTenantId();
        List<ResourceDocument> results = resourceSearchService.getLeastUsedResources(tenantId);
        
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "count", results.size(),
                "data", results
        ));
    }

    // ==================== RECOMMENDATION ENDPOINTS ====================
    
    /**
     * Get recommended appointment slots
     * GET /api/search/recommendations/slots?appointmentType=consultation&durationMinutes=30&startDate=2024-01-15T00:00:00&endDate=2024-01-31T23:59:59&maxResults=5
     */
    @GetMapping("/recommendations/slots")
    public ResponseEntity<RecommendationResponse> getRecommendedSlots(
            @RequestParam String appointmentType,
            @RequestParam int durationMinutes,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "10") int maxResults) {
        
        long startTime = System.currentTimeMillis();
        String tenantId = tenantContext.getCurrentTenantId();
        
        List<SlotRecommendation> recommendations = recommendationEngine.recommendSlots(
                appointmentType, durationMinutes, startDate, endDate, tenantId, maxResults
        );
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        RecommendationResponse response = new RecommendationResponse();
        response.setRecommendedSlots(recommendations);
        response.setRecommendedResources(List.of());
        response.setSearchCriteria(String.format("appointmentType=%s, duration=%d min", appointmentType, durationMinutes));
        response.setTotalResults(recommendations.size());
        response.setExecutionTimeMs(executionTime);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get recommended resources
     * GET /api/search/recommendations/resources?resourceType=meetingRoom&location=floor1&startDate=2024-01-15T00:00:00&endDate=2024-01-31T23:59:59&maxResults=5
     */
    @GetMapping("/recommendations/resources")
    public ResponseEntity<RecommendationResponse> getRecommendedResources(
            @RequestParam String resourceType,
            @RequestParam(required = false) String location,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "10") int maxResults) {
        
        long startTime = System.currentTimeMillis();
        String tenantId = tenantContext.getCurrentTenantId();
        
        List<ResourceRecommendation> recommendations = recommendationEngine.recommendResources(
                resourceType, location, startDate, endDate, tenantId, maxResults
        );
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        RecommendationResponse response = new RecommendationResponse();
        response.setRecommendedSlots(List.of());
        response.setRecommendedResources(recommendations);
        response.setSearchCriteria(String.format("resourceType=%s, location=%s", resourceType, location));
        response.setTotalResults(recommendations.size());
        response.setExecutionTimeMs(executionTime);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get combined recommendations for appointments and resources
     * GET /api/search/recommendations/combined?appointmentType=consultation&resourceType=meetingRoom&durationMinutes=30&startDate=2024-01-15T00:00:00&endDate=2024-01-31T23:59:59
     */
    @GetMapping("/recommendations/combined")
    public ResponseEntity<RecommendationResponse> getCombinedRecommendations(
            @RequestParam(required = false) String appointmentType,
            @RequestParam(required = false) String resourceType,
            @RequestParam(defaultValue = "30") int durationMinutes,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "5") int maxResults) {
        
        long startTime = System.currentTimeMillis();
        String tenantId = tenantContext.getCurrentTenantId();
        
        List<SlotRecommendation> slotRecommendations = appointmentType != null ?
                recommendationEngine.recommendSlots(appointmentType, durationMinutes, startDate, endDate, tenantId, maxResults) :
                List.of();
        
        List<ResourceRecommendation> resourceRecommendations = resourceType != null ?
                recommendationEngine.recommendResources(resourceType, null, startDate, endDate, tenantId, maxResults) :
                List.of();
        
        long executionTime = System.currentTimeMillis() - startTime;
        
        RecommendationResponse response = new RecommendationResponse();
        response.setRecommendedSlots(slotRecommendations);
        response.setRecommendedResources(resourceRecommendations);
        response.setSearchCriteria(String.format("appointmentType=%s, resourceType=%s", appointmentType, resourceType));
        response.setTotalResults(slotRecommendations.size() + resourceRecommendations.size());
        response.setExecutionTimeMs(executionTime);
        
        return ResponseEntity.ok(response);
    }
}
