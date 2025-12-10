-- Comprehensive Demo Data for Panchmukhi Trading Platform

-- ====================
-- NEWS ARTICLES (22)
-- ====================

-- IT Sector News
INSERT INTO news (id, title, content, source, url, published_at, category, language, sentiment_score, created_at, updated_at) VALUES
('news-1', 'TCS wins $1.2 Billion deal from UK Retailer', 'Tata Consultancy Services has secured a major digital transformation contract worth $1.2 billion from a leading UK-based retailer. This multi-year engagement will focus on cloud migration, AI-powered analytics, and customer experience enhancement.', 'Moneycontrol', 'https://www.moneycontrol.com/news/tcs-deal', NOW() - INTERVAL ''2 hours'', ''TECHNOLOGY'', ''en'', 0.75, NOW(), NOW()),
('news-2', 'Infosys launches AI-powered financial platform', 'Infosys has unveiled Finacle 2.0, an AI-driven banking platform that promises to revolutionize retail banking operations. The platform integrates machine learning for fraud detection and personalized customer service.', 'Economic Times', 'https://economictimes.com/infosys-ai', NOW() - INTERVAL ''5 hours'', ''TECHNOLOGY'', ''en'', 0.68, NOW(), NOW()),
('news-3', 'Wipro expands European operations with new centers', 'Wipro Limited is investing €200 million to establish three new delivery centers across France and Germany, creating 5,000 jobs over the next 18 months.', 'Business Standard', 'https://business-standard.com/wipro', NOW() - INTERVAL ''1 day'', ''TECHNOLOGY'', ''en'', 0.52, NOW(), NOW()),
('news-4', 'HCL Tech partners with Google Cloud', 'HCL Technologies and Google Cloud announce strategic partnership to deliver cloud-native solutions for global enterprises.', 'Livemint', 'https://livemint.com/hcl-google', NOW() - INTERVAL ''8 hours'', ''TECHNOLOGY'', ''en'', 0.60, NOW(), NOW()),

-- Banking News
('news-5', 'RBI maintains repo rate at 6.5%', 'Reserve Bank of India keeps key lending rates unchanged, citing inflation concerns and global economic uncertainties.', 'Moneycontrol', 'https://moneycontrol.com/rbi', NOW() - INTERVAL ''3 hours'', ''BANKING'', ''en'', 0.12, NOW(), NOW()),
('news-6', 'HDFC Bank Q4 profit jumps 34%', 'HDFC Bank reports strong quarterly results with net profit rising to  ₹16,372 crore.', 'Economic Times', 'https://economictimes.com/hdfc', NOW() - INTERVAL ''6 hours'', ''BANKING'', ''en'', 0.72, NOW(), NOW()),
('news-7', 'SBI launches instant loan facility', 'State Bank of India introduces digital lending platform offering instant personal loans up to ₹50 lakh.', 'Business Today', 'https://businesstoday.in/sbi', NOW() - INTERVAL ''1 day'', ''BANKING'', ''en'', 0.55, NOW(), NOW()),

-- Auto News
('news-9', 'Tata Motors EV sales surge 200%', 'Tata Motors reports exponential growth in EV segment with 200% year-on-year increase.', 'AutocarIndia', 'https://autocarindia.com/tata', NOW() - INTERVAL ''4 hours'', ''AUTOMOBILE'', ''en'', 0.82, NOW(), NOW()),
('news-10', 'Maruti invests ₹35,000 crore in EVs', 'India''s largest carmaker announces massive investment for electric vehicle production.', 'Livemint', 'https://livemint.com/maruti', NOW() - INTERVAL ''7 hours'', ''AUTOMOBILE'', ''en'', 0.70, NOW(), NOW()),

-- Pharma News
('news-12', 'Sun Pharma gets USFDA approval', 'Sun Pharmaceutical receives FDA approval for generic oncology drug.', 'Pharmabiz', 'https://pharmabiz.com/sun', NOW() - INTERVAL ''5 hours'', ''PHARMA'', ''en'', 0.78, NOW(), NOW()),
('news-13', 'Dr Reddy launches affordable insulin', 'Dr Reddy''s introduces biosimilar insulin at 40% lower cost.', 'HealthcareIndia', 'https://healthcareindia.com/dr', NOW() - INTERVAL ''9 hours'', ''PHARMA'', ''en'', 0.85, NOW(), NOW());


-- ====================
-- SECTOR INTELLIGENCE (7)
-- ====================

INSERT INTO sector_intelligence (id, sector_name, sentiment_score, current_trend, news_count, top_story, market_cap_change, volume_change, created_at, updated_at) VALUES
('sector-1', 'NIFTY IT', 0.65, 'Strong Bullish', 125, 'TCS wins $1.2 Billion deal', 2.4, 15.3, NOW(), NOW()),
('sector-2', 'NIFTY BANK', 0.15, 'Neutral with Upside', 98, 'HDFC Bank Q4 profit jumps 34%', 0.8, 8.2, NOW(), NOW()),
('sector-3', 'NIFTY PHARMA', 0.72, 'Bullish', 67, 'Dr Reddy launches affordable insulin', 3.1, 12.5, NOW(), NOW()),
('sector-4', 'NIFTY AUTO', -0.28, 'Bearish Correction', 89, 'Tata Motors EV sales surge 200%', -1.5, 6.8, NOW(), NOW()),
('sector-5', 'NIFTY ENERGY', 0.38, 'Moderately Bullish', 54, 'Adani Green Energy 1 GW solar', 1.8, 10.1, NOW(), NOW()),
('sector-6', 'NIFTY INFRASTRUCTURE', 0.55, 'Bullish Momentum', 43, 'L&T bags ₹25,000 crore project', 2.2, 14.7, NOW(), NOW()),
('sector-7', 'NIFTY FMCG', 0.18, 'Neutral', 36, 'ITC Foods premium snacks', 0.5, 4.2, NOW(), NOW());


-- ====================
-- COMPANY PROJECTS (5)
-- ====================

INSERT INTO company_projects (id, company_name, project_title, project_type, description, investment_amount, location, expected_completion, status, sector, employment_generation, created_at, updated_at) VALUES
('project-1', 'Reliance Industries', 'Green Hydrogen Plant', 'EXPANSION', 'Massive green hydrogen facility targeting 100 GW solar capacity by 2030', 75000, 'Jamnagar, Gujarat', '2030-12-31', 'In Progress', 'ENERGY', 50000, NOW(), NOW()),
('project-2', 'Tata Motors', 'EV Manufacturing Plant', 'NEW_FACILITY', 'State-of-the-art EV plant with 250,000 annual capacity', 15000, 'Sanand, Gujarat', '2025-06-30', 'In Progress', 'AUTOMOBILE', 12000, NOW(), NOW()),
('project-3', 'Adani Group', 'Data Centers Network', 'EXPANSION', 'Pan-India data center network with 1 GW capacity', 50000, 'Multiple Locations', '2026-12-31', 'In Progress', 'TECHNOLOGY', 25000, NOW(), NOW()),
('project-4', 'Infosys', 'AI Center of Excellence', 'NEW_FACILITY', 'Global AI research campus for generative AI solutions', 2000, 'Bengaluru, Karnataka', '2024-09-30', 'On Track', 'TECHNOLOGY', 5000, NOW(), NOW()),
('project-5', 'HDFC Bank', 'Digital Banking Platform 2.0', 'DIGITAL_TRANSFORMATION', 'Next-gen digital banking with blockchain and AI', 5000, 'Mumbai, Maharashtra', '2025-03-31', 'In Progress', 'BANKING', 8000, NOW(), NOW());


-- ====================
-- SCRAPER CONFIGS (3)
-- ====================

INSERT INTO scraper_configs (id, website_name, base_url, category, target_selector, title_selector, content_selector, date_selector, image_selector, link_selector, is_active, scraping_frequency, success_count, failure_count, last_scraped, description, created_at, updated_at) VALUES
('scraper-1', 'MoneyControl', 'https://www.moneycontrol.com/news/business/', 'news', '.newslist', 'h2 a', 'p', 'span.ago', 'img', 'h2 a', true, 60, 247, 3, NOW(), 'Business news scraper', NOW(), NOW()),
('scraper-2', 'Economic Times', 'https://economictimes.indiatimes.com/markets', 'news', ' .eachStory', '.w_tle', '.w_img', 'time', 'img', 'a', true, 30, 512, 8, NOW(), 'Market news scraper', NOW(), NOW()),
('scraper-3', 'ISRO Official', 'https://www.isro.gov.in/updates.html', 'isro', '.update-list', 'h3', '.content', '.date', 'img', 'a', true, 120, 89, 1, NOW(), 'ISRO updates scraper', NOW(), NOW());
