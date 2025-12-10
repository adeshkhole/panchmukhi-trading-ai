package com.panchmukhi.trading.scheduler;

import com.panchmukhi.trading.model.ScraperConfig;
import com.panchmukhi.trading.repository.ScraperConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScrapingScheduler {

    @Autowired
    private ScraperConfigRepository scraperConfigRepository;

    // TODO: Add reference to scraping service once implemented

    /**
     * Daily news scraping - 6 AM IST
     */
    @Scheduled(cron = "0 0 6 * * *")
    public void dailyNewsScraping() {
        System.out.println("[SCHEDULER] Starting daily news scraping at " + LocalDateTime.now());

        List<ScraperConfig> newsConfigs = scraperConfigRepository.findByIsActiveTrueAndCategory("news");

        for (ScraperConfig config : newsConfigs) {
            try {
                // TODO: Call ML service to scrape
                System.out.println("  - Scraping: " + config.getWebsiteName());

                // Update last scraped time
                config.setLastScraped(LocalDateTime.now());
                scraperConfigRepository.save(config);

            } catch (Exception e) {
                System.err.println("  - Error scraping " + config.getWebsiteName() + ": " + e.getMessage());
                config.setFailureCount(config.getFailureCount() + 1);
                config.setLastError(e.getMessage());
                scraperConfigRepository.save(config);
            }
        }

        System.out.println("[SCHEDULER] Daily news scraping completed");
    }

    /**
     * Stock data update - Every 15 minutes during market hours
     */
    @Scheduled(cron = "0 */15 * * * *")
    public void stockDataUpdate() {
        // Only run during Indian market hours (9:15 AM - 3:30 PM IST)
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();

        boolean isMarketHours = (hour > 9 || (hour == 9 && minute >= 15))
                && (hour < 15 || (hour == 15 && minute <= 30));

        if (!isMarketHours) {
            return; // Skip if market is closed
        }

        System.out.println("[SCHEDULER] Updating stock data at " + now);

        List<ScraperConfig> stockConfigs = scraperConfigRepository.findByIsActiveTrueAndCategory("stocks");

        for (ScraperConfig config : stockConfigs) {
            try {
                // TODO: Call scraping service
                System.out.println("  - Updating stocks from: " + config.getWebsiteName());

                config.setLastScraped(LocalDateTime.now());
                config.setSuccessCount(config.getSuccessCount() + 1);
                scraperConfigRepository.save(config);

            } catch (Exception e) {
                System.err.println("  - Error: " + e.getMessage());
            }
        }
    }

    /**
     * Daily sector analysis - 12 PM IST
     */
    @Scheduled(cron = "0 0 12 * * *")
    public void dailySectorAnalysis() {
        System.out.println("[SCHEDULER] Starting daily sector analysis at " + LocalDateTime.now());

        // TODO: Implement sector analysis
        // 1. Aggregate news by sector
        // 2. Calculate sentiment scores
        // 3. Update SectorIntelligence table

        System.out.println("[SCHEDULER] Sector analysis completed");
    }

    /**
     * Scrape based on frequency - Every hour
     */
    @Scheduled(cron = "0 0 * * * *") // Every hour
    public void frequencyBasedScraping() {
        System.out.println("[SCHEDULER] Checking frequency-based scrapers at " + LocalDateTime.now());

        List<ScraperConfig> allConfigs = scraperConfigRepository.findByIsActiveTrueOrderByCreatedAtDesc();

        for (ScraperConfig config : allConfigs) {
            if (shouldScrape(config)) {
                try {
                    System.out.println("  - Frequency scrape: " + config.getWebsiteName());

                    // TODO: Call scraping service

                    config.setLastScraped(LocalDateTime.now());
                    config.setSuccessCount(config.getSuccessCount() + 1);
                    scraperConfigRepository.save(config);

                } catch (Exception e) {
                    System.err.println("  - Error: " + e.getMessage());
                    config.setFailureCount(config.getFailureCount() + 1);
                    scraperConfigRepository.save(config);
                }
            }
        }
    }

    private boolean shouldScrape(ScraperConfig config) {
        if (config.getLastScraped() == null) {
            return true; // Never scraped, do it now
        }

        LocalDateTime nextScrapeTime = config.getLastScraped().plusMinutes(config.getScrapingFrequency());
        return LocalDateTime.now().isAfter(nextScrapeTime);
    }
}
