package com.panchmukhi.trading.repository;

import com.panchmukhi.trading.model.IPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IPORepository extends JpaRepository<IPO, String> {
    
    Optional<IPO> findBySymbol(String symbol);
    
    List<IPO> findByCurrentStatusOrderByBidStartDateDesc(String currentStatus);
    
    List<IPO> findByListingDateAfterOrderByListingDateAsc(LocalDate date);
    
    List<IPO> findByBidStartDateAfterOrderByBidStartDateAsc(LocalDate date);
    
    List<IPO> findByCategoryOrderByCreatedAtDesc(String category);
    
    @Query("SELECT i FROM IPO i WHERE i.bidStartDate <= :date AND i.bidEndDate >= :date")
    List<IPO> findOpenForBidding(@Param("date") LocalDate date);
    
    @Query("SELECT i FROM IPO i WHERE i.subscriptionTotal > :minSubscription ORDER BY i.subscriptionTotal DESC")
    List<IPO> findHighDemandIPOs(@Param("minSubscription") BigDecimal minSubscription);
    
    @Query("SELECT i FROM IPO i WHERE i.listingGain > :minGain ORDER BY i.listingGain DESC")
    List<IPO> findSuccessfulIPOs(@Param("minGain") BigDecimal minGain);
    
    @Query("SELECT i FROM IPO i WHERE i.issueSize > :minSize ORDER BY i.issueSize DESC")
    List<IPO> findLargeIPOs(@Param("minSize") BigDecimal minSize);
}