package com.panchmukhi.trading.controller;

import com.panchmukhi.trading.model.Alert;
import com.panchmukhi.trading.model.User;
import com.panchmukhi.trading.repository.AlertRepository;
import com.panchmukhi.trading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AlertController {

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/my")
    public ResponseEntity<?> getMyAlerts(Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            
            List<Alert> alerts = alertRepository.findByUserOrderByCreatedAtDesc(user);
            return ResponseEntity.ok(alerts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/my/active")
    public ResponseEntity<?> getMyActiveAlerts(Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            
            List<Alert> alerts = alertRepository.findByUserAndIsActiveTrue(user);
            return ResponseEntity.ok(alerts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAlert(Authentication authentication, @RequestBody Alert alert) {
        try {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            
            alert.setUser(user);
            alert.setCreatedAt(LocalDateTime.now());
            alert.setUpdatedAt(LocalDateTime.now());
            
            Alert savedAlert = alertRepository.save(alert);
            return ResponseEntity.ok(savedAlert);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAlert(Authentication authentication, 
                                         @PathVariable String id, 
                                         @RequestBody Alert alert) {
        try {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            
            Alert existingAlert = alertRepository.findById(id).orElse(null);
            if (existingAlert == null || !existingAlert.getUser().getId().equals(user.getId())) {
                return ResponseEntity.notFound().build();
            }
            
            existingAlert.setTargetPrice(alert.getTargetPrice());
            existingAlert.setAlertType(alert.getAlertType());
            existingAlert.setConditionType(alert.getConditionType());
            existingAlert.setMessage(alert.getMessage());
            existingAlert.setExpiresAt(alert.getExpiresAt());
            existingAlert.setUpdatedAt(LocalDateTime.now());
            
            Alert updatedAlert = alertRepository.save(existingAlert);
            return ResponseEntity.ok(updatedAlert);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAlert(Authentication authentication, @PathVariable String id) {
        try {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            
            Alert alert = alertRepository.findById(id).orElse(null);
            if (alert == null || !alert.getUser().getId().equals(user.getId())) {
                return ResponseEntity.notFound().build();
            }
            
            alertRepository.delete(alert);
            return ResponseEntity.ok(Map.of("message", "Alert deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/toggle/{id}")
    public ResponseEntity<?> toggleAlert(Authentication authentication, @PathVariable String id) {
        try {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            
            Alert alert = alertRepository.findById(id).orElse(null);
            if (alert == null || !alert.getUser().getId().equals(user.getId())) {
                return ResponseEntity.notFound().build();
            }
            
            alert.setIsActive(!alert.getIsActive());
            alert.setUpdatedAt(LocalDateTime.now());
            
            Alert updatedAlert = alertRepository.save(alert);
            return ResponseEntity.ok(updatedAlert);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}