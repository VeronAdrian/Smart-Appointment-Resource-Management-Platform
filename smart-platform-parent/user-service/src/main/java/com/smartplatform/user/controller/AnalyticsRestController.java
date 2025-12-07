package com.smartplatform.user.controller;

import com.smartplatform.user.service.AnalyticsService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*") // Allow CORS for React app
@RequiredArgsConstructor
public class AnalyticsRestController {

    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public Map<String, Object> getDashboardData() {
        return analyticsService.getDashboardData();
    }

    @GetMapping("/appointments")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public Map<String, Object> getAppointmentTrends() {
        return analyticsService.getAppointmentTrends();
    }

    @GetMapping("/staff")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public Map<String, Object> getStaffUtilization() {
        return analyticsService.getStaffUtilization();
    }

    @GetMapping("/resources")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public Map<String, Object> getResourceOccupancy() {
        return analyticsService.getResourceOccupancy();
    }

    @GetMapping("/revenue")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public Map<String, Object> getRevenueInsights() {
        return analyticsService.getRevenueInsights();
    }
}
