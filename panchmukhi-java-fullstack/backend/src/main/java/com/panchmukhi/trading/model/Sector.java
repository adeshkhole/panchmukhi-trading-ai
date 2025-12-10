package com.panchmukhi.trading.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sectors")
public class Sector {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @NotBlank
    @Size(max = 100)
    @Column(name = "sector_name", unique = true)
    private String sectorName;
    
    @Size(max = 500)
    @Column(name = "description")
    private String description;
    
    @NotBlank
    @Size(max = 50)
    @Column(name = "category")
    private String category;
    
    @Column(name = "market_cap", precision = 20, scale = 2)
    private BigDecimal marketCap;
    
    @Column(name = "pe_ratio", precision = 8, scale = 2)
    private BigDecimal peRatio;
    
    @Column(name = "dividend_yield", precision = 8, scale = 4)
    private BigDecimal dividendYield;
    
    @Column(name = "performance_1d", precision = 8, scale = 4)
    private BigDecimal performance1d;
    
    @Column(name = "performance_1w", precision = 8, scale = 4)
    private BigDecimal performance1w;
    
    @Column(name = "performance_1m", precision = 8, scale = 4)
    private BigDecimal performance1m;
    
    @Column(name = "performance_3m", precision = 8, scale = 4)
    private BigDecimal performance3m;
    
    @Column(name = "performance_1y", precision = 8, scale = 4)
    private BigDecimal performance1y;
    
    @Column(name = "top_stocks", length = 500)
    private String topStocks;
    
    @Column(name = "stock_count")
    private Integer stockCount;
    
    @Column(name = "risk_level", length = 20)
    private String riskLevel;
    
    @Column(name = "growth_outlook", length = 20)
    private String growthOutlook;
    
    @Column(name = "key_drivers", columnDefinition = "TEXT")
    private String keyDrivers;
    
    @Column(name = "challenges", columnDefinition = "TEXT")
    private String challenges;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getSectorName() { return sectorName; }
    public void setSectorName(String sectorName) { this.sectorName = sectorName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public BigDecimal getMarketCap() { return marketCap; }
    public void setMarketCap(BigDecimal marketCap) { this.marketCap = marketCap; }
    
    public BigDecimal getPeRatio() { return peRatio; }
    public void setPeRatio(BigDecimal peRatio) { this.peRatio = peRatio; }
    
    public BigDecimal getDividendYield() { return dividendYield; }
    public void setDividendYield(BigDecimal dividendYield) { this.dividendYield = dividendYield; }
    
    public BigDecimal getPerformance1d() { return performance1d; }
    public void setPerformance1d(BigDecimal performance1d) { this.performance1d = performance1d; }
    
    public BigDecimal getPerformance1w() { return performance1w; }
    public void setPerformance1w(BigDecimal performance1w) { this.performance1w = performance1w; }
    
    public BigDecimal getPerformance1m() { return performance1m; }
    public void setPerformance1m(BigDecimal performance1m) { this.performance1m = performance1m; }
    
    public BigDecimal getPerformance3m() { return performance3m; }
    public void setPerformance3m(BigDecimal performance3m) { this.performance3m = performance3m; }
    
    public BigDecimal getPerformance1y() { return performance1y; }
    public void setPerformance1y(BigDecimal performance1y) { this.performance1y = performance1y; }
    
    public String getTopStocks() { return topStocks; }
    public void setTopStocks(String topStocks) { this.topStocks = topStocks; }
    
    public Integer getStockCount() { return stockCount; }
    public void setStockCount(Integer stockCount) { this.stockCount = stockCount; }
    
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    
    public String getGrowthOutlook() { return growthOutlook; }
    public void setGrowthOutlook(String growthOutlook) { this.growthOutlook = growthOutlook; }
    
    public String getKeyDrivers() { return keyDrivers; }
    public void setKeyDrivers(String keyDrivers) { this.keyDrivers = keyDrivers; }
    
    public String getChallenges() { return challenges; }
    public void setChallenges(String challenges) { this.challenges = challenges; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}