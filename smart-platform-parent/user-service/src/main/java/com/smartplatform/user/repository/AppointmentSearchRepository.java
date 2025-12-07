package com.smartplatform.user.repository;

import com.smartplatform.user.model.document.AppointmentDocument;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/** Elasticsearch repository for Appointment searches */
@Repository
public interface AppointmentSearchRepository
        extends ElasticsearchRepository<AppointmentDocument, String> {

    /** Find appointments by client username and tenant */
    List<AppointmentDocument> findByClientUsernameAndTenantId(
            String clientUsername, String tenantId);

    /** Find appointments by staff username and tenant */
    List<AppointmentDocument> findByStaffUsernameAndTenantId(String staffUsername, String tenantId);

    /** Find appointments by status and tenant */
    List<AppointmentDocument> findByStatusAndTenantId(String status, String tenantId);

    /** Find appointments by appointment type and tenant */
    List<AppointmentDocument> findByAppointmentTypeAndTenantId(
            String appointmentType, String tenantId);

    /** Find appointments within a date range for a tenant */
    @Query(
            "{\"bool\": {\"must\": ["
                    + "{\"range\": {\"appointmentDateTime\": {\"gte\": \"?0\", \"lte\": \"?1\"}}},"
                    + "{\"term\": {\"tenantId\": \"?2\"}}"
                    + "]}}")
    List<AppointmentDocument> findByDateRangeAndTenant(
            LocalDateTime startDate, LocalDateTime endDate, String tenantId);

    /** Find appointments by staff and date range */
    @Query(
            "{\"bool\": {\"must\": ["
                    + "{\"term\": {\"staffUsername\": \"?0\"}},"
                    + "{\"range\": {\"appointmentDateTime\": {\"gte\": \"?1\", \"lte\": \"?2\"}}},"
                    + "{\"term\": {\"tenantId\": \"?3\"}}"
                    + "]}}")
    List<AppointmentDocument> findByStaffAndDateRange(
            String staffUsername, LocalDateTime startDate, LocalDateTime endDate, String tenantId);

    /** Find appointments by client with high rating */
    @Query(
            "{\"bool\": {\"must\": ["
                    + "{\"term\": {\"clientUsername\": \"?0\"}},"
                    + "{\"range\": {\"rating\": {\"gte\": 4.0}}},"
                    + "{\"term\": {\"tenantId\": \"?1\"}}"
                    + "]}}")
    List<AppointmentDocument> findHighRatedAppointments(String clientUsername, String tenantId);
}
