package com.smartplatform.user.model.dto;

import java.util.List;

/** DTO for recommendation responses Contains combined recommendations for slots and resources */
public class RecommendationResponse {

    private List<SlotRecommendation> recommendedSlots;
    private List<ResourceRecommendation> recommendedResources;
    private String searchCriteria;
    private int totalResults;
    private long executionTimeMs;

    public RecommendationResponse() {}

    public RecommendationResponse(
            List<SlotRecommendation> recommendedSlots,
            List<ResourceRecommendation> recommendedResources,
            String searchCriteria,
            int totalResults,
            long executionTimeMs) {
        this.recommendedSlots = recommendedSlots;
        this.recommendedResources = recommendedResources;
        this.searchCriteria = searchCriteria;
        this.totalResults = totalResults;
        this.executionTimeMs = executionTimeMs;
    }

    // Getters and Setters
    public List<SlotRecommendation> getRecommendedSlots() {
        return recommendedSlots;
    }

    public void setRecommendedSlots(List<SlotRecommendation> recommendedSlots) {
        this.recommendedSlots = recommendedSlots;
    }

    public List<ResourceRecommendation> getRecommendedResources() {
        return recommendedResources;
    }

    public void setRecommendedResources(List<ResourceRecommendation> recommendedResources) {
        this.recommendedResources = recommendedResources;
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }
}
