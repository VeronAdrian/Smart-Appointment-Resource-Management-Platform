package com.smartplatform.user.service;

import com.smartplatform.user.model.Resource;
import com.smartplatform.user.model.ResourceReservation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResourceService {
    private static final List<Resource> resources = new ArrayList<>();
    private static final List<ResourceReservation> reservations = new ArrayList<>();

    // Resource Management
    public static Resource createResource(String name, String type, String description, int maxReservationHours) {
        Resource resource = new Resource(name, type, description, maxReservationHours);
        resources.add(resource);
        return resource;
    }

    public static List<Resource> getAllResources() {
        return new ArrayList<>(resources);
    }

    public static Resource findResourceById(String resourceId) {
        return resources.stream()
                .filter(r -> r.getId().equals(resourceId))
                .findFirst()
                .orElse(null);
    }

    public static boolean deleteResource(String resourceId) {
        return resources.removeIf(r -> r.getId().equals(resourceId));
    }

    // Reservation Management
    public static ResourceReservation createReservation(String resourceId, String userId, String userName,
                                                        LocalDateTime startDateTime, LocalDateTime endDateTime,
                                                        String priority) {
        Resource resource = findResourceById(resourceId);
        if (resource == null) {
            return null;
        }
        if (checkConflict(resourceId, startDateTime, endDateTime)) {
            return null; // Conflict detected
        }
        ResourceReservation reservation = new ResourceReservation(
            resourceId, resource.getName(), userId, userName, startDateTime, endDateTime, priority
        );
        reservations.add(reservation);
        return reservation;
    }

    public static List<ResourceReservation> getAllReservations() {
        return new ArrayList<>(reservations);
    }

    public static List<ResourceReservation> getPendingReservations() {
        return reservations.stream()
                .filter(r -> r.getStatus().equals("PENDING"))
                .sorted((r1, r2) -> {
                    int priorityOrder = getPriorityOrder(r2.getPriority()) - getPriorityOrder(r1.getPriority());
                    if (priorityOrder != 0) return priorityOrder;
                    return r1.getStartDateTime().compareTo(r2.getStartDateTime());
                })
                .collect(Collectors.toList());
    }

    public static List<ResourceReservation> getReservationsByUser(String userId) {
        return reservations.stream()
                .filter(r -> r.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public static List<ResourceReservation> getReservationsByResource(String resourceId) {
        return reservations.stream()
                .filter(r -> r.getResourceId().equals(resourceId))
                .collect(Collectors.toList());
    }

    public static boolean approveReservation(String reservationId, String approvedBy) {
        ResourceReservation reservation = reservations.stream()
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
                "Your reservation for " + reservation.getResourceName() + " has been approved."
            );
            return true;
        }
        return false;
    }

    public static boolean rejectReservation(String reservationId, String rejectedBy, String reason) {
        ResourceReservation reservation = reservations.stream()
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
                "Your reservation for " + reservation.getResourceName() + " has been rejected. Reason: " + reason
            );
            return true;
        }
        return false;
    }

    public static boolean cancelReservation(String reservationId) {
        ResourceReservation reservation = reservations.stream()
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

    private static boolean checkConflict(String resourceId, LocalDateTime start, LocalDateTime end) {
        return reservations.stream()
                .filter(r -> r.getResourceId().equals(resourceId))
                .filter(r -> r.getStatus().equals("APPROVED") || r.getStatus().equals("PENDING"))
                .anyMatch(r -> {
                    LocalDateTime rStart = r.getStartDateTime();
                    LocalDateTime rEnd = r.getEndDateTime();
                    return (start.isBefore(rEnd) && end.isAfter(rStart));
                });
    }

    private static int getPriorityOrder(String priority) {
        switch (priority) {
            case "URGENT": return 4;
            case "HIGH": return 3;
            case "MEDIUM": return 2;
            case "LOW": return 1;
            default: return 0;
        }
    }

    private static String getUserEmail(String userId) {
        var user = UserService.findByUsername(userId);
        return user != null ? user.getEmail() : userId + "@example.com";
    }
}

