package com.panchmukhi.trading.controller;

import com.panchmukhi.trading.model.ScraperConfig;
import com.panchmukhi.trading.repository.ScraperConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scraper-config")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ScraperConfigController {

    @Autowired
    private ScraperConfigRepository scraperConfigRepository;

    @GetMapping("/public/all")
    public ResponseEntity<List<ScraperConfig>> getAllConfigs() {
        List<ScraperConfig> configs = scraperConfigRepository.findAll();
        return ResponseEntity.ok(configs);
    }

    @GetMapping("/public/active")
    public ResponseEntity<List<ScraperConfig>> getActiveConfigs() {
        List<ScraperConfig> configs = scraperConfigRepository.findByIsActiveTrueOrderByCreatedAtDesc();
        return ResponseEntity.ok(configs);
    }

    @GetMapping("/public/category/{category}")
    public ResponseEntity<List<ScraperConfig>> getConfigsByCategory(@PathVariable String category) {
        List<ScraperConfig> configs = scraperConfigRepository.findByCategoryOrderByCreatedAtDesc(category);
        return ResponseEntity.ok(configs);
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<?> getConfigById(@PathVariable String id) {
        return scraperConfigRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/admin/create")
    public ResponseEntity<?> createConfig(@RequestBody ScraperConfig config) {
        try {
            ScraperConfig saved = scraperConfigRepository.save(config);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Failed to create scraper config: " + e.getMessage()));
        }
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<?> updateConfig(@PathVariable String id, @RequestBody ScraperConfig config) {
        return scraperConfigRepository.findById(id)
                .map(existing -> {
                    config.setId(id);
                    config.setCreatedAt(existing.getCreatedAt());
                    ScraperConfig updated = scraperConfigRepository.save(config);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteConfig(@PathVariable String id) {
        if (scraperConfigRepository.existsById(id)) {
            scraperConfigRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Scraper config deleted successfully"));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/admin/toggle/{id}")
    public ResponseEntity<?> toggleConfig(@PathVariable String id) {
        return scraperConfigRepository.findById(id)
                .map(config -> {
                    config.setIsActive(!config.getIsActive());
                    ScraperConfig updated = scraperConfigRepository.save(config);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/public/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        long totalConfigs = scraperConfigRepository.count();
        long activeConfigs = scraperConfigRepository.countByIsActiveTrue();

        Map<String, Object> stats = Map.of(
                "total", totalConfigs,
                "active", activeConfigs,
                "inactive", totalConfigs - activeConfigs);

        return ResponseEntity.ok(stats);
    }
}
