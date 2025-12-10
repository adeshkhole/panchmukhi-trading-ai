package com.panchmukhi.trading.controller;

import com.panchmukhi.trading.model.Sector;
import com.panchmukhi.trading.repository.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sectors")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SectorController {

    @Autowired
    private SectorRepository sectorRepository;

    @GetMapping("/public/all")
    public ResponseEntity<List<Sector>> getAllSectors() {
        List<Sector> sectors = sectorRepository.findByIsActiveTrueOrderByMarketCapDesc();
        return ResponseEntity.ok(sectors);
    }

    @GetMapping("/public/{name}")
    public ResponseEntity<?> getSectorByName(@PathVariable String name) {
        try {
            Sector sector = sectorRepository.findBySectorName(name).orElse(null);
            if (sector != null) {
                return ResponseEntity.ok(sector);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/public/category/{category}")
    public ResponseEntity<List<Sector>> getSectorsByCategory(@PathVariable String category) {
        List<Sector> sectors = sectorRepository.findByCategoryOrderByMarketCapDesc(category);
        return ResponseEntity.ok(sectors);
    }

    @GetMapping("/public/top-performers")
    public ResponseEntity<List<Sector>> getTopPerformingSectors() {
        List<Sector> sectors = sectorRepository.findTopPerformingSectors(new java.math.BigDecimal("2.0"));
        return ResponseEntity.ok(sectors);
    }

    @GetMapping("/public/risk/{riskLevel}")
    public ResponseEntity<List<Sector>> getSectorsByRisk(@PathVariable String riskLevel) {
        List<Sector> sectors = sectorRepository.findByRiskLevel(riskLevel);
        return ResponseEntity.ok(sectors);
    }

    @GetMapping("/public/growth/{outlook}")
    public ResponseEntity<List<Sector>> getSectorsByGrowth(@PathVariable String outlook) {
        List<Sector> sectors = sectorRepository.findByGrowthOutlook(outlook);
        return ResponseEntity.ok(sectors);
    }

    @GetMapping("/public/large")
    public ResponseEntity<List<Sector>> getLargeSectors() {
        List<Sector> sectors = sectorRepository.findLargeSectors(50);
        return ResponseEntity.ok(sectors);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<?> createSector(@RequestBody Sector sector) {
        try {
            Sector savedSector = sectorRepository.save(sector);
            return ResponseEntity.ok(savedSector);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<?> updateSector(@PathVariable String id, @RequestBody Sector sector) {
        try {
            Sector existingSector = sectorRepository.findById(id).orElse(null);
            if (existingSector == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Update fields
            existingSector.setMarketCap(sector.getMarketCap());
            existingSector.setPeRatio(sector.getPeRatio());
            existingSector.setDividendYield(sector.getDividendYield());
            existingSector.setPerformance1d(sector.getPerformance1d());
            existingSector.setPerformance1w(sector.getPerformance1w());
            existingSector.setPerformance1m(sector.getPerformance1m());
            existingSector.setPerformance3m(sector.getPerformance3m());
            existingSector.setPerformance1y(sector.getPerformance1y());
            existingSector.setTopStocks(sector.getTopStocks());
            existingSector.setStockCount(sector.getStockCount());
            existingSector.setRiskLevel(sector.getRiskLevel());
            existingSector.setGrowthOutlook(sector.getGrowthOutlook());
            existingSector.setKeyDrivers(sector.getKeyDrivers());
            existingSector.setChallenges(sector.getChallenges());
            existingSector.setIsActive(sector.getIsActive());
            
            Sector updatedSector = sectorRepository.save(existingSector);
            return ResponseEntity.ok(updatedSector);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/public/dashboard")
    public ResponseEntity<Map<String, Object>> getSectorDashboard() {
        Map<String, Object> dashboard = Map.of(
            "allSectors", sectorRepository.findByIsActiveTrueOrderByMarketCapDesc(),
            "topPerformers", sectorRepository.findTopPerformingSectors(new java.math.BigDecimal("2.0")),
            "largeSectors", sectorRepository.findLargeSectors(50),
            "highGrowthSectors", sectorRepository.findByGrowthOutlook("High")
        );
        return ResponseEntity.ok(dashboard);
    }
}