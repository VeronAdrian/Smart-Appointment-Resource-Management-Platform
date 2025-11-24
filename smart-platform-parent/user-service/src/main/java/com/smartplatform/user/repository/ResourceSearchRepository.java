package com.smartplatform.user.repository;

import com.smartplatform.user.model.document.ResourceDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Elasticsearch repository for Resource searches
 */
@Repository
public interface ResourceSearchRepository extends ElasticsearchRepository<ResourceDocument, String> {
    
    /**
     * Find resources by type and tenant
     */
    List<ResourceDocument> findByTypeAndTenantId(String type, String tenantId);
    
    /**
     * Find available resources by tenant
     */
    List<ResourceDocument> findByIsAvailableAndTenantId(boolean isAvailable, String tenantId);
    
    /**
     * Find resources by name (text search) and tenant
     */
    @Query("{\"bool\": {\"must\": [" +
            "{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"name\", \"description\"]}}," +
            "{\"term\": {\"tenantId\": \"?1\"}}" +
            "]}}")
    List<ResourceDocument> findByNameOrDescriptionAndTenant(String searchTerm, String tenantId);
    
    /**
     * Find highly rated resources
     */
    @Query("{\"bool\": {\"must\": [" +
            "{\"range\": {\"rating\": {\"gte\": 4.0}}}," +
            "{\"term\": {\"tenantId\": \"?0\"}}" +
            "]}}")
    List<ResourceDocument> findHighRatedResources(String tenantId);
    
    /**
     * Find least used resources (good for recommendation)
     */
    @Query("{\"bool\": {\"must\": [" +
            "{\"term\": {\"isAvailable\": true}}," +
            "{\"term\": {\"tenantId\": \"?0\"}}]," +
            "\"sort\": [{\"currentReservations\": {\"order\": \"asc\"}}]}}")
    List<ResourceDocument> findLeastUsedResources(String tenantId);
    
    /**
     * Find resources by type and availability
     */
    List<ResourceDocument> findByTypeAndIsAvailableAndTenantId(String type, boolean isAvailable, String tenantId);
    
    /**
     * Find resources by location and tenant
     */
    List<ResourceDocument> findByLocationAndTenantId(String location, String tenantId);
}
