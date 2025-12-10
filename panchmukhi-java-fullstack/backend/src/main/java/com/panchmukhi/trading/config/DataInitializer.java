package com.panchmukhi.trading.config;

import com.panchmukhi.trading.model.IPO;
import com.panchmukhi.trading.model.SectorIntelligence;
import com.panchmukhi.trading.model.CompanyProject;
import com.panchmukhi.trading.model.ScraperConfig;
import com.panchmukhi.trading.repository.IPORepository;
import com.panchmukhi.trading.repository.SectorIntelligenceRepository;
import com.panchmukhi.trading.repository.CompanyProjectRepository;
import com.panchmukhi.trading.repository.ScraperConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private IPORepository ipoRepository;

    @Autowired
    private com.panchmukhi.trading.repository.MarketDataRepository marketDataRepository;

    @Autowired
    private com.panchmukhi.trading.repository.NewsRepository newsRepository;

    @Autowired
    private com.panchmukhi.trading.repository.SectorRepository sectorRepository;

    @Autowired
    private SectorIntelligenceRepository sectorIntelligenceRepository;

    @Autowired
    private CompanyProjectRepository companyProjectRepository;

    @Autowired
    private ScraperConfigRepository scraperConfigRepository;

    @Override
    public void run(String... args) throws Exception {
        if (ipoRepository.count() == 0) {
            System.out.println("Initializing Demo IPO Data...");

            // 1. OPEN IPO
            IPO ipo1 = new IPO();
            ipo1.setCompanyName("Ola Electric Mobility Ltd");
            ipo1.setSymbol("OLA-ELEC");
            ipo1.setPriceBandLower(new BigDecimal("72"));
            ipo1.setPriceBandUpper(new BigDecimal("76"));
            ipo1.setIssueSize(new BigDecimal("6145.56"));
            ipo1.setLotSize(195);
            ipo1.setBidStartDate(LocalDate.now().minusDays(1));
            ipo1.setBidEndDate(LocalDate.now().plusDays(2));
            ipo1.setCurrentStatus("Open");
            ipo1.setSubscriptionTotal(new BigDecimal("2.5"));
            ipo1.setCategory("Mainboard");
            ipoRepository.save(ipo1);

            // 2. UPCOMING IPO
            IPO ipo2 = new IPO();
            ipo2.setCompanyName("Tata Technologies Ltd");
            ipo2.setSymbol("TATATECH");
            ipo2.setPriceBandLower(new BigDecimal("475"));
            ipo2.setPriceBandUpper(new BigDecimal("500"));
            ipo2.setIssueSize(new BigDecimal("3042.00"));
            ipo2.setLotSize(30);
            ipo2.setBidStartDate(LocalDate.now().plusDays(5));
            ipo2.setBidEndDate(LocalDate.now().plusDays(8));
            ipo2.setCurrentStatus("Upcoming");
            ipo2.setSubscriptionTotal(new BigDecimal("0"));
            ipo2.setCategory("Mainboard");
            ipoRepository.save(ipo2);

            // 3. CLOSED IPO
            IPO ipo3 = new IPO();
            ipo3.setCompanyName("Ideaforge Technology Ltd");
            ipo3.setSymbol("IDEAFORGE");
            ipo3.setPriceBandLower(new BigDecimal("638"));
            ipo3.setPriceBandUpper(new BigDecimal("672"));
            ipo3.setFinalPrice(new BigDecimal("672"));
            ipo3.setIssueSize(new BigDecimal("567.00"));
            ipo3.setLotSize(22);
            ipo3.setBidStartDate(LocalDate.now().minusDays(20));
            ipo3.setBidEndDate(LocalDate.now().minusDays(17));
            ipo3.setListingDate(LocalDate.now().minusDays(10));
            ipo3.setCurrentStatus("Listed");
            ipo3.setSubscriptionTotal(new BigDecimal("106.06"));
            ipo3.setListingGain(new BigDecimal("94.21"));
            ipo3.setCategory("Mainboard");
            ipoRepository.save(ipo3);

            System.out.println("IPO Demo Data Initialized!");
        }

        if (marketDataRepository.count() == 0) {
            System.out.println("Initializing Market Data...");

            // NIFTY 50
            com.panchmukhi.trading.model.MarketData nifty = new com.panchmukhi.trading.model.MarketData();
            nifty.setSymbol("NIFTY 50");
            nifty.setExchange("NSE");
            nifty.setCurrentPrice(new BigDecimal("19845.65"));
            nifty.setOpenPrice(new BigDecimal("19600.00"));
            nifty.setHighPrice(new BigDecimal("19850.00"));
            nifty.setLowPrice(new BigDecimal("19580.00"));
            nifty.setClosePrice(new BigDecimal("19845.65"));
            nifty.setVolume(1000000L);
            nifty.setChangeAmount(new BigDecimal("245.65"));
            nifty.setChangePercent(new BigDecimal("1.25"));
            marketDataRepository.save(nifty);

            // SENSEX
            com.panchmukhi.trading.model.MarketData sensex = new com.panchmukhi.trading.model.MarketData();
            sensex.setSymbol("SENSEX");
            sensex.setExchange("BSE");
            sensex.setCurrentPrice(new BigDecimal("65432.10"));
            sensex.setOpenPrice(new BigDecimal("65000.00"));
            sensex.setHighPrice(new BigDecimal("65500.00"));
            sensex.setLowPrice(new BigDecimal("64900.00"));
            sensex.setClosePrice(new BigDecimal("65432.10"));
            sensex.setVolume(500000L);
            sensex.setChangeAmount(new BigDecimal("432.10"));
            sensex.setChangePercent(new BigDecimal("0.66"));
            marketDataRepository.save(sensex);

            // RELIANCE
            com.panchmukhi.trading.model.MarketData reliance = new com.panchmukhi.trading.model.MarketData();
            reliance.setSymbol("RELIANCE");
            reliance.setExchange("NSE");
            reliance.setCurrentPrice(new BigDecimal("2345.60"));
            reliance.setOpenPrice(new BigDecimal("2300.00"));
            reliance.setHighPrice(new BigDecimal("2350.00"));
            reliance.setLowPrice(new BigDecimal("2290.00"));
            reliance.setClosePrice(new BigDecimal("2345.60"));
            reliance.setVolume(250000L);
            reliance.setChangeAmount(new BigDecimal("45.60"));
            reliance.setChangePercent(new BigDecimal("1.98"));
            marketDataRepository.save(reliance);

            System.out.println("Market Data Initialized!");
        }

        if (newsRepository.count() == 0) {
            System.out.println("Initializing News Data...");

            com.panchmukhi.trading.model.News news1 = new com.panchmukhi.trading.model.News();
            news1.setTitle("Sensex hits fresh all-time high");
            news1.setSummary("Indian markets continue bull run as Sensex crosses 66000 mark.");
            news1.setSource("Moneycontrol");
            news1.setLanguage("en");
            news1.setSentimentLabel("Positive");
            news1.setSentimentScore(new BigDecimal("0.85"));
            news1.setCategory("Market");
            newsRepository.save(news1);

            com.panchmukhi.trading.model.News news2 = new com.panchmukhi.trading.model.News();
            news2.setTitle("TCS तिमाही निकाल: नफा 15% ने वाढला");
            news2.setSummary("IT क्षेत्रातील दिग्गज TCS ने अपेक्षेपेक्षा चांगले निकाल जाहीर केले आहेत.");
            news2.setSource("Lokmat");
            news2.setLanguage("mr");
            news2.setSentimentLabel("Positive");
            news2.setSentimentScore(new BigDecimal("0.75"));
            news2.setCategory("Earnings");
            newsRepository.save(news2);

            System.out.println("News Data Initialized!");
        }

        if (sectorRepository.count() == 0) {
            System.out.println("Initializing Sector Data...");

            com.panchmukhi.trading.model.Sector sector1 = new com.panchmukhi.trading.model.Sector();
            sector1.setSectorName("NIFTY IT");
            sector1.setCategory("Technology");
            sector1.setPerformance1d(new BigDecimal("1.5"));
            sector1.setPerformance1y(new BigDecimal("12.5"));
            sector1.setRiskLevel("Medium");
            sector1.setGrowthOutlook("Positive");
            sector1.setStockCount(10);
            sectorRepository.save(sector1);

            com.panchmukhi.trading.model.Sector sector2 = new com.panchmukhi.trading.model.Sector();
            sector2.setSectorName("NIFTY BANK");
            sector2.setCategory("Finance");
            sector2.setPerformance1d(new BigDecimal("-0.5"));
            sector2.setPerformance1y(new BigDecimal("8.2"));
            sector2.setRiskLevel("High");
            sector2.setGrowthOutlook("Stable");
            sector2.setStockCount(12);
            sectorRepository.save(sector2);

            System.out.println("Sector Data Initialized!");
        }
    }
}
