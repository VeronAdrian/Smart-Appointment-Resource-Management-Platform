package com.smartplatform.user.service;

import com.smartplatform.user.model.AvailabilitySlot;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AvailabilityService {
    private static final List<AvailabilitySlot> slots = new ArrayList<>();

    public static AvailabilitySlot createSlot(String staffUsername, LocalDateTime startDateTime, LocalDateTime endDateTime, String tenantId) {
        if (checkConflict(staffUsername, startDateTime, endDateTime, tenantId)) {
            return null; // Conflict detected
        }
        AvailabilitySlot slot = new AvailabilitySlot(staffUsername, startDateTime, endDateTime, tenantId);
        slots.add(slot);
        return slot;
    }

    public static List<AvailabilitySlot> getAllSlots() {
        String tenantId = TenantContext.getCurrentTenantId();
        return slots.stream()
                .filter(slot -> tenantId == null || slot.getTenantId() == null || slot.getTenantId().equals(tenantId))
                .collect(Collectors.toList());
    }

    public static List<AvailabilitySlot> getAvailableSlots() {
        String tenantId = TenantContext.getCurrentTenantId();
        return slots.stream()
                .filter(slot -> !slot.isBooked())
                .filter(slot -> tenantId == null || slot.getTenantId() == null || slot.getTenantId().equals(tenantId))
                .collect(Collectors.toList());
    }

    public static List<AvailabilitySlot> getSlotsByStaff(String staffUsername) {
        String tenantId = TenantContext.getCurrentTenantId();
        return slots.stream()
                .filter(slot -> slot.getStaffUsername().equalsIgnoreCase(staffUsername))
                .filter(slot -> tenantId == null || slot.getTenantId() == null || slot.getTenantId().equals(tenantId))
                .collect(Collectors.toList());
    }

    public static boolean checkConflict(String staffUsername, LocalDateTime start, LocalDateTime end, String tenantId) {
        return slots.stream()
                .filter(slot -> slot.getStaffUsername().equalsIgnoreCase(staffUsername))
                .filter(slot -> tenantId == null || slot.getTenantId() == null || slot.getTenantId().equals(tenantId))
                .anyMatch(slot -> {
                    LocalDateTime slotStart = slot.getStartDateTime();
                    LocalDateTime slotEnd = slot.getEndDateTime();
                    return (start.isBefore(slotEnd) && end.isAfter(slotStart));
                });
    }

    public static AvailabilitySlot findSlotById(String slotId) {
        String tenantId = TenantContext.getCurrentTenantId();
        return slots.stream()
                .filter(slot -> slot.getId().equals(slotId))
                .filter(slot -> tenantId == null || slot.getTenantId() == null || slot.getTenantId().equals(tenantId))
                .findFirst()
                .orElse(null);
    }
}

