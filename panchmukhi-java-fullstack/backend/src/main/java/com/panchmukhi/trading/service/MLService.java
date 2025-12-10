package com.panchmukhi.trading.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class MLService {

    private static final Logger logger = LoggerFactory.getLogger(MLService.class);
    private final WebClient webClient;

    public MLService(@Value("${ml.service.url}") String mlServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(mlServiceUrl)
                .build();
    }

    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> analyzeSentiment(String text, String language) {
        return webClient.post()
                .uri("/sentiment/analyze")
                .bodyValue(Map.of("text", text, "language", language))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (Map<String, Object>) response)
                .onErrorResume(e -> {
                    logger.error("Error calling sentiment analysis: {}", e.getMessage());
                    return Mono.empty();
                });
    }

    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> predictPrice(String symbol, String timeframe, int historicalDays) {
        return webClient.post()
                .uri("/predictions/price")
                .bodyValue(Map.of("symbol", symbol, "timeframe", timeframe, "historical_days", historicalDays))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (Map<String, Object>) response)
                .onErrorResume(e -> {
                    logger.error("Error calling price prediction: {}", e.getMessage());
                    return Mono.empty();
                });
    }

    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> analyzeRisk(String symbol, double portfolioValue, double positionSize) {
        return webClient.post()
                .uri("/risk/analyze")
                .bodyValue(Map.of("symbol", symbol, "portfolio_value", portfolioValue, "position_size", positionSize))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (Map<String, Object>) response)
                .onErrorResume(e -> {
                    logger.error("Error calling risk analysis: {}", e.getMessage());
                    return Mono.empty();
                });
    }

    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> calculateFusionScore(String symbol, Map<String, Object> marketData,
            Map<String, Object> technicalIndicators) {
        return webClient.post()
                .uri("/fusion/score")
                .bodyValue(Map.of(
                        "symbol", symbol,
                        "market_data", marketData,
                        "technical_indicators", technicalIndicators))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (Map<String, Object>) response)
                .onErrorResume(e -> {
                    logger.error("Error calling fusion score: {}", e.getMessage());
                    return Mono.empty();
                });
    }

    @SuppressWarnings("unchecked")
    public Mono<List<Map<String, Object>>> getISROData() {
        return webClient.get()
                .uri("/data/isro")
                .retrieve()
                .bodyToMono(List.class)
                .map(response -> (List<Map<String, Object>>) response)
                .onErrorResume(e -> {
                    logger.error("Error calling ISRO data: {}. Returning Mock Data.", e.getMessage());
                    // Fallback Mock Data
                    return Mono.just(List.of(
                            Map.of("name", "Gaganyaan-1", "date", "2024-12-25", "status", "Scheduled", "description",
                                    "First uncrewed mission of the Gaganyaan programme."),
                            Map.of("name", "Aditya-L1", "date", "2023-09-02", "status", "Success", "description",
                                    "India's first solar mission has reached its destination.")));
                });
    }

    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> getOptionChainData() {
        return webClient.get()
                .uri("/data/options")
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (Map<String, Object>) response)
                .onErrorResume(e -> {
                    logger.error("Error calling Option Chain data: {}. Returning Mock Data.", e.getMessage());
                    // Fallback Mock Data
                    return Mono.just(Map.of(
                            "symbol", "NIFTY",
                            "spot_price", 19845.65,
                            "expiry_date", "2024-12-28",
                            "pcr", 1.25,
                            "max_pain", 19800,
                            "strikes", List.of(
                                    Map.of("strike", 19800, "ce_oi", 150000, "pe_oi", 200000, "ce_price", 120,
                                            "pe_price", 45),
                                    Map.of("strike", 19850, "ce_oi", 100000, "pe_oi", 120000, "ce_price", 85,
                                            "pe_price", 65),
                                    Map.of("strike", 19900, "ce_oi", 250000, "pe_oi", 80000, "ce_price", 40, "pe_price",
                                            95))));
                });
    }

    @SuppressWarnings("unchecked")
    public Mono<List<Map<String, Object>>> getSocialData() {
        return webClient.get()
                .uri("/data/social")
                .retrieve()
                .bodyToMono(List.class)
                .map(response -> (List<Map<String, Object>>) response)
                .onErrorResume(e -> {
                    logger.error("Error calling Social data: {}. Returning Mock Data.", e.getMessage());
                    // Fallback Mock Data
                    return Mono.just(List.of(
                            Map.of("source", "Twitter", "user", "@StockMaster_Ind", "content",
                                    "Nifty looking strong above 19800! #BullRun", "sentiment", "Bullish", "likes",
                                    1240),
                            Map.of("source", "Reddit", "user", "u/TraderJoe", "content",
                                    "BankNifty facing resistance at 43500. Be careful.", "sentiment", "Bearish",
                                    "likes", 450)));
                });
    }

}
