package com.panchmukhi.trading.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@Service
public class ScrapingService {

    @Value("${ml.service.url}")
    private String mlServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Scrape website using configuration
     */
    public Map<String, Object> scrapeWithConfig(Map<String, Object> config) {
        try {
            String url = mlServiceUrl + "/scrape/with-config";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(config, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }

            return createErrorResponse("Failed to scrape: " + response.getStatusCode());

        } catch (Exception e) {
            System.err.println("Error calling ML scraping service: " + e.getMessage());
            return createErrorResponse(e.getMessage());
        }
    }

    /**
     * Extract full article from URL
     */
    public Map<String, Object> extractFullArticle(String articleUrl) {
        try {
            String url = mlServiceUrl + "/scrape/article/" + articleUrl;

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }

            return createErrorResponse("Failed to extract article");

        } catch (Exception e) {
            System.err.println("Error extracting article: " + e.getMessage());
            return createErrorResponse(e.getMessage());
        }
    }

    /**
     * Get pre-configured scrapers
     */
    public Map<String, Object> getPreconfiguredScrapers() {
        try {
            String url = mlServiceUrl + "/scrape/preconfigured";
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            return response.getBody();
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", message);
        return error;
    }
}
