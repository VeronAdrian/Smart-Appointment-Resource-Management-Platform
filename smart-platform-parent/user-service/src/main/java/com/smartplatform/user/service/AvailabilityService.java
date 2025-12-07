package com.smartplatform.user.service;

import com.smartplatform.user.model.AvailabilitySlot;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvailabilityService {
    private final List<AvailabilitySlot> slots = new ArrayList<>();
    private final TenantContext tenantContext;

    public AvailabilitySlot createSlot(
            String staffUsername,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            String tenantId) {
        if (checkConflict(staffUsername, startDateTime, endDateTime, tenantId)) {
            return null; // Conflict detected
        }
        AvailabilitySlot slot = new AvailabilitySlot(staffUsername, startDateTime, endDateTime, tenantId);
        slots.add(slot);
        return slot;
    }

    public List<AvailabilitySlot> getAllSlots() {
        String tenantId = tenantContext.getCurrentTenantId();
        return slots.stream()
                .filter(
                        slot -> tenantId == null
                                || slot.getTenantId() == null
                                || slot.getTenantId().equals(tenantId))
                .collect(Collectors.toList());
    }

    public List<AvailabilitySlot> getAvailableSlots() {
        String tenantId = tenantContext.getCurrentTenantId();
        return slots.stream()
                .filter(slot -> !slot.isBooked())
                .filter(
                        slot -> tenantId == null
                                || slot.getTenantId() == null
                                || slot.getTenantId().equals(tenantId))
                .collect(Collectors.toList());
    }

    public List<AvailabilitySlot> getSlotsByStaff(String staffUsername) {
        String tenantId = tenantContext.getCurrentTenantId();
        return slots.stream()
                .filter(slot -> slot.getStaffUsername().equalsIgnoreCase(staffUsername))
                .filter(
                        slot -> tenantId == null
                                || slot.getTenantId() == null
                                || slot.getTenantId().equals(tenantId))
                .collect(Collectors.toList());
    }

    public boolean checkConflict(
            String staffUsername, LocalDateTime start, LocalDateTime end, String tenantId) {
        return slots.stream()
                .filter(slot -> slot.getStaffUsername().equalsIgnoreCase(staffUsername))
                .filter(
                        slot -> tenantId == null
                                || slot.getTenantId() == null
                                || slot.getTenantId().equals(tenantId))
                .anyMatch(
                        slot -> {
                            LocalDateTime slotStart = slot.getStartDateTime();
                            LocalDateTime slotEnd = slot.getEndDateTime();
                            return (start.isBefore(slotEnd) && end.isAfter(slotStart));
                        });
    }

    public AvailabilitySlot findSlotById(String slotId) {
        String tenantId = tenantContext.getCurrentTenantId();
        return slots.stream()
                .filter(slot -> slot.getId().equals(slotId))
                .filter(
                        slot -> tenantId == null
                                || slot.getTenantId() == null
                                || slot.getTenantId().equals(tenantId))
                .findFirst()
                .orElse(null);
    }

    public List<AvailabilitySlot> getAvailableSlotsInRange(
            LocalDateTime start, LocalDateTime end, String tenantId) {
        return slots.stream()
                .filter(slot -> !slot.isBooked())
                .filter(
                        slot ->
                                tenantId == null
                                        || slot.getTenantId() == null
                                        || slot.getTenantId().equals(tenantId))
                .filter(
                        slot ->
                                slot.getStartDateTime().isAfter(start)
                                        || slot.getStartDateTime().isEqual(start))
                .filter(
                        slot ->
                                slot.getEndDateTime().isBefore(end)
                                        || slot.getEndDateTime().isEqual(end))
                .collect(Collectors.toList());
    }
}; 
                                
                                 
                                
                                 
                                 
                                