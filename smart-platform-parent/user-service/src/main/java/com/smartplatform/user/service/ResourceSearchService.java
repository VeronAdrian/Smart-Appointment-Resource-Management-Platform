package com.smartplatform.user.service;

import com.smartplatform.user.model.document.ResourceDocument;
import com.smartplatform.user.repository.ResourceSearchRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for searching resources using Elasticsearch. Provides
 * high-performance search and
 * filtering capabilities.
 */
@Service
public class ResourceSearchService {

    @Autowired
    private ResourceSearchRepository resourceSearchRepository;

    /** Search resources by type. */
    public List<ResourceDocument> searchByType(String type, String tenantId) {
        return resourceSearchRepository.findByTypeAndTenantId(type, tenantId);
    }

    /** Search available resources. */
    public List<ResourceDocument> searchAvailableResources(String tenantId) {
        return resourceSearchRepository.findByIsAvailableAndTenantId(true, tenantId);
    }

    /** Search resources by name or description. */
    public List<ResourceDocument> searchByNameOrDescription(String searchTerm, String tenantId) {
        return resourceSearchRepository.findByNameOrDescriptionAndTenant(searchTerm, tenantId);
    }

    /** Get high-rated resources. */
    public List<ResourceDocument> getHighRatedResources(String tenantId) {
        return resourceSearchRepository.findHighRatedResources(tenantId);
    }

    /** Get least used resources (good for balancing load). */
    public List<ResourceDocument> getLeastUsedResources(String tenantId) {
        return resourceSearchRepository.findLeastUsedResources(tenantId);
    }

    /** Search by type and availability. */
    public List<ResourceDocument> searchByTypeAndAvailability(
            String type, boolean available, String tenantId) {
        return resourceSearchRepository.findByTypeAndIsAvailableAndTenantId(
                type, available, tenantId);
    }

    /** Search by location. */
    public List<ResourceDocument> searchByLocation(String location, String tenantId) {
        return resourceSearchRepository.findByLocationAndTenantId(location, tenantId);
    }

    /** Advanced search with multiple filters. */
    public List<ResourceDocument> advancedSearch(
            String type,
            String location,
            boolean availableOnly,
            String searchTerm,
            String tenantId,
            int page,
            int size) {
        List<ResourceDocument> results =
                StreamSupport.stream(resourceSearchRepository.findAll().spliterator(), false)
                        .filter(res -> res.getTenantId().equals(tenantId))
                        .filter(res -> type == null || res.getType().equals(type))
                        .filter(res -> location == null || res.getLocation().equals(location))
                        .filter(res -> !availableOnly || res.isAvailable())
                        .filter(
                                res ->
                                        searchTerm == null
                                                || res.getName()
                                                        .toLowerCase()
                                                        .contains(searchTerm.toLowerCase())
                                                || res.getDescription()
                                                        .toLowerCase()
                                                        .contains(searchTerm.toLowerCase()))
                        .collect(Collectors.toList());
        
        // Apply pagination
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, results.size());

        if (fromIndex >= results.size()) {
            return List.of();
        }

        return results.subList(fromIndex, toIndex);
    }

    /** Index a resource for searching. */
    public ResourceDocument indexResource(ResourceDocument resource) {
        return resourceSearchRepository.save(resource);
    }

    /** Delete resource from index. */
    public void deleteResourceFromIndex(String resourceId) {
        resourceSearchRepository.deleteById(resourceId);
    }

    /** Update resource in index. */
    public ResourceDocument updateResourceIndex(ResourceDocument resource) {
        return resourceSearchRepository.save(resource);
    }

    /** Bulk index resources. */
    public void bulkIndexResources(List<ResourceDocument> resources) {
        resourceSearchRepository.saveAll(resources);
    }

    /** Clear all resources from index. */
    public void clearAllResources() {
        resourceSearchRepository.deleteAll();
    }
}
