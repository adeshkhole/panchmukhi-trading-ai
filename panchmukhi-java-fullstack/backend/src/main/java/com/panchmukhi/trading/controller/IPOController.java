package com.panchmukhi.trading.controller;

import com.panchmukhi.trading.model.IPO;
import com.panchmukhi.trading.repository.IPORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ipo")
@CrossOrigin(origins = "*", maxAge = 3600)
public class IPOController {

    @Autowired
    private IPORepository ipoRepository;

    @GetMapping("/public/all")
    public ResponseEntity<List<IPO>> getAllIPOs() {
        List<IPO> ipos = ipoRepository.findAll();
        return ResponseEntity.ok(ipos);
    }

    @GetMapping("/public/symbol/{symbol}")
    public ResponseEntity<?> getIPOBySymbol(@PathVariable String symbol) {
        try {
            IPO ipo = ipoRepository.findBySymbol(symbol).orElse(null);
            if (ipo != null) {
                return ResponseEntity.ok(ipo);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/public/status/{status}")
    public ResponseEntity<List<IPO>> getIPOsByStatus(@PathVariable String status) {
        List<IPO> ipos = ipoRepository.findByCurrentStatusOrderByBidStartDateDesc(status);
        return ResponseEntity.ok(ipos);
    }

    @GetMapping("/public/open")
    public ResponseEntity<List<IPO>> getOpenIPOs() {
        List<IPO> ipos = ipoRepository.findOpenForBidding(LocalDate.now());
        return ResponseEntity.ok(ipos);
    }

    @GetMapping("/public/upcoming")
    public ResponseEntity<List<IPO>> getUpcomingIPOs() {
        List<IPO> ipos = ipoRepository.findByBidStartDateAfterOrderByBidStartDateAsc(LocalDate.now());
        return ResponseEntity.ok(ipos);
    }

    @GetMapping("/public/listed")
    public ResponseEntity<List<IPO>> getRecentlyListedIPOs() {
        List<IPO> ipos = ipoRepository.findByListingDateAfterOrderByListingDateAsc(LocalDate.now().minusDays(30));
        return ResponseEntity.ok(ipos);
    }

    @GetMapping("/public/category/{category}")
    public ResponseEntity<List<IPO>> getIPOsByCategory(@PathVariable String category) {
        List<IPO> ipos = ipoRepository.findByCategoryOrderByCreatedAtDesc(category);
        return ResponseEntity.ok(ipos);
    }

    @GetMapping("/public/high-demand")
    public ResponseEntity<List<IPO>> getHighDemandIPOs() {
        List<IPO> ipos = ipoRepository.findHighDemandIPOs(new java.math.BigDecimal("10.0"));
        return ResponseEntity.ok(ipos);
    }

    @GetMapping("/public/successful")
    public ResponseEntity<List<IPO>> getSuccessfulIPOs() {
        List<IPO> ipos = ipoRepository.findSuccessfulIPOs(new java.math.BigDecimal("10.0"));
        return ResponseEntity.ok(ipos);
    }

    @GetMapping("/public/large")
    public ResponseEntity<List<IPO>> getLargeIPOs() {
        List<IPO> ipos = ipoRepository.findLargeIPOs(new java.math.BigDecimal("1000.0"));
        return ResponseEntity.ok(ipos);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<?> createIPO(@RequestBody IPO ipo) {
        try {
            IPO savedIPO = ipoRepository.save(ipo);
            return ResponseEntity.ok(savedIPO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<?> updateIPO(@PathVariable String id, @RequestBody IPO ipo) {
        try {
            IPO existingIPO = ipoRepository.findById(id).orElse(null);
            if (existingIPO == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Update fields
            existingIPO.setCurrentStatus(ipo.getCurrentStatus());
            existingIPO.setSubscriptionQIB(ipo.getSubscriptionQIB());
            existingIPO.setSubscriptionNII(ipo.getSubscriptionNII());
            existingIPO.setSubscriptionRII(ipo.getSubscriptionRII());
            existingIPO.setSubscriptionTotal(ipo.getSubscriptionTotal());
            existingIPO.setListingGain(ipo.getListingGain());
            existingIPO.setFinalPrice(ipo.getFinalPrice());
            
            IPO updatedIPO = ipoRepository.save(existingIPO);
            return ResponseEntity.ok(updatedIPO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/public/dashboard")
    public ResponseEntity<Map<String, Object>> getIPODashboard() {
        Map<String, Object> dashboard = Map.of(
            "openIPOs", ipoRepository.findOpenForBidding(LocalDate.now()),
            "upcomingIPOs", ipoRepository.findByBidStartDateAfterOrderByBidStartDateAsc(LocalDate.now()),
            "recentIPOs", ipoRepository.findByListingDateAfterOrderByListingDateAsc(LocalDate.now().minusDays(30)),
            "highDemandIPOs", ipoRepository.findHighDemandIPOs(new java.math.BigDecimal("10.0")),
            "successfulIPOs", ipoRepository.findSuccessfulIPOs(new java.math.BigDecimal("10.0"))
        );
        return ResponseEntity.ok(dashboard);
    }
}