package com.smartplatform.user.service;

import com.smartplatform.user.model.AvailabilitySlot;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AvailabilityService {
    private static final List<AvailabilitySlot> slots = new ArrayList<>();

    public static AvailabilitySlot createSlot(String staffUsername, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (checkConflict(staffUsername, startDateTime, endDateTime)) {
            return null; // Conflict detected
        }
        AvailabilitySlot slot = new AvailabilitySlot(staffUsername, startDateTime, endDateTime);
        slots.add(slot);
        return slot;
    }

    public static List<AvailabilitySlot> getAllSlots() {
        return new ArrayList<>(slots);
    }

    public static List<AvailabilitySlot> getAvailableSlots() {
        return slots.stream()
                .filter(slot -> !slot.isBooked())
                .collect(Collectors.toList());
    }

    public static List<AvailabilitySlot> getSlotsByStaff(String staffUsername) {
        return slots.stream()
                .filter(slot -> slot.getStaffUsername().equalsIgnoreCase(staffUsername))
                .collect(Collectors.toList());
    }

    public static boolean checkConflict(String staffUsername, LocalDateTime start, LocalDateTime end) {
        return slots.stream()
                .filter(slot -> slot.getStaffUsername().equalsIgnoreCase(staffUsername))
                .anyMatch(slot -> {
                    LocalDateTime slotStart = slot.getStartDateTime();
                    LocalDateTime slotEnd = slot.getEndDateTime();
                    return (start.isBefore(slotEnd) && end.isAfter(slotStart));
                });
    }

    public static AvailabilitySlot findSlotById(String slotId) {
        return slots.stream()
                .filter(slot -> slot.getId().equals(slotId))
                .findFirst()
                .orElse(null);
    }
}

