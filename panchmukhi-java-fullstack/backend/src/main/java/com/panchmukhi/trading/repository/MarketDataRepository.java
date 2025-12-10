package com.panchmukhi.trading.repository;

import com.panchmukhi.trading.model.MarketData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MarketDataRepository extends JpaRepository<MarketData, String> {
    
    Optional<MarketData> findBySymbolAndExchange(String symbol, String exchange);
    
    List<MarketData> findByExchangeOrderByCurrentPriceDesc(String exchange);
    
    List<MarketData> findBySymbolIn(List<String> symbols);
    
    @Query("SELECT m FROM MarketData m WHERE m.timestamp > :since ORDER BY m.timestamp DESC")
    List<MarketData> findRecentData(@Param("since") LocalDateTime since);
    
    @Query("SELECT m FROM MarketData m WHERE m.changePercent > :minChange ORDER BY m.changePercent DESC")
    List<MarketData> findTopGainers(@Param("minChange") BigDecimal minChange);
    
    @Query("SELECT m FROM MarketData m WHERE m.changePercent < :maxChange ORDER BY m.changePercent ASC")
    List<MarketData> findTopLosers(@Param("maxChange") BigDecimal maxChange);
    
    @Query("SELECT m FROM MarketData m WHERE m.volume > :minVolume ORDER BY m.volume DESC")
    List<MarketData> findHighVolumeStocks(@Param("minVolume") Long minVolume);
    
    @Query(value = "SELECT * FROM market_data WHERE symbol LIKE %:keyword% OR LOWER(symbol) LIKE LOWER(:keyword%)", nativeQuery = true)
    List<MarketData> searchBySymbol(@Param("keyword") String keyword);
    
    @Query("SELECT COUNT(m) FROM MarketData m WHERE m.exchange = :exchange")
    long countByExchange(@Param("exchange") String exchange);
}