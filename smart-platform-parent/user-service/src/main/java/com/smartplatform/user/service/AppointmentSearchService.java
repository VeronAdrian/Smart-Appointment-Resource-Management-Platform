package com.smartplatform.user.service;

import com.smartplatform.user.model.document.AppointmentDocument;
import com.smartplatform.user.repository.AppointmentSearchRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for searching appointments using Elasticsearch Provides high-performance search
 * capabilities
 */
@Service
public class AppointmentSearchService {

    @Autowired private AppointmentSearchRepository appointmentSearchRepository;

    /** Search appointments by client username */
    public List<AppointmentDocument> searchByClient(String clientUsername, String tenantId) {
        return appointmentSearchRepository.findByClientUsernameAndTenantId(
                clientUsername, tenantId);
    }

    /** Search appointments by staff member */
    public List<AppointmentDocument> searchByStaff(String staffUsername, String tenantId) {
        return appointmentSearchRepository.findByStaffUsernameAndTenantId(staffUsername, tenantId);
    }

    /** Search appointments by status */
    public List<AppointmentDocument> searchByStatus(String status, String tenantId) {
        return appointmentSearchRepository.findByStatusAndTenantId(status, tenantId);
    }

    /** Search appointments by type */
    public List<AppointmentDocument> searchByType(String appointmentType, String tenantId) {
        return appointmentSearchRepository.findByAppointmentTypeAndTenantId(
                appointmentType, tenantId);
    }

    /** Search appointments within a date range */
    public List<AppointmentDocument> searchByDateRange(
            LocalDateTime startDate, LocalDateTime endDate, String tenantId) {
        return appointmentSearchRepository.findByDateRangeAndTenant(startDate, endDate, tenantId);
    }

    /** Search appointments for a specific staff member within a date range */
    public List<AppointmentDocument> searchStaffSchedule(
            String staffUsername, LocalDateTime startDate, LocalDateTime endDate, String tenantId) {
        return appointmentSearchRepository.findByStaffAndDateRange(
                staffUsername, startDate, endDate, tenantId);
    }

    /** Get high-rated appointments for a client */
    public List<AppointmentDocument> getHighRatedAppointments(
            String clientUsername, String tenantId) {
        return appointmentSearchRepository.findHighRatedAppointments(clientUsername, tenantId);
    }

    /** Search by multiple criteria with pagination */
    public List<AppointmentDocument> advancedSearch(
            String clientUsername,
            String status,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String tenantId,
            int page,
            int size) {
        List<AppointmentDocument> results =
                appointmentSearchRepository.findByDateRangeAndTenant(startDate, endDate, tenantId);

        // Filter by additional criteria
        results =
                results.stream()
                        .filter(
                                apt ->
                                        clientUsername == null
                                                || apt.getClientUsername().equals(clientUsername))
                        .filter(apt -> status == null || apt.getStatus().equals(status))
                        .collect(Collectors.toList());

        // Apply pagination
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, results.size());

        if (fromIndex >= results.size()) {
            return List.of();
        }

        return results.subList(fromIndex, toIndex);
    }

    /** Index an appointment for searching */
    public AppointmentDocument indexAppointment(AppointmentDocument appointment) {
        return appointmentSearchRepository.save(appointment);
    }

    /** Delete appointment from index */
    public void deleteAppointmentFromIndex(String appointmentId) {
        appointmentSearchRepository.deleteById(appointmentId);
    }

    /** Update appointment in index */
    public AppointmentDocument updateAppointmentIndex(AppointmentDocument appointment) {
        return appointmentSearchRepository.save(appointment);
    }

    /** Bulk index appointments */
    public void bulkIndexAppointments(List<AppointmentDocument> appointments) {
        appointmentSearchRepository.saveAll(appointments);
    }

    /** Clear all appointments from index */
    public void clearAllAppointments() {
        appointmentSearchRepository.deleteAll();
    }
}
