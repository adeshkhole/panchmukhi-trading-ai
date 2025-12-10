package com.panchmukhi.trading.controller;

import com.panchmukhi.trading.service.MLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/ml")
@CrossOrigin(origins = "*")
public class MLController {

    private final MLService mlService;

    @Autowired
    private com.panchmukhi.trading.repository.UserRepository userRepository;

    @Autowired
    public MLController(MLService mlService) {
        this.mlService = mlService;
    }

    @PostMapping("/sentiment")
    public Mono<ResponseEntity<Map<String, Object>>> analyzeSentiment(@RequestBody Map<String, String> request) {
        if (!hasAccess(com.panchmukhi.trading.model.User.PlanType.BASIC)) {
            return Mono.just(ResponseEntity.status(403)
                    .body(Map.of("error", "Upgrade to BASIC plan to access Sentiment Analysis")));
        }
        return mlService.analyzeSentiment(request.get("text"), request.getOrDefault("language", "en"))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PostMapping("/predict")
    public Mono<ResponseEntity<Map<String, Object>>> predictPrice(@RequestBody Map<String, Object> request) {
        if (!hasAccess(com.panchmukhi.trading.model.User.PlanType.PRO)) {
            return Mono.just(
                    ResponseEntity.status(403).body(Map.of("error", "Upgrade to PRO plan to access Price Prediction")));
        }
        String symbol = (String) request.get("symbol");
        String timeframe = (String) request.getOrDefault("timeframe", "1d");
        int historicalDays = (int) request.getOrDefault("historical_days", 30);

        return mlService.predictPrice(symbol, timeframe, historicalDays)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PostMapping("/risk")
    public Mono<ResponseEntity<Map<String, Object>>> analyzeRisk(@RequestBody Map<String, Object> request) {
        if (!hasAccess(com.panchmukhi.trading.model.User.PlanType.PRO)) {
            return Mono.just(
                    ResponseEntity.status(403).body(Map.of("error", "Upgrade to PRO plan to access Risk Analysis")));
        }
        String symbol = (String) request.get("symbol");
        double portfolioValue = Double.parseDouble(request.get("portfolio_value").toString());
        double positionSize = Double.parseDouble(request.get("position_size").toString());

        return mlService.analyzeRisk(symbol, portfolioValue, positionSize)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/fusion")
    public Mono<ResponseEntity<Map<String, Object>>> calculateFusionScore(@RequestBody Map<String, Object> request) {
        if (!hasAccess(com.panchmukhi.trading.model.User.PlanType.PRO)) {
            return Mono.just(
                    ResponseEntity.status(403).body(Map.of("error", "Upgrade to PRO plan to access Fusion Score")));
        }
        String symbol = (String) request.get("symbol");
        Map<String, Object> marketData = (Map<String, Object>) request.get("market_data");
        Map<String, Object> technicalIndicators = (Map<String, Object>) request.get("technical_indicators");

        return mlService.calculateFusionScore(symbol, marketData, technicalIndicators)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    private boolean hasAccess(com.panchmukhi.trading.model.User.PlanType requiredPlan) {
        try {
            org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder
                    .getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return false;
            }

            String email = auth.getName();
            com.panchmukhi.trading.model.User user = userRepository.findByEmail(email).orElse(null);

            if (user == null)
                return false;

            // ADMIN has access to everything
            if (user.getRole() == com.panchmukhi.trading.model.User.Role.ADMIN)
                return true;

            com.panchmukhi.trading.model.User.PlanType userPlan = user.getPlan();

            if (requiredPlan == com.panchmukhi.trading.model.User.PlanType.FREE)
                return true;

            if (requiredPlan == com.panchmukhi.trading.model.User.PlanType.BASIC) {
                return userPlan == com.panchmukhi.trading.model.User.PlanType.BASIC ||
                        userPlan == com.panchmukhi.trading.model.User.PlanType.PRO ||
                        userPlan == com.panchmukhi.trading.model.User.PlanType.ENTERPRISE;
            }

            if (requiredPlan == com.panchmukhi.trading.model.User.PlanType.PRO) {
                return userPlan == com.panchmukhi.trading.model.User.PlanType.PRO ||
                        userPlan == com.panchmukhi.trading.model.User.PlanType.ENTERPRISE;
            }

            return userPlan == com.panchmukhi.trading.model.User.PlanType.ENTERPRISE;

        } catch (Exception e) {
            return false;
        }
    }
}
