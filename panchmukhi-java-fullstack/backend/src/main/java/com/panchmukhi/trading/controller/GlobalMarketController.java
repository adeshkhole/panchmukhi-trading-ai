package com.panchmukhi.trading.controller;

import com.panchmukhi.trading.service.GlobalMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/markets")
@CrossOrigin(origins = "*", maxAge = 3600)
public class GlobalMarketController {

    @Autowired
    private GlobalMarketService globalMarketService;

    @GetMapping("/public/all")
    public ResponseEntity<List<GlobalMarketService.GlobalMarket>> getAllMarkets() {
        return ResponseEntity.ok(globalMarketService.getAllMarkets());
    }

    @GetMapping("/public/{marketCode}")
    public ResponseEntity<?> getMarketByCode(@PathVariable String marketCode) {
        GlobalMarketService.GlobalMarket market = globalMarketService.getMarketByCode(marketCode);
        if (market != null) {
            return ResponseEntity.ok(market);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/public/next-opening")
    public ResponseEntity<Map<String, Object>> getNextOpening() {
        return ResponseEntity.ok(globalMarketService.getNextOpening());
    }
}
