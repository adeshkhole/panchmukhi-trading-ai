-- Panchmukhi Trading Brain Pro - Database Initialization
-- PostgreSQL Database Schema

-- Create database (run this separately as superuser)
-- CREATE DATABASE panchmukhi_trading;
-- CREATE USER panchmukhi_user WITH PASSWORD 'panchmukhi_pass';
-- GRANT ALL PRIVILEGES ON DATABASE panchmukhi_trading TO panchmukhi_user;

-- Connect to database
\c panchmukhi_trading;

-- Create extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm"; -- For full-text search

-- Create schema
CREATE SCHEMA IF NOT EXISTS trading;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    plan VARCHAR(20) DEFAULT 'FREE',
    status VARCHAR(20) DEFAULT 'ACTIVE',
    role VARCHAR(20) DEFAULT 'USER',
    language VARCHAR(10) DEFAULT 'en',
    voice_alerts BOOLEAN DEFAULT true,
    theme VARCHAR(20) DEFAULT 'dark',
    two_factor_enabled BOOLEAN DEFAULT false,
    two_factor_secret VARCHAR(255),
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Market data table
CREATE TABLE IF NOT EXISTS market_data (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    symbol VARCHAR(20) NOT NULL,
    exchange VARCHAR(10) NOT NULL,
    current_price DECIMAL(15, 4) NOT NULL,
    open_price DECIMAL(15, 4) NOT NULL,
    high_price DECIMAL(15, 4) NOT NULL,
    low_price DECIMAL(15, 4) NOT NULL,
    close_price DECIMAL(15, 4) NOT NULL,
    volume BIGINT NOT NULL,
    change_amount DECIMAL(15, 4) NOT NULL,
    change_percent DECIMAL(8, 4) NOT NULL,
    market_cap DECIMAL(20, 2),
    pe_ratio DECIMAL(8, 2),
    dividend_yield DECIMAL(8, 4),
    fifty_two_week_high DECIMAL(15, 4),
    fifty_two_week_low DECIMAL(15, 4),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(symbol, exchange)
);

-- News table
CREATE TABLE IF NOT EXISTS news (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(500) NOT NULL,
    content TEXT,
    summary VARCHAR(500),
    source VARCHAR(255),
    url VARCHAR(500),
    author VARCHAR(255),
    language VARCHAR(20),
    sentiment_score DECIMAL(3, 2),
    sentiment_label VARCHAR(20),
    relevance_score DECIMAL(3, 2),
    category VARCHAR(50),
    tags VARCHAR(50),
    image_url VARCHAR(500),
    published_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Alerts table
CREATE TABLE IF NOT EXISTS alerts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    symbol VARCHAR(20) NOT NULL,
    target_price DECIMAL(15, 4) NOT NULL,
    current_price DECIMAL(15, 4),
    alert_type VARCHAR(50) NOT NULL,
    condition_type VARCHAR(50),
    message VARCHAR(500),
    is_active BOOLEAN DEFAULT true,
    is_triggered BOOLEAN DEFAULT false,
    triggered_at TIMESTAMP,
    notification_sent BOOLEAN DEFAULT false,
    expires_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- IPO table
CREATE TABLE IF NOT EXISTS ipos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    company_name VARCHAR(100) NOT NULL,
    symbol VARCHAR(20) NOT NULL UNIQUE,
    exchange VARCHAR(10),
    issue_size DECIMAL(15, 2) NOT NULL,
    price_band_lower DECIMAL(15, 4) NOT NULL,
    price_band_upper DECIMAL(15, 4) NOT NULL,
    final_price DECIMAL(15, 4),
    lot_size INTEGER NOT NULL,
    issue_type VARCHAR(20),
    listing_date DATE,
    bid_start_date DATE,
    bid_end_date DATE,
    allotment_date DATE,
    refund_date DATE,
    category VARCHAR(50),
    prospectus_url VARCHAR(500),
    company_description TEXT,
    strengths TEXT,
    risks TEXT,
    subscription_qib DECIMAL(8, 2),
    subscription_nii DECIMAL(8, 2),
    subscription_rii DECIMAL(8, 2),
    subscription_total DECIMAL(8, 2),
    listing_gain DECIMAL(8, 2),
    current_status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sectors table
CREATE TABLE IF NOT EXISTS sectors (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    sector_name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    category VARCHAR(50) NOT NULL,
    market_cap DECIMAL(20, 2),
    pe_ratio DECIMAL(8, 2),
    dividend_yield DECIMAL(8, 4),
    performance_1d DECIMAL(8, 4),
    performance_1w DECIMAL(8, 4),
    performance_1m DECIMAL(8, 4),
    performance_3m DECIMAL(8, 4),
    performance_1y DECIMAL(8, 4),
    top_stocks VARCHAR(500),
    stock_count INTEGER,
    risk_level VARCHAR(20),
    growth_outlook VARCHAR(20),
    key_drivers TEXT,
    challenges TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User sessions table for tracking
CREATE TABLE IF NOT EXISTS user_sessions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    session_token VARCHAR(500) NOT NULL UNIQUE,
    ip_address INET,
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL
);

-- User activity logs
CREATE TABLE IF NOT EXISTS user_activity (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    activity_type VARCHAR(50) NOT NULL,
    description TEXT,
    ip_address INET,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Market data history for time-series analysis
CREATE TABLE IF NOT EXISTS market_data_history (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    symbol VARCHAR(20) NOT NULL,
    exchange VARCHAR(10) NOT NULL,
    price DECIMAL(15, 4) NOT NULL,
    volume BIGINT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- News sentiment history
CREATE TABLE IF NOT EXISTS news_sentiment_history (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    news_id UUID NOT NULL REFERENCES news(id) ON DELETE CASCADE,
    sentiment_score DECIMAL(3, 2) NOT NULL,
    sentiment_label VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Alert history
CREATE TABLE IF NOT EXISTS alert_history (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    alert_id UUID NOT NULL REFERENCES alerts(id) ON DELETE CASCADE,
    triggered_price DECIMAL(15, 4),
    triggered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notification_sent BOOLEAN DEFAULT false
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_phone ON users(phone);
CREATE INDEX IF NOT EXISTS idx_users_status ON users(status);
CREATE INDEX IF NOT EXISTS idx_users_plan ON users(plan);

CREATE INDEX IF NOT EXISTS idx_market_data_symbol_exchange ON market_data(symbol, exchange);
CREATE INDEX IF NOT EXISTS idx_market_data_timestamp ON market_data(timestamp);

CREATE INDEX IF NOT EXISTS idx_news_language ON news(language);
CREATE INDEX IF NOT EXISTS idx_news_source ON news(source);
CREATE INDEX IF NOT EXISTS idx_news_category ON news(category);
CREATE INDEX IF NOT EXISTS idx_news_published_at ON news(published_at);
CREATE INDEX IF NOT EXISTS idx_news_sentiment ON news(sentiment_score);

CREATE INDEX IF NOT EXISTS idx_alerts_user_id ON alerts(user_id);
CREATE INDEX IF NOT EXISTS idx_alerts_symbol ON alerts(symbol);
CREATE INDEX IF NOT EXISTS idx_alerts_is_active ON alerts(is_active);
CREATE INDEX IF NOT EXISTS idx_alerts_created_at ON alerts(created_at);

CREATE INDEX IF NOT EXISTS idx_ipos_symbol ON ipos(symbol);
CREATE INDEX IF NOT EXISTS idx_ipos_current_status ON ipos(current_status);
CREATE INDEX IF NOT EXISTS idx_ipos_listing_date ON ipos(listing_date);
CREATE INDEX IF NOT EXISTS idx_ipos_bid_start_date ON ipos(bid_start_date);

CREATE INDEX IF NOT EXISTS idx_sectors_sector_name ON sectors(sector_name);
CREATE INDEX IF NOT EXISTS idx_sectors_category ON sectors(category);
CREATE INDEX IF NOT EXISTS idx_sectors_is_active ON sectors(is_active);

CREATE INDEX IF NOT EXISTS idx_user_sessions_user_id ON user_sessions(user_id);
CREATE INDEX IF NOT EXISTS idx_user_sessions_session_token ON user_sessions(session_token);
CREATE INDEX IF NOT EXISTS idx_user_sessions_expires_at ON user_sessions(expires_at);

CREATE INDEX IF NOT EXISTS idx_user_activity_user_id ON user_activity(user_id);
CREATE INDEX IF NOT EXISTS idx_user_activity_created_at ON user_activity(created_at);

CREATE INDEX IF NOT EXISTS idx_market_data_history_symbol ON market_data_history(symbol, exchange);
CREATE INDEX IF NOT EXISTS idx_market_data_history_timestamp ON market_data_history(timestamp);

-- Create full-text search indexes
CREATE INDEX IF NOT EXISTS idx_news_title_content ON news USING gin(to_tsvector('english', title || ' ' || COALESCE(content, '')));

-- Create functions for updated_at timestamps
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for updated_at
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_news_updated_at BEFORE UPDATE ON news 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_alerts_updated_at BEFORE UPDATE ON alerts 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_ipos_updated_at BEFORE UPDATE ON ipos 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_sectors_updated_at BEFORE UPDATE ON sectors 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Insert sample data
INSERT INTO users (name, email, phone, password, plan, language) VALUES
('राजेश पाटील', 'rajesh@email.com', '9876543210', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5.1JkW', 'PRO', 'mr'),
('प्रिया शर्मा', 'priya@email.com', '9123456789', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5.1JkW', 'BASIC', 'hi'),
('અર્જુન પટેલ', 'arjun@email.com', '9823456789', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5.1JkW', 'FREE', 'gu'),
('ವಿಕಾಸ್ ಕುಮಾರ್', 'vikas@email.com', '9987654321', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM5.1JkW', 'ENTERPRISE', 'kn')
ON CONFLICT (email) DO NOTHING;

-- Insert sample market data
INSERT INTO market_data (symbol, exchange, current_price, open_price, high_price, low_price, close_price, volume, change_amount, change_percent, market_cap, pe_ratio) VALUES
('RELIANCE', 'NSE', 2456.80, 2411.60, 2467.90, 2405.20, 2456.80, 12500000, 45.20, 1.88, 15600000000000, 24.5),
('TCS', 'NSE', 3421.50, 3389.40, 3434.20, 3376.80, 3421.50, 8900000, 32.10, 0.95, 12500000000000, 28.3),
('HDFC', 'NSE', 1654.30, 1625.40, 1667.80, 1618.90, 1654.30, 6700000, 28.90, 1.78, 9800000000000, 22.1),
('INFY', 'NSE', 1456.20, 1479.60, 1487.30, 1449.80, 1456.20, 8900000, -23.40, -1.58, 6200000000000, 25.7),
('ICICI', 'NSE', 987.60, 1002.90, 1008.40, 982.30, 987.60, 12300000, -15.30, -1.53, 6900000000000, 18.9)
ON CONFLICT (symbol, exchange) DO NOTHING;

-- Insert sample news
INSERT INTO news (title, content, source, language, sentiment_score, sentiment_label, category) VALUES
('Sensex, Nifty hit fresh record highs as IT stocks rally', 'Indian equity markets continued their upward momentum...', 'Economic Times', 'en', 0.75, 'Positive', 'markets'),
('RBI keeps repo rate unchanged at 6.5% for fifth consecutive time', 'The Reserve Bank of India monetary policy committee...', 'Business Standard', 'en', 0.60, 'Positive', 'economy'),
('सेंसेक्स, निफ्टीने नवा उच्चांक गाठला', 'भारतीय शेअर बाजारात सातत्याने वाढ होत आहे...', 'लोकमत', 'mr', 0.80, 'Positive', 'markets'),
('टेक स्टॉक्समध्ये तेजी, मार्केट उंचावले', 'माहिती तंत्रज्ञान कंपन्यांच्या शेअर्समध्ये मोठी वाढ...', 'सakाळ', 'mr', 0.70, 'Positive', 'technology')
ON CONFLICT DO NOTHING;

-- Insert sample IPOs
INSERT INTO ipos (company_name, symbol, issue_size, price_band_lower, price_band_upper, lot_size, category, current_status, listing_date, bid_start_date, bid_end_date) VALUES
('Tech Innovators Ltd', 'TECHINNO', 500.00, 145.00, 150.00, 100, 'Technology', 'Open', '2025-12-15', '2025-12-01', '2025-12-05'),
('Green Energy Solutions', 'GREENENG', 750.00, 234.00, 238.00, 60, 'Energy', 'Upcoming', '2025-12-20', '2025-12-10', '2025-12-14'),
('Healthcare Innovations', 'HEALTHIN', 300.00, 89.00, 92.00, 150, 'Healthcare', 'Listed', '2025-11-20', '2025-11-01', '2025-11-05')
ON CONFLICT (symbol) DO NOTHING;

-- Insert sample sectors
INSERT INTO sectors (sector_name, description, category, market_cap, pe_ratio, performance_1d, performance_1w, performance_1m, stock_count, risk_level, growth_outlook) VALUES
('Technology', 'IT services, software companies, and tech startups', 'Services', 45000000000000.00, 28.5, 1.2, 3.4, 8.7, 250, 'Medium', 'High'),
('Banking & Finance', 'Banks, NBFCs, insurance companies', 'Financial', 35000000000000.00, 18.2, 0.8, 2.1, 5.3, 150, 'Low', 'Medium'),
('Healthcare', 'Pharmaceuticals, hospitals, medical devices', 'Services', 18000000000000.00, 32.1, 1.5, 2.8, 6.9, 120, 'Medium', 'High'),
('Energy', 'Oil & gas, renewable energy, power generation', 'Industrial', 22000000000000.00, 15.8, -0.5, 1.2, 3.4, 80, 'High', 'Medium'),
('Automobile', 'Car manufacturers, auto components', 'Industrial', 15000000000000.00, 24.3, 0.9, 2.5, 4.8, 90, 'Medium', 'Medium')
ON CONFLICT (sector_name) DO NOTHING;

-- Grant permissions
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO panchmukhi_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO panchmukhi_user;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO panchmukhi_user;

-- Create indexes for better query performance
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_market_data_performance ON market_data(change_percent);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_news_sentiment_recent ON news(sentiment_score, published_at) WHERE published_at > NOW() - INTERVAL '7 days';
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_alerts_active_recent ON alerts(user_id, symbol) WHERE is_active = true AND expires_at > NOW();