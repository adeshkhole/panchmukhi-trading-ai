package com.panchmukhi.trading.repository;

import com.panchmukhi.trading.model.ScraperConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScraperConfigRepository extends JpaRepository<ScraperConfig, String> {

    // Find all active scraper configurations
    List<ScraperConfig> findByIsActiveTrueOrderByCreatedAtDesc();

    // Find by category (news, stocks, sectors, etc.)
    List<ScraperConfig> findByCategoryOrderByCreatedAtDesc(String category);

    // Find active configs by category
    List<ScraperConfig> findByIsActiveTrueAndCategory(String category);

    // Find by website name
    Optional<ScraperConfig> findByWebsiteName(String websiteName);

    // Find configs due for scraping (based on frequency)
    List<ScraperConfig> findByIsActiveTrueAndLastScrapedBefore(java.time.LocalDateTime threshold);

    // Count active scrapers
    long countByIsActiveTrue();
}
