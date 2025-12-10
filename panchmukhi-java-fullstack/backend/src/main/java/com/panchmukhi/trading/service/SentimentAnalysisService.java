package com.panchmukhi.trading.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SentimentAnalysisService {

    @Value("${ml.service.url}")
    private String mlServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Analyze sentiment of text
     */
    public Map<String, Object> analyzeSentiment(String text, String language) {
        try {
            String url = mlServiceUrl + "/analyze/sentiment";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("text", text);
            requestBody.put("language", language != null ? language : "en");

            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }

            return createErrorResponse("Sentiment analysis failed");

        } catch (Exception e) {
            System.err.println("Error analyzing sentiment: " + e.getMessage());
            return createErrorResponse(e.getMessage());
        }
    }

    /**
     * Analyze market mood from batch of articles
     */
    public Map<String, Object> analyzeMarketMood(List<String> articles) {
        try {
            String url = mlServiceUrl + "/analyze/market-mood";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<List<String>> request = new HttpEntity<>(articles, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }

            return createErrorResponse("Market mood analysis failed");

        } catch (Exception e) {
            System.err.println("Error analyzing market mood: " + e.getMessage());
            return createErrorResponse(e.getMessage());
        }
    }

    /**
     * Get sentiment label for score
     */
    public String getSentimentLabel(double score) {
        if (score > 0.3)
            return "Bullish 📈";
        if (score < -0.3)
            return "Bearish 📉";
        return "Neutral ⚖️";
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", message);
        return error;
    }
}
