package com.panchmukhi.trading.repository;

import com.panchmukhi.trading.model.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface SectorRepository extends JpaRepository<Sector, String> {
    
    Optional<Sector> findBySectorName(String sectorName);
    
    List<Sector> findByCategoryOrderByMarketCapDesc(String category);
    
    List<Sector> findByIsActiveTrueOrderByMarketCapDesc();
    
    @Query("SELECT s FROM Sector s WHERE s.performance1d > :minPerformance ORDER BY s.performance1d DESC")
    List<Sector> findTopPerformingSectors(@Param("minPerformance") BigDecimal minPerformance);
    
    @Query("SELECT s FROM Sector s WHERE s.riskLevel = :riskLevel ORDER BY s.marketCap DESC")
    List<Sector> findByRiskLevel(@Param("riskLevel") String riskLevel);
    
    @Query("SELECT s FROM Sector s WHERE s.growthOutlook = :outlook ORDER BY s.performance1y DESC")
    List<Sector> findByGrowthOutlook(@Param("outlook") String outlook);
    
    @Query("SELECT s FROM Sector s WHERE s.stockCount > :minStocks ORDER BY s.stockCount DESC")
    List<Sector> findLargeSectors(@Param("minStocks") Integer minStocks);
}