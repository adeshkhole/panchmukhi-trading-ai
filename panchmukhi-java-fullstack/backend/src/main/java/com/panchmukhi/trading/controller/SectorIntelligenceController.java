package com.panchmukhi.trading.controller;

import com.panchmukhi.trading.model.SectorIntelligence;
import com.panchmukhi.trading.repository.SectorIntelligenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sector-intelligence")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SectorIntelligenceController {

    @Autowired
    private SectorIntelligenceRepository sectorIntelligenceRepository;

    @GetMapping("/public/all")
    public ResponseEntity<List<SectorIntelligence>> getAllSectors() {
        return ResponseEntity.ok(sectorIntelligenceRepository.findAll());
    }

    @GetMapping("/public/{sectorName}")
    public ResponseEntity<?> getSectorByName(@PathVariable String sectorName) {
        return sectorIntelligenceRepository.findBySectorName(sectorName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
