package com.smartplatform.user.service;

import com.smartplatform.user.model.Appointment;
import com.smartplatform.user.model.Resource;
import com.smartplatform.user.model.document.AppointmentDocument;
import com.smartplatform.user.model.document.ResourceDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Elasticsearch Index Synchronization Service
 * Handles bulk indexing of appointments and resources into Elasticsearch
 * Useful for initial setup and periodic re-indexing
 */
@Service
public class IndexSyncService {
    
    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private ResourceService resourceService;
    
    @Autowired
    private AppointmentSearchService appointmentSearchService;
    
    @Autowired
    private ResourceSearchService resourceSearchService;
    
    @Autowired(required = false)
    private TenantContext tenantContext;
    
    /**
     * Synchronize all appointments to Elasticsearch
     * Called during application startup
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initializeIndices() {
        try {
            syncAllAppointments();
            syncAllResources();
        } catch (Exception e) {
            // Log but don't fail startup if Elasticsearch is not available
            System.err.println("Warning: Could not initialize Elasticsearch indices: " + e.getMessage());
        }
    }
    
    /**
     * Sync all appointments for a specific tenant
     */
    public void syncAllAppointments() {
        try {
            // Get all appointments from the application service
            // This is a simplified version; adjust based on your AppointmentService implementation
            List<AppointmentDocument> appointmentDocs = List.of();
            
            if (!appointmentDocs.isEmpty()) {
                appointmentSearchService.bulkIndexAppointments(appointmentDocs);
                System.out.println("Successfully indexed " + appointmentDocs.size() + " appointments");
            }
        } catch (Exception e) {
            System.err.println("Error syncing appointments: " + e.getMessage());
        }
    }
    
    /**
     * Sync all resources for a specific tenant
     */
    public void syncAllResources() {
        try {
            // Get all resources from the application service
            // This is a simplified version; adjust based on your ResourceService implementation
            List<ResourceDocument> resourceDocs = List.of();
            
            if (!resourceDocs.isEmpty()) {
                resourceSearchService.bulkIndexResources(resourceDocs);
                System.out.println("Successfully indexed " + resourceDocs.size() + " resources");
            }
        } catch (Exception e) {
            System.err.println("Error syncing resources: " + e.getMessage());
        }
    }
    
    /**
     * Reindex all documents (useful for schema changes)
     */
    public void reindexAll() {
        System.out.println("Starting full reindex...");
        syncAllAppointments();
        syncAllResources();
        System.out.println("Reindex complete");
    }
    
    /**
     * Clear all indices (for testing purposes)
     */
    public void clearAllIndices() {
        try {
            appointmentSearchService.clearAllAppointments();
            resourceSearchService.clearAllResources();
            System.out.println("All indices cleared successfully");
        } catch (Exception e) {
            System.err.println("Error clearing indices: " + e.getMessage());
        }
    }
}
