package com.panchmukhi.trading.controller;

import com.panchmukhi.trading.model.MarketData;
import com.panchmukhi.trading.service.MarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/market")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MarketDataController {

    @Autowired
    private MarketDataService marketDataService;

    @GetMapping("/public/all")
    public ResponseEntity<List<MarketData>> getAllMarketData() {
        List<MarketData> marketData = marketDataService.getAllMarketData();
        return ResponseEntity.ok(marketData);
    }

    @GetMapping("/public/{symbol}")
    public ResponseEntity<?> getMarketDataBySymbol(@PathVariable String symbol, 
                                                   @RequestParam(defaultValue = "NSE") String exchange) {
        try {
            MarketData marketData = marketDataService.getMarketDataBySymbol(symbol, exchange);
            return ResponseEntity.ok(marketData);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/public/gainers")
    public ResponseEntity<List<MarketData>> getTopGainers() {
        List<MarketData> gainers = marketDataService.getTopGainers();
        return ResponseEntity.ok(gainers);
    }

    @GetMapping("/public/losers")
    public ResponseEntity<List<MarketData>> getTopLosers() {
        List<MarketData> losers = marketDataService.getTopLosers();
        return ResponseEntity.ok(losers);
    }

    @GetMapping("/public/volume")
    public ResponseEntity<List<MarketData>> getHighVolumeStocks() {
        List<MarketData> highVolume = marketDataService.getHighVolumeStocks();
        return ResponseEntity.ok(highVolume);
    }

    @GetMapping("/public/search")
    public ResponseEntity<List<MarketData>> searchStocks(@RequestParam String q) {
        List<MarketData> results = marketDataService.searchStocks(q);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/admin/update")
    public ResponseEntity<?> updateMarketData(@RequestBody MarketData marketData) {
        try {
            marketDataService.saveMarketData(marketData);
            return ResponseEntity.ok(Map.of("message", "Market data updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/public/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        Map<String, Object> dashboard = Map.of(
            "gainers", marketDataService.getTopGainers(),
            "losers", marketDataService.getTopLosers(),
            "volume", marketDataService.getHighVolumeStocks(),
            "allStocks", marketDataService.getAllMarketData()
        );
        return ResponseEntity.ok(dashboard);
    }
}