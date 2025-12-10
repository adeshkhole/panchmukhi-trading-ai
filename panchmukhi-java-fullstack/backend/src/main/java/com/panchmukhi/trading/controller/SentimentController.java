package com.panchmukhi.trading.controller;

import com.panchmukhi.trading.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sentiment")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SentimentController {

    @Autowired
    private SentimentAnalysisService sentimentService;

    @PostMapping("/public/analyze")
    public ResponseEntity<Map<String, Object>> analyzeSentiment(@RequestBody Map<String, String> request) {
        String text = request.get("text");
        String language = request.getOrDefault("language", "en");

        Map<String, Object> result = sentimentService.analyzeSentiment(text, language);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/public/market-mood")
    public ResponseEntity<Map<String, Object>> analyzeMarketMood(@RequestBody List<String> articles) {
        Map<String, Object> result = sentimentService.analyzeMarketMood(articles);
        return ResponseEntity.ok(result);
    }
}
