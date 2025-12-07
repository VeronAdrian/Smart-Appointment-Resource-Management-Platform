package com.smartplatform.user.service;

import com.smartplatform.user.model.Appointment;
import com.smartplatform.user.model.AvailabilitySlot;
import com.smartplatform.user.model.Resource;
import com.smartplatform.user.model.ResourceReservation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
        private final AppointmentService appointmentService;
        private final ResourceService resourceService;
        private final AvailabilityService availabilityService;

        public Map<String, Object> getResourceUsageStats() {
                Map<String, Object> stats = new HashMap<>();
                List<ResourceReservation> allReservations = resourceService.getAllReservations();
                List<Resource> allResources = resourceService.getAllResources();

                // Total resources
                stats.put("totalResources", allResources.size());

                // Total reservations
                stats.put("totalReservations", allReservations.size());

                // Approved reservations
                long approvedCount = allReservations.stream().filter(r -> r.getStatus().equals("APPROVED")).count();
                stats.put("approvedReservations", approvedCount);

                // Pending reservations
                long pendingCount = allReservations.stream().filter(r -> r.getStatus().equals("PENDING")).count();
                stats.put("pendingReservations", pendingCount);

                // Most used resources (top 5)
                Map<String, Long> resourceUsage = allReservations.stream()
                                .filter(
                                                r -> r.getStatus().equals("APPROVED")
                                                                || r.getStatus().equals("COMPLETED"))
                                .collect(
                                                Collectors.groupingBy(
                                                                ResourceReservation::getResourceName,
                                                                Collectors.counting()));
                List<Map.Entry<String, Long>> topResources = resourceUsage.entrySet().stream()
                                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                                .limit(5)
                                .collect(Collectors.toList());
                stats.put("topResources", topResources);

                // Reservations by priority
                Map<String, Long> priorityStats = allReservations.stream()
                                .collect(
                                                Collectors.groupingBy(
                                                                ResourceReservation::getPriority,
                                                                Collectors.counting()));
                stats.put("reservationsByPriority", priorityStats);

                // Reservations by status
                Map<String, Long> statusStats = allReservations.stream()
                                .collect(
                                                Collectors.groupingBy(
                                                                ResourceReservation::getStatus, Collectors.counting()));
                stats.put("reservationsByStatus", statusStats);

                // Active reservations (current time within reservation period)
                LocalDateTime now = LocalDateTime.now();
                long activeReservations = allReservations.stream()
                                .filter(r -> r.getStatus().equals("APPROVED"))
                                .filter(
                                                r -> now.isAfter(r.getStartDateTime())
                                                                && now.isBefore(r.getEndDateTime()))
                                .count();
                stats.put("activeReservations", activeReservations);

                return stats;
        }

        // Appointment Trends
        public Map<String, Object> getAppointmentTrends() {
                Map<String, Object> trends = new HashMap<>();
                List<Appointment> allAppointments = appointmentService.getAllAppointments();
                LocalDate today = LocalDate.now();

                // Last 7 days appointment count
                List<Map<String, Object>> dailyTrends = new ArrayList<>();
                for (int i = 6; i >= 0; i--) {
                        LocalDate date = today.minusDays(i);
                        long count = allAppointments.stream()
                                        .filter(apt -> apt.getAppointmentDateTime().toLocalDate().equals(date))
                                        .count();
                        Map<String, Object> dayData = new HashMap<>();
                        dayData.put("date", date.toString());
                        dayData.put("count", count);
                        dailyTrends.add(dayData);
                }
                trends.put("dailyTrends", dailyTrends);

                // Appointments by status
                Map<String, Long> statusCounts = allAppointments.stream()
                                .collect(
                                                Collectors.groupingBy(
                                                                Appointment::getStatus, Collectors.counting()));
                trends.put("statusBreakdown", statusCounts);

                // Total appointments
                trends.put("totalAppointments", allAppointments.size());

                // Upcoming appointments (next 7 days)
                long upcoming = allAppointments.stream()
                                .filter(apt -> apt.getAppointmentDateTime().toLocalDate().isAfter(today))
                                .filter(
                                                apt -> apt.getAppointmentDateTime()
                                                                .toLocalDate()
                                                                .isBefore(today.plusDays(8)))
                                .count();
                trends.put("upcomingAppointments", upcoming);

                return trends;
        }

        // Staff Utilization
        public Map<String, Object> getStaffUtilization() {
                Map<String, Object> utilization = new HashMap<>();
                List<AvailabilitySlot> allSlots = availabilityService.getAllSlots();

                // Staff utilization (booked slots / total slots)
                Map<String, Map<String, Long>> staffStats = new HashMap<>();
                allSlots.forEach(
                                slot -> {
                                        String staff = slot.getStaffUsername();
                                        staffStats.putIfAbsent(staff, new HashMap<>());
                                        staffStats
                                                        .get(staff)
                                                        .put(
                                                                        "totalSlots",
                                                                        staffStats.get(staff).getOrDefault("totalSlots",
                                                                                        0L) + 1);
                                        if (slot.isBooked()) {
                                                staffStats
                                                                .get(staff)
                                                                .put(
                                                                                "bookedSlots",
                                                                                staffStats.get(staff).getOrDefault(
                                                                                                "bookedSlots", 0L) + 1);
                                        }
                                });

                // Calculate utilization percentage
                List<Map<String, Object>> staffUtilization = new ArrayList<>();
                staffStats.forEach(
                                (staff, stats) -> {
                                        long total = stats.getOrDefault("totalSlots", 0L);
                                        long booked = stats.getOrDefault("bookedSlots", 0L);
                                        double utilizationPercent = total > 0 ? (booked * 100.0 / total) : 0;

                                        Map<String, Object> staffData = new HashMap<>();
                                        staffData.put("staffName", staff);
                                        staffData.put("totalSlots", total);
                                        staffData.put("bookedSlots", booked);
                                        staffData.put(
                                                        "utilizationPercent",
                                                        Math.round(utilizationPercent * 10.0) / 10.0);
                                        staffUtilization.add(staffData);
                                });

                utilization.put("staffUtilization", staffUtilization);
                utilization.put("totalStaff", staffStats.size());

                // Average utilization
                double avgUtilization = staffUtilization.stream()
                                .mapToDouble(s -> (Double) s.get("utilizationPercent"))
                                .average()
                                .orElse(0.0);
                utilization.put("averageUtilization", Math.round(avgUtilization * 10.0) / 10.0);

                return utilization;
        }

        // Resource Occupancy
        public Map<String, Object> getResourceOccupancy() {
                Map<String, Object> occupancy = new HashMap<>();
                List<Resource> allResources = resourceService.getAllResources();
                List<ResourceReservation> allReservations = resourceService.getAllReservations();
                LocalDateTime now = LocalDateTime.now();

                // Resource occupancy rate
                List<Map<String, Object>> resourceOccupancy = new ArrayList<>();
                allResources.forEach(
                                resource -> {
                                        long totalReservations = allReservations.stream()
                                                        .filter(r -> r.getResourceId().equals(resource.getId()))
                                                        .filter(
                                                                        r -> r.getStatus().equals("APPROVED")
                                                                                        || r.getStatus().equals(
                                                                                                        "COMPLETED"))
                                                        .count();

                                        long activeReservations = allReservations.stream()
                                                        .filter(r -> r.getResourceId().equals(resource.getId()))
                                                        .filter(r -> r.getStatus().equals("APPROVED"))
                                                        .filter(
                                                                        r -> now.isAfter(r.getStartDateTime())
                                                                                        && now.isBefore(r
                                                                                                        .getEndDateTime()))
                                                        .count();

                                        Map<String, Object> resourceData = new HashMap<>();
                                        resourceData.put("resourceName", resource.getName());
                                        resourceData.put("resourceType", resource.getType());
                                        resourceData.put("totalReservations", totalReservations);
                                        resourceData.put("activeReservations", activeReservations);
                                        resourceData.put("isAvailable", resource.isAvailable());
                                        resourceOccupancy.add(resourceData);
                                });

                occupancy.put("resourceOccupancy", resourceOccupancy);
                occupancy.put("totalResources", allResources.size());

                // Overall occupancy rate
                long totalActive = resourceOccupancy.stream().mapToLong(r -> (Long) r.get("activeReservations")).sum();
                double occupancyRate = allResources.size() > 0 ? (totalActive * 100.0 / allResources.size()) : 0;
                occupancy.put("overallOccupancyRate", Math.round(occupancyRate * 10.0) / 10.0);

                return occupancy;
        }

        // Revenue Insights (Mock - for demo purposes)
        public Map<String, Object> getRevenueInsights() {
                Map<String, Object> revenue = new HashMap<>();
                List<Appointment> allAppointments = appointmentService.getAllAppointments();
                List<ResourceReservation> allReservations = resourceService.getAllReservations();

                // Mock revenue calculation (assume $50 per appointment, $30 per resource
                // reservation)
                long confirmedAppointments = allAppointments.stream()
                                .filter(
                                                apt -> apt.getStatus().equals("CONFIRMED")
                                                                || apt.getStatus().equals("PENDING"))
                                .count();
                long approvedReservations = allReservations.stream().filter(r -> r.getStatus().equals("APPROVED"))
                                .count();

                double appointmentRevenue = confirmedAppointments * 50.0;
                double resourceRevenue = approvedReservations * 30.0;
                double totalRevenue = appointmentRevenue + resourceRevenue;

                revenue.put("appointmentRevenue", Math.round(appointmentRevenue * 100.0) / 100.0);
                revenue.put("resourceRevenue", Math.round(resourceRevenue * 100.0) / 100.0);
                revenue.put("totalRevenue", Math.round(totalRevenue * 100.0) / 100.0);

                // Monthly revenue trend (last 6 months - mock data)
                List<Map<String, Object>> monthlyTrends = new ArrayList<>();
                LocalDate today = LocalDate.now();
                for (int i = 5; i >= 0; i--) {
                        LocalDate monthStart = today.minusMonths(i).withDayOfMonth(1);
                        Map<String, Object> monthData = new HashMap<>();
                        monthData.put("month", monthStart.toString().substring(0, 7)); // YYYY-MM
                        // Mock: base revenue + random variation
                        double mockRevenue = 5000 + (Math.random() * 3000);
                        monthData.put("revenue", Math.round(mockRevenue * 100.0) / 100.0);
                        monthlyTrends.add(monthData);
                }
                revenue.put("monthlyTrends", monthlyTrends);

                // Revenue by service type
                Map<String, Double> revenueByType = new HashMap<>();
                revenueByType.put("Appointments", appointmentRevenue);
                revenueByType.put("Resources", resourceRevenue);
                revenue.put("revenueByType", revenueByType);

                return revenue;
        }

        // Comprehensive Dashboard Data
        public Map<String, Object> getDashboardData() {
                Map<String, Object> dashboard = new HashMap<>();
                dashboard.put("appointmentTrends", getAppointmentTrends());
                dashboard.put("staffUtilization", getStaffUtilization());
                dashboard.put("resourceOccupancy", getResourceOccupancy());
                dashboard.put("revenueInsights", getRevenueInsights());
                dashboard.put("resourceStats", getResourceUsageStats());
                return dashboard;
        }
}
