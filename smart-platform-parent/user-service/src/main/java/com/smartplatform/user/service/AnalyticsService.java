package com.smartplatform.user.service;

import com.smartplatform.user.model.Resource;
import com.smartplatform.user.model.ResourceReservation;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyticsService {
    public static Map<String, Object> getResourceUsageStats() {
        Map<String, Object> stats = new HashMap<>();
        List<ResourceReservation> allReservations = ResourceService.getAllReservations();
        List<Resource> allResources = ResourceService.getAllResources();
        
        // Total resources
        stats.put("totalResources", allResources.size());
        
        // Total reservations
        stats.put("totalReservations", allReservations.size());
        
        // Approved reservations
        long approvedCount = allReservations.stream()
                .filter(r -> r.getStatus().equals("APPROVED"))
                .count();
        stats.put("approvedReservations", approvedCount);
        
        // Pending reservations
        long pendingCount = allReservations.stream()
                .filter(r -> r.getStatus().equals("PENDING"))
                .count();
        stats.put("pendingReservations", pendingCount);
        
        // Most used resources (top 5)
        Map<String, Long> resourceUsage = allReservations.stream()
                .filter(r -> r.getStatus().equals("APPROVED") || r.getStatus().equals("COMPLETED"))
                .collect(Collectors.groupingBy(
                    ResourceReservation::getResourceName,
                    Collectors.counting()
                ));
        List<Map.Entry<String, Long>> topResources = resourceUsage.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(5)
                .collect(Collectors.toList());
        stats.put("topResources", topResources);
        
        // Reservations by priority
        Map<String, Long> priorityStats = allReservations.stream()
                .collect(Collectors.groupingBy(
                    ResourceReservation::getPriority,
                    Collectors.counting()
                ));
        stats.put("reservationsByPriority", priorityStats);
        
        // Reservations by status
        Map<String, Long> statusStats = allReservations.stream()
                .collect(Collectors.groupingBy(
                    ResourceReservation::getStatus,
                    Collectors.counting()
                ));
        stats.put("reservationsByStatus", statusStats);
        
        // Active reservations (current time within reservation period)
        LocalDateTime now = LocalDateTime.now();
        long activeReservations = allReservations.stream()
                .filter(r -> r.getStatus().equals("APPROVED"))
                .filter(r -> now.isAfter(r.getStartDateTime()) && now.isBefore(r.getEndDateTime()))
                .count();
        stats.put("activeReservations", activeReservations);
        
        return stats;
    }
}

