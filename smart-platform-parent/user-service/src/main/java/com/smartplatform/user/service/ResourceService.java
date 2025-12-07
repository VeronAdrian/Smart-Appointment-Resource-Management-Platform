package com.smartplatform.user.service;

import com.smartplatform.user.model.Resource;
import com.smartplatform.user.model.ResourceReservation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResourceService {
    private final List<Resource> resources = new ArrayList<>();
    private final List<ResourceReservation> reservations = new ArrayList<>();
    private final UserService userService;
    private final TenantContext tenantContext;

    // Resource Management
    public Resource createResource(
            String name,
            String type,
            String description,
            int maxReservationHours,
            String tenantId) {
        Resource resource = new Resource(name, type, description, maxReservationHours, tenantId);
        resources.add(resource);
        return resource;
    }

    public List<Resource> getAllResources() {
        String tenantId = tenantContext.getCurrentTenantId();
        return resources.stream()
                .filter(
                        r ->
                                tenantId == null
                                        || r.getTenantId() == null
                                        || r.getTenantId().equals(tenantId))
                .collect(Collectors.toList());
    }

    public Resource findResourceById(String resourceId) {
        String tenantId = tenantContext.getCurrentTenantId();
        return resources.stream()
                .filter(r -> r.getId().equals(resourceId))
                .filter(
                        r ->
                                tenantId == null
                                        || r.getTenantId() == null
                                        || r.getTenantId().equals(tenantId))
                .findFirst()
                .orElse(null);
    }

    public boolean deleteResource(String resourceId) {
        return resources.removeIf(r -> r.getId().equals(resourceId));
    }

    // Reservation Management
    public ResourceReservation createReservation(
            String resourceId,
            String userId,
            String userName,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            String priority) {
        Resource resource = findResourceById(resourceId);
        if (resource == null) {
            return null;
        }
        String tenantId = resource.getTenantId();
        if (checkConflict(resourceId, startDateTime, endDateTime, tenantId)) {
            return null; // Conflict detected
        }
        ResourceReservation reservation =
                new ResourceReservation(
                        resourceId,
                        resource.getName(),
                        userId,
                        userName,
                        startDateTime,
                        endDateTime,
                        priority,
                        tenantId);
        reservations.add(reservation);
        return reservation;
    }

    public List<ResourceReservation> getAllReservations() {
        String tenantId = tenantContext.getCurrentTenantId();
        return reservations.stream()
                .filter(
                        r ->
                                tenantId == null
                                        || r.getTenantId() == null
                                        || r.getTenantId().equals(tenantId))
                .collect(Collectors.toList());
    }

    public List<ResourceReservation> getPendingReservations() {
        String tenantId = tenantContext.getCurrentTenantId();
        return reservations.stream()
                .filter(r -> r.getStatus().equals("PENDING"))
                .filter(
                        r ->
                                tenantId == null
                                        || r.getTenantId() == null
                                        || r.getTenantId().equals(tenantId))
                .sorted(
                        (r1, r2) -> {
                            int priorityOrder =
                                    getPriorityOrder(r2.getPriority())
                                            - getPriorityOrder(r1.getPriority());
                            if (priorityOrder != 0) return priorityOrder;
                            return r1.getStartDateTime().compareTo(r2.getStartDateTime());
                        })
                .collect(Collectors.toList());
    }

    public List<ResourceReservation> getReservationsByUser(String userId) {
        String tenantId = tenantContext.getCurrentTenantId();
        return reservations.stream()
                .filter(r -> r.getUserId().equals(userId))
                .filter(
                        r ->
                                tenantId == null
                                        || r.getTenantId() == null
                                        || r.getTenantId().equals(tenantId))
                .collect(Collectors.toList());
    }

    public List<ResourceReservation> getReservationsByResource(String resourceId) {
        String tenantId = tenantContext.getCurrentTenantId();
        return reservations.stream()
                .filter(r -> r.getResourceId().equals(resourceId))
                .filter(
                        r ->
                                tenantId == null
                                        || r.getTenantId() == null
                                        || r.getTenantId().equals(tenantId))
                .collect(Collectors.toList());
    }

    public boolean approveReservation(String reservationId, String approvedBy) {
        ResourceReservation reservation =
                reservations.stream()
                        .filter(r -> r.getId().equals(reservationId))
                        .findFirst()
                        .orElse(null);
        if (reservation != null && reservation.getStatus().equals("PENDING")) {
            reservation.setStatus("APPROVED");
            reservation.setApprovedBy(approvedBy);
            Resource resource = findResourceById(reservation.getResourceId());
            if (resource != null) {
                resource.setAvailable(false);
            }
            NotificationService.sendEmail(
                    getUserEmail(reservation.getUserId()),
                    "Resource Reservation Approved",
                    "Your reservation for "
                            + reservation.getResourceName()
                            + " has been approved.");
            return true;
        }
        return false;
    }

    public boolean rejectReservation(String reservationId, String rejectedBy, String reason) {
        ResourceReservation reservation =
                reservations.stream()
                        .filter(r -> r.getId().equals(reservationId))
                        .findFirst()
                        .orElse(null);
        if (reservation != null && reservation.getStatus().equals("PENDING")) {
            reservation.setStatus("REJECTED");
            reservation.setApprovedBy(rejectedBy);
            reservation.setNotes(reason);
            NotificationService.sendEmail(
                    getUserEmail(reservation.getUserId()),
                    "Resource Reservation Rejected",
                    "Your reservation for "
                            + reservation.getResourceName()
                            + " has been rejected. Reason: "
                            + reason);
            return true;
        }
        return false;
    }

    public boolean cancelReservation(String reservationId) {
        ResourceReservation reservation =
                reservations.stream()
                        .filter(r -> r.getId().equals(reservationId))
                        .findFirst()
                        .orElse(null);
        if (reservation != null) {
            reservation.setStatus("CANCELLED");
            Resource resource = findResourceById(reservation.getResourceId());
            if (resource != null) {
                resource.setAvailable(true);
            }
            return true;
        }
        return false;
    }

    private boolean checkConflict(
            String resourceId, LocalDateTime start, LocalDateTime end, String tenantId) {
        return reservations.stream()
                .filter(r -> r.getResourceId().equals(resourceId))
                .filter(
                        r ->
                                tenantId == null
                                        || r.getTenantId() == null
                                        || r.getTenantId().equals(tenantId))
                .filter(r -> r.getStatus().equals("APPROVED") || r.getStatus().equals("PENDING"))
                .anyMatch(
                        r -> {
                            LocalDateTime rStart = r.getStartDateTime();
                            LocalDateTime rEnd = r.getEndDateTime();
                            return (start.isBefore(rEnd) && end.isAfter(rStart));
                        });
    }

    private int getPriorityOrder(String priority) {
        switch (priority) {
            case "URGENT":
                return 4;
            case "HIGH":
                return 3;
            case "MEDIUM":
                return 2;
            case "LOW":
                return 1;
            default:
                return 0;
        }
    }

    private String getUserEmail(String userId) {
        var user = userService.findByUsername(userId);
        return user != null ? user.getEmail() : userId + "@example.com";
    }
}
