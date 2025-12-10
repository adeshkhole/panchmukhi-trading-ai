package com.panchmukhi.trading.controller;

import com.panchmukhi.trading.model.CompanyProject;
import com.panchmukhi.trading.repository.CompanyProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CompanyProjectController {

    @Autowired
    private CompanyProjectRepository projectRepository;

    @GetMapping("/public/all")
    public ResponseEntity<List<CompanyProject>> getAllProjects() {
        return ResponseEntity.ok(projectRepository.findAll());
    }

    @GetMapping("/public/recent")
    public ResponseEntity<List<CompanyProject>> getRecentProjects() {
        return ResponseEntity.ok(projectRepository.findTop10ByOrderByAnnouncedDateDesc());
    }

    @GetMapping("/public/company/{companyName}")
    public ResponseEntity<List<CompanyProject>> getProjectsByCompany(@PathVariable String companyName) {
        return ResponseEntity.ok(projectRepository.findByCompanyNameContainingIgnoreCase(companyName));
    }

    @GetMapping("/public/sector/{sector}")
    public ResponseEntity<List<CompanyProject>> getProjectsBySector(@PathVariable String sector) {
        return ResponseEntity.ok(projectRepository.findBySectorOrderByAnnouncedDateDesc(sector));
    }

    @GetMapping("/public/type/{type}")
    public ResponseEntity<List<CompanyProject>> getProjectsByType(@PathVariable String type) {
        return ResponseEntity.ok(projectRepository.findByProjectTypeOrderByAnnouncedDateDesc(type));
    }
}
