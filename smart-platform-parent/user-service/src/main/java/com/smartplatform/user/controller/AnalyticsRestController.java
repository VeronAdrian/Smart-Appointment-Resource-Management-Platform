package com.smartplatform.user.controller;

import com.smartplatform.user.service.AnalyticsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*") // Allow CORS for React app
public class AnalyticsRestController {
    
    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public Map<String, Object> getDashboardData() {
        return AnalyticsService.getDashboardData();
    }
    
    @GetMapping("/appointments")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public Map<String, Object> getAppointmentTrends() {
        return AnalyticsService.getAppointmentTrends();
    }
    
    @GetMapping("/staff")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public Map<String, Object> getStaffUtilization() {
        return AnalyticsService.getStaffUtilization();
    }
    
    @GetMapping("/resources")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public Map<String, Object> getResourceOccupancy() {
        return AnalyticsService.getResourceOccupancy();
    }
    
    @GetMapping("/revenue")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPER_ADMIN')")
    public Map<String, Object> getRevenueInsights() {
        return AnalyticsService.getRevenueInsights();
    }
}

