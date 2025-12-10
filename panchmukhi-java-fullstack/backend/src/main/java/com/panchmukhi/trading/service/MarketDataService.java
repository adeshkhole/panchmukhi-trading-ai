package com.panchmukhi.trading.service;

import com.panchmukhi.trading.model.MarketData;
import com.panchmukhi.trading.repository.MarketDataRepository;
import com.panchmukhi.trading.websocket.MarketDataWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MarketDataService {

    @Autowired
    private MarketDataRepository marketDataRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private MarketDataWebSocketHandler webSocketHandler;

    private static final String MARKET_DATA_CACHE_PREFIX = "market_data:";
    private static final long CACHE_TTL = 300; // 5 minutes

    public List<MarketData> getAllMarketData() {
        return marketDataRepository.findAll();
    }

    public MarketData getMarketDataBySymbol(String symbol, String exchange) {
        String cacheKey = MARKET_DATA_CACHE_PREFIX + symbol + ":" + exchange;
        
        // Try to get from cache first
        MarketData cachedData = (MarketData) redisTemplate.opsForValue().get(cacheKey);
        if (cachedData != null) {
            return cachedData;
        }

        // If not in cache, get from database
        MarketData marketData = marketDataRepository.findBySymbolAndExchange(symbol, exchange)
                .orElseGet(() -> createMockMarketData(symbol, exchange));

        // Cache the result
        redisTemplate.opsForValue().set(cacheKey, marketData, CACHE_TTL, TimeUnit.SECONDS);
        
        return marketData;
    }

    public List<MarketData> getTopGainers() {
        return marketDataRepository.findTopGainers(BigDecimal.valueOf(2.0));
    }

    public List<MarketData> getTopLosers() {
        return marketDataRepository.findTopLosers(BigDecimal.valueOf(-2.0));
    }

    public List<MarketData> getHighVolumeStocks() {
        return marketDataRepository.findHighVolumeStocks(1000000L);
    }

    public List<MarketData> searchStocks(String keyword) {
        return marketDataRepository.searchBySymbol(keyword);
    }

    @Scheduled(fixedRate = 30000) // Update every 30 seconds
    public void updateMarketData() {
        List<MarketData> allStocks = marketDataRepository.findAll();
        
        for (MarketData stock : allStocks) {
            // Simulate price changes
            BigDecimal newPrice = simulatePriceChange(stock.getCurrentPrice());
            stock.setCurrentPrice(newPrice);
            
            // Recalculate change amounts and percentages
            BigDecimal changeAmount = newPrice.subtract(stock.getOpenPrice());
            BigDecimal changePercent = changeAmount.divide(stock.getOpenPrice(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            
            stock.setChangeAmount(changeAmount);
            stock.setChangePercent(changePercent);
            stock.setTimestamp(LocalDateTime.now());
            
            marketDataRepository.save(stock);
            
            // Update cache
            String cacheKey = MARKET_DATA_CACHE_PREFIX + stock.getSymbol() + ":" + stock.getExchange();
            redisTemplate.opsForValue().set(cacheKey, stock, CACHE_TTL, TimeUnit.SECONDS);
            
            // Broadcast update via WebSocket
            webSocketHandler.broadcastMarketUpdate(stock);
        }
    }

    private MarketData createMockMarketData(String symbol, String exchange) {
        MarketData data = new MarketData();
        data.setSymbol(symbol);
        data.setExchange(exchange);
        
        // Generate mock data
        BigDecimal basePrice = generateBasePrice(symbol);
        data.setCurrentPrice(basePrice);
        data.setOpenPrice(basePrice);
        data.setHighPrice(basePrice.multiply(BigDecimal.valueOf(1.02)));
        data.setLowPrice(basePrice.multiply(BigDecimal.valueOf(0.98)));
        data.setClosePrice(basePrice);
        data.setVolume(1000000L + (long)(Math.random() * 9000000L));
        data.setChangeAmount(BigDecimal.ZERO);
        data.setChangePercent(BigDecimal.ZERO);
        data.setMarketCap(basePrice.multiply(BigDecimal.valueOf(1000000000L)));
        data.setPeRatio(BigDecimal.valueOf(15 + Math.random() * 20));
        data.setDividendYield(BigDecimal.valueOf(Math.random() * 5));
        data.setFiftyTwoWeekHigh(basePrice.multiply(BigDecimal.valueOf(1.5)));
        data.setFiftyTwoWeekLow(basePrice.multiply(BigDecimal.valueOf(0.5)));
        data.setTimestamp(LocalDateTime.now());
        
        return marketDataRepository.save(data);
    }

    private BigDecimal generateBasePrice(String symbol) {
        // Generate a consistent but varied base price based on symbol
        int hash = symbol.hashCode();
        double multiplier = 50 + Math.abs(hash % 1000);
        return BigDecimal.valueOf(multiplier);
    }

    private BigDecimal simulatePriceChange(BigDecimal currentPrice) {
        // Simulate small random price changes
        double changePercent = (Math.random() - 0.5) * 0.02; // ±1% change
        BigDecimal change = currentPrice.multiply(BigDecimal.valueOf(changePercent));
        return currentPrice.add(change).setScale(4, RoundingMode.HALF_UP);
    }

    public void saveMarketData(MarketData marketData) {
        marketDataRepository.save(marketData);
        
        // Update cache
        String cacheKey = MARKET_DATA_CACHE_PREFIX + marketData.getSymbol() + ":" + marketData.getExchange();
        redisTemplate.opsForValue().set(cacheKey, marketData, CACHE_TTL, TimeUnit.SECONDS);
    }
}