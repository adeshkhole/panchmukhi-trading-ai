package com.panchmukhi.trading.controller;

import com.panchmukhi.trading.model.News;
import com.panchmukhi.trading.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NewsController {

    @Autowired
    private NewsRepository newsRepository;

    @GetMapping("/public/all")
    public ResponseEntity<List<News>> getAllNews() {
        List<News> news = newsRepository.findAll();
        return ResponseEntity.ok(news);
    }

    @GetMapping("/public/language/{language}")
    public ResponseEntity<Page<News>> getNewsByLanguage(@PathVariable String language,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<News> news = newsRepository.findByLanguage(language, pageable);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/public/source/{source}")
    public ResponseEntity<List<News>> getNewsBySource(@PathVariable String source) {
        List<News> news = newsRepository.findBySourceOrderByPublishedAtDesc(source);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/public/category/{category}")
    public ResponseEntity<List<News>> getNewsByCategory(@PathVariable String category) {
        List<News> news = newsRepository.findByCategoryOrderByPublishedAtDesc(category);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/public/positive")
    public ResponseEntity<List<News>> getPositiveNews() {
        List<News> news = newsRepository.findPositiveNews(new java.math.BigDecimal("0.5"));
        return ResponseEntity.ok(news);
    }

    @GetMapping("/public/negative")
    public ResponseEntity<List<News>> getNegativeNews() {
        List<News> news = newsRepository.findNegativeNews(new java.math.BigDecimal("-0.5"));
        return ResponseEntity.ok(news);
    }

    @GetMapping("/public/search")
    public ResponseEntity<List<News>> searchNews(@RequestParam String q) {
        List<News> news = newsRepository.searchByKeyword(q);
        return ResponseEntity.ok(news);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<?> createNews(@RequestBody News news) {
        try {
            News savedNews = newsRepository.save(news);
            return ResponseEntity.ok(savedNews);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/public/sources")
    public ResponseEntity<List<String>> getSources() {
        List<String> sources = newsRepository.findDistinctSources();
        return ResponseEntity.ok(sources);
    }

    @GetMapping("/public/categories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = newsRepository.findDistinctCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/public/sentiment")
    public ResponseEntity<Map<String, Object>> getSentimentAnalysis() {
        Map<String, Object> sentiment = Map.of(
            "positive", newsRepository.findPositiveNews(new java.math.BigDecimal("0.5")),
            "negative", newsRepository.findNegativeNews(new java.math.BigDecimal("-0.5")),
            "neutral", newsRepository.findAll()
        );
        return ResponseEntity.ok(sentiment);
    }
}