package com.panchmukhi.trading.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ipos")
public class IPO {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @NotBlank
    @Size(max = 100)
    @Column(name = "company_name")
    private String companyName;
    
    @NotBlank
    @Size(max = 20)
    @Column(name = "symbol")
    private String symbol;
    
    @Size(max = 10)
    @Column(name = "exchange")
    private String exchange;
    
    @NotNull
    @Column(name = "issue_size", precision = 15, scale = 2)
    private BigDecimal issueSize;
    
    @NotNull
    @Column(name = "price_band_lower", precision = 15, scale = 4)
    private BigDecimal priceBandLower;
    
    @NotNull
    @Column(name = "price_band_upper", precision = 15, scale = 4)
    private BigDecimal priceBandUpper;
    
    @Column(name = "final_price", precision = 15, scale = 4)
    private BigDecimal finalPrice;
    
    @NotNull
    @Column(name = "lot_size")
    private Integer lotSize;
    
    @Column(name = "issue_type", length = 20)
    private String issueType;
    
    @Column(name = "listing_date")
    private LocalDate listingDate;
    
    @Column(name = "bid_start_date")
    private LocalDate bidStartDate;
    
    @Column(name = "bid_end_date")
    private LocalDate bidEndDate;
    
    @Column(name = "allotment_date")
    private LocalDate allotmentDate;
    
    @Column(name = "refund_date")
    private LocalDate refundDate;
    
    @Column(name = "category", length = 50)
    private String category;
    
    @Column(name = "prospectus_url", length = 500)
    private String prospectusUrl;
    
    @Column(name = "company_description", columnDefinition = "TEXT")
    private String companyDescription;
    
    @Column(name = "strengths", columnDefinition = "TEXT")
    private String strengths;
    
    @Column(name = "risks", columnDefinition = "TEXT")
    private String risks;
    
    @Column(name = "subscription_qib", precision = 8, scale = 2)
    private BigDecimal subscriptionQIB;
    
    @Column(name = "subscription_nii", precision = 8, scale = 2)
    private BigDecimal subscriptionNII;
    
    @Column(name = "subscription_rii", precision = 8, scale = 2)
    private BigDecimal subscriptionRII;
    
    @Column(name = "subscription_total", precision = 8, scale = 2)
    private BigDecimal subscriptionTotal;
    
    @Column(name = "listing_gain", precision = 8, scale = 2)
    private BigDecimal listingGain;
    
    @Column(name = "current_status", length = 20)
    private String currentStatus;
    
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
    
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    
    public String getExchange() { return exchange; }
    public void setExchange(String exchange) { this.exchange = exchange; }
    
    public BigDecimal getIssueSize() { return issueSize; }
    public void setIssueSize(BigDecimal issueSize) { this.issueSize = issueSize; }
    
    public BigDecimal getPriceBandLower() { return priceBandLower; }
    public void setPriceBandLower(BigDecimal priceBandLower) { this.priceBandLower = priceBandLower; }
    
    public BigDecimal getPriceBandUpper() { return priceBandUpper; }
    public void setPriceBandUpper(BigDecimal priceBandUpper) { this.priceBandUpper = priceBandUpper; }
    
    public BigDecimal getFinalPrice() { return finalPrice; }
    public void setFinalPrice(BigDecimal finalPrice) { this.finalPrice = finalPrice; }
    
    public Integer getLotSize() { return lotSize; }
    public void setLotSize(Integer lotSize) { this.lotSize = lotSize; }
    
    public String getIssueType() { return issueType; }
    public void setIssueType(String issueType) { this.issueType = issueType; }
    
    public LocalDate getListingDate() { return listingDate; }
    public void setListingDate(LocalDate listingDate) { this.listingDate = listingDate; }
    
    public LocalDate getBidStartDate() { return bidStartDate; }
    public void setBidStartDate(LocalDate bidStartDate) { this.bidStartDate = bidStartDate; }
    
    public LocalDate getBidEndDate() { return bidEndDate; }
    public void setBidEndDate(LocalDate bidEndDate) { this.bidEndDate = bidEndDate; }
    
    public LocalDate getAllotmentDate() { return allotmentDate; }
    public void setAllotmentDate(LocalDate allotmentDate) { this.allotmentDate = allotmentDate; }
    
    public LocalDate getRefundDate() { return refundDate; }
    public void setRefundDate(LocalDate refundDate) { this.refundDate = refundDate; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getProspectusUrl() { return prospectusUrl; }
    public void setProspectusUrl(String prospectusUrl) { this.prospectusUrl = prospectusUrl; }
    
    public String getCompanyDescription() { return companyDescription; }
    public void setCompanyDescription(String companyDescription) { this.companyDescription = companyDescription; }
    
    public String getStrengths() { return strengths; }
    public void setStrengths(String strengths) { this.strengths = strengths; }
    
    public String getRisks() { return risks; }
    public void setRisks(String risks) { this.risks = risks; }
    
    public BigDecimal getSubscriptionQIB() { return subscriptionQIB; }
    public void setSubscriptionQIB(BigDecimal subscriptionQIB) { this.subscriptionQIB = subscriptionQIB; }
    
    public BigDecimal getSubscriptionNII() { return subscriptionNII; }
    public void setSubscriptionNII(BigDecimal subscriptionNII) { this.subscriptionNII = subscriptionNII; }
    
    public BigDecimal getSubscriptionRII() { return subscriptionRII; }
    public void setSubscriptionRII(BigDecimal subscriptionRII) { this.subscriptionRII = subscriptionRII; }
    
    public BigDecimal getSubscriptionTotal() { return subscriptionTotal; }
    public void setSubscriptionTotal(BigDecimal subscriptionTotal) { this.subscriptionTotal = subscriptionTotal; }
    
    public BigDecimal getListingGain() { return listingGain; }
    public void setListingGain(BigDecimal listingGain) { this.listingGain = listingGain; }
    
    public String getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(String currentStatus) { this.currentStatus = currentStatus; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}