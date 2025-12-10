package com.panchmukhi.trading.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sector_intelligence")
public class SectorIntelligence {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(nullable = false, unique = true)
    private String sectorName; // "IT", "Banking", "Pharma", etc.

    private String currentTrend; // "Growing", "Declining", "Stable"

    private BigDecimal sentimentScore; // -1.0 to +1.0

    private Integer newsCount; // Articles in last 24h

    @Column(columnDefinition = "TEXT")
    private String topStory;

    @Column(columnDefinition = "TEXT")
    private String activeCompanies; // JSON array of company names

    @Column(columnDefinition = "TEXT")
    private String insights; // JSON field with daily insights

    private LocalDateTime lastUpdated;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }

    // Constructors
    public SectorIntelligence() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public String getCurrentTrend() {
        return currentTrend;
    }

    public void setCurrentTrend(String currentTrend) {
        this.currentTrend = currentTrend;
    }

    public BigDecimal getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(BigDecimal sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public Integer getNewsCount() {
        return newsCount;
    }

    public void setNewsCount(Integer newsCount) {
        this.newsCount = newsCount;
    }

    public String getTopStory() {
        return topStory;
    }

    public void setTopStory(String topStory) {
        this.topStory = topStory;
    }

    public String getActiveCompanies() {
        return activeCompanies;
    }

    public void setActiveCompanies(String activeCompanies) {
        this.activeCompanies = activeCompanies;
    }

    public String getInsights() {
        return insights;
    }

    public void setInsights(String insights) {
        this.insights = insights;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
