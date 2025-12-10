# पंचमुखी ट्रेडिंग ब्रेन प्रो - Java Fullstack Version 🧠

## AI-Powered Trading Platform for Indian Markets (NSE/BSE) with Multi-Language Support

### 🌟 Overview

**पंचमुखी ट्रेडिंग ब्रेन प्रो** (Panchmukhi Trading Brain Pro) is a production-ready AI-powered trading platform specifically designed for Indian markets. The name "पंचमुखी" (Panchmukhi - five-faced) represents data fusion from 5 key sources:

- **Market Data** - Real-time NSE/BSE tick data
- **News Sentiment** - Multi-language news analysis (Marathi, Hindi, English, Gujarati, Kannada)
- **Social Media** - Twitter, Reddit sentiment tracking
- **Technical Analysis** - Advanced chart patterns and indicators
- **Fundamental Analysis** - Company financials and ratios

This is the **Java Fullstack version** that converts the original Node.js/React application to a robust Java Spring Boot backend with enhanced performance and scalability.

### 🚀 Key Features

#### 🌐 5-Language Support
- **मराठी (Marathi)** - Complete platform localization
- **हिन्दी (Hindi)** - Full functionality in Devanagari script
- **English** - Complete English interface
- **ગુજરાતી (Gujarati)** - Gujarati language support
- **ಕನ್ನಡ (Kannada)** - Kannada language interface

#### 🤖 AI-Powered Predictions
- **LSTM-based Price Forecasting** - 24-hour price predictions
- **Multi-Language Sentiment Analysis** - News and social media sentiment
- **Pattern Recognition** - Advanced candlestick pattern detection
- **Risk Assessment** - Portfolio risk metrics and VaR calculations
- **Fusion Score** - Intelligent scoring combining all data sources

#### 📊 Real-Time Features
- **WebSocket Streaming** - Live price updates and notifications
- **Market Data Caching** - Redis-based caching for fast access
- **Multi-Exchange Support** - NSE, BSE, MCX integration
- **Advanced Charting** - ECharts.js powered visualizations

#### 🛡️ Enterprise Security
- **JWT Authentication** - Secure token-based authentication
- **Two-Factor Authentication** - SMS/Email 2FA support
- **Role-Based Access Control** - Admin, User, Moderator roles
- **Rate Limiting** - API rate limiting to prevent abuse
- **Audit Logging** - Complete activity tracking

#### 📱 Cross-Platform
- **Web Application** - Responsive design for all devices
- **Progressive Web App** - Offline capabilities and push notifications
- **Mobile Optimized** - Touch-friendly interface
- **Voice Alerts** - Multi-language voice notifications

### 🏗️ Technology Stack

#### Backend (Java Spring Boot)
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: PostgreSQL 15 (Primary), MongoDB 7 (Logs), Redis 7 (Cache)
- **Security**: Spring Security + JWT
- **Real-time**: WebSocket + STOMP
- **API Documentation**: OpenAPI 3.0 (Swagger)

#### Frontend (Vanilla JS)
- **Framework**: Vanilla JavaScript (No framework dependency)
- **Styling**: Tailwind CSS
- **Charts**: ECharts.js
- **Animations**: Anime.js
- **Visual Effects**: p5.js, PIXI.js
- **Typography**: Typed.js

#### ML Services (Python FastAPI)
- **Framework**: FastAPI + Uvicorn
- **ML Libraries**: TensorFlow, PyTorch, Scikit-learn
- **NLP**: NLTK, spaCy, Transformers
- **Sentiment Analysis**: VADER, TextBlob
- **Time Series**: Prophet, Statsmodels

#### Infrastructure
- **Reverse Proxy**: Nginx
- **Containerization**: Docker + Docker Compose
- **Monitoring**: Prometheus + Grafana
- **CI/CD**: GitHub Actions

### 📁 Project Structure

```
panchmukhi-java-fullstack/
├── backend/                    # Java Spring Boot Backend
│   ├── src/main/java/com/panchmukhi/trading/
│   │   ├── config/            # Configuration classes
│   │   ├── controller/        # REST API controllers
│   │   ├── model/             # Entity classes
│   │   ├── repository/        # Database repositories
│   │   ├── service/           # Business logic services
│   │   ├── security/          # JWT and security
│   │   ├── websocket/         # WebSocket handlers
│   │   └── dto/               # Data transfer objects
│   ├── pom.xml                # Maven configuration
│   └── Dockerfile             # Backend container
│
├── frontend/                   # Vanilla JavaScript Frontend
│   ├── index.html             # Main landing page
│   ├── js/main.js             # Main application logic
│   ├── resources/             # Images and assets
│   └── css/                   # Custom styles
│
├── ml-services/               # Python FastAPI ML Services
│   ├── app.py                 # FastAPI application
│   ├── services/              # ML service modules
│   ├── models/                # ML models
│   ├── utils/                 # Utility functions
│   ├── requirements.txt       # Python dependencies
│   └── Dockerfile             # ML services container
│
├── database/                  # Database configurations
│   ├── init.sql              # PostgreSQL initialization
│   └── mongo-init.js         # MongoDB initialization
│
├── nginx/                     # Nginx configuration
│   ├── nginx.conf            # Main nginx configuration
│   └── sites-available/      # Site configurations
│
├── infrastructure/           # Infrastructure as Code
│   ├── docker-compose.yml    # Docker compose configuration
│   └── monitoring/           # Prometheus & Grafana configs
│
└── scripts/                  # Deployment and utility scripts
    ├── setup.sh              # Initial setup script
    ├── deploy.sh             # Deployment script
    └── validate_setup.sh     # Setup validation
```

### 🚀 Quick Start

#### Prerequisites
- Docker and Docker Compose installed
- Git installed
- At least 8GB RAM and 4 CPU cores recommended

#### Installation

1. **Clone the repository**
```bash
git clone https://github.com/adeshkhole/panchmukhi-trading-brain-java.git
cd panchmukhi-trading-brain-java
```

2. **Environment Setup**
```bash
# Copy environment template
cp .env.example .env

# Edit .env file with your configurations
nano .env
```

3. **Start the application**
```bash
# Using Docker Compose (Recommended)
docker-compose up -d

# Or using custom script
./scripts/start_all.sh
```

4. **Access the application**
- **Frontend**: http://localhost:8080
- **Backend API**: http://localhost:8083/api
- **ML Services**: http://localhost:8000
- **Documentation**: http://localhost:8083/docs
- **Monitoring**: http://localhost:3000 (Grafana)

#### Development Setup

1. **Backend Development**
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

2. **Frontend Development**
```bash
cd frontend
# Serve using Python HTTP server
python -m http.server 8000
# Or using Node.js
npx serve .
```

3. **ML Services Development**
```bash
cd ml-services
pip install -r requirements.txt
uvicorn app:app --reload --host 0.0.0.0 --port 8000
```

### 📊 API Documentation

#### Authentication Endpoints
```http
POST /api/auth/signup     # User registration
POST /api/auth/signin     # User login
POST /api/auth/refresh    # Token refresh
GET  /api/auth/me         # Get current user
```

#### Market Data Endpoints
```http
GET /api/market/public/all          # Get all market data
GET /api/market/public/{symbol}     # Get specific stock data
GET /api/market/public/gainers      # Get top gainers
GET /api/market/public/losers       # Get top losers
GET /api/market/public/dashboard    # Get dashboard data
```

#### News & Sentiment Endpoints
```http
GET /api/news/public/all                    # Get all news
GET /api/news/language/{language}           # Get news by language
GET /api/news/public/positive               # Get positive news
GET /api/news/public/negative               # Get negative news
POST /api/news/admin/create                 # Create news (Admin)
```

#### Trading Alerts Endpoints
```http
GET  /api/alerts/my                        # Get user's alerts
GET  /api/alerts/my/active                 # Get active alerts
POST /api/alerts/create                    # Create new alert
PUT  /api/alerts/update/{id}               # Update alert
DELETE /api/alerts/delete/{id}             # Delete alert
```

#### IPO Endpoints
```http
GET /api/ipo/public/all                    # Get all IPOs
GET /api/ipo/public/open                   # Get open IPOs
GET /api/ipo/public/upcoming               # Get upcoming IPOs
GET /api/ipo/public/successful             # Get successful IPOs
```

#### ML Services Endpoints
```http
POST /ml/sentiment/analyze                 # Analyze sentiment
POST /ml/predictions/price                 # Price prediction
POST /ml/risk/analyze                      # Risk analysis
POST /ml/fusion/score                      # AI fusion score
GET  /ml/health                            # Health check
```

### 🌐 Multi-Language Support

The platform supports 5 Indian languages with complete localization:

#### Language Features
- **Complete UI Translation** - All interface elements
- **Voice Alerts** - Multi-language voice notifications
- **News Analysis** - Sentiment analysis in local languages
- **Regional Content** - Localized market news and analysis

#### Adding New Languages
1. Add translations in `frontend/js/main.js`
2. Update language selector in HTML
3. Add NLP models for new language in ML services
4. Update database schema if needed

### 🤖 AI/ML Features

#### Sentiment Analysis
- **Multi-language Support** - Marathi, Hindi, English, Gujarati, Kannada
- **Real-time Processing** - Live sentiment scoring
- **News & Social Media** - Comprehensive sentiment sources
- **Confidence Scoring** - Reliability metrics for predictions

#### Price Prediction
- **LSTM Models** - Deep learning for time series forecasting
- **Multiple Timeframes** - 1H, 4H, 1D, 1W predictions
- **Technical Indicators** - RSI, MACD, Bollinger Bands integration
- **Ensemble Methods** - Multiple models for better accuracy

#### Risk Assessment
- **Portfolio Risk** - VaR (Value at Risk) calculations
- **Position Sizing** - Optimal position size recommendations
- **Correlation Analysis** - Cross-asset correlation monitoring
- **Stress Testing** - Portfolio stress test scenarios

#### Fusion Score
- **Multi-source Integration** - Combines 5 data sources
- **Weighted Scoring** - Dynamic weight adjustments
- **Real-time Updates** - Continuous score updates
- **Explainable AI** - Transparent scoring methodology

### 📈 Performance Optimization

#### Caching Strategy
- **Redis Cache** - Market data and user sessions
- **Database Query Optimization** - Indexed queries and connection pooling
- **Static Asset Caching** - CDN-ready static files
- **API Response Caching** - Intelligent API caching

#### Scalability Features
- **Horizontal Scaling** - Docker Compose scaling
- **Load Balancing** - Nginx reverse proxy
- **Database Sharding** - Ready for sharding implementation
- **Microservices Architecture** - Independent service scaling

### 🔐 Security Features

#### Authentication & Authorization
- **JWT Tokens** - Secure stateless authentication
- **Refresh Tokens** - Automatic token renewal
- **Role-based Access** - Granular permission control
- **Session Management** - Secure session handling

#### Data Protection
- **Password Hashing** - BCrypt password encryption
- **Data Encryption** - Sensitive data encryption at rest
- **HTTPS Ready** - SSL/TLS configuration support
- **Input Validation** - Comprehensive input sanitization

#### Security Monitoring
- **Audit Logging** - Complete activity tracking
- **Rate Limiting** - API abuse prevention
- **CORS Configuration** - Cross-origin request control
- **Security Headers** - XSS and CSRF protection

### 📊 Monitoring & Observability

#### Metrics & Monitoring
- **Prometheus Metrics** - Application performance metrics
- **Grafana Dashboards** - Real-time monitoring dashboards
- **Health Checks** - Service health monitoring
- **Error Tracking** - Comprehensive error logging

#### Logging
- **Structured Logging** - JSON format logs
- **Multi-level Logging** - Debug, Info, Warn, Error levels
- **Centralized Logging** - ELK stack ready
- **Audit Trails** - Complete activity logs

### 🚀 Deployment Options

#### Docker Deployment (Recommended)
```bash
# Production deployment
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d

# Development deployment
docker-compose up -d

# Scaling services
docker-compose up -d --scale backend=3 --scale ml-services=2
```

#### Kubernetes Deployment
```bash
# Using kubectl
kubectl apply -f infrastructure/k8s/

# Using Helm charts
helm install panchmukhi infrastructure/helm/
```

#### Cloud Deployment
- **AWS ECS/Fargate** - Container orchestration
- **Google Cloud Run** - Serverless deployment
- **Azure Container Instances** - Managed containers
- **DigitalOcean App Platform** - Simple cloud deployment

### 🧪 Testing

#### Backend Testing
```bash
cd backend
mvn test                          # Run unit tests
mvn integration-test              # Run integration tests
mvn test-coverage                 # Generate coverage report
```

#### Frontend Testing
```bash
cd frontend
npm test                          # Run JavaScript tests
npm run test:e2e                  # Run end-to-end tests
```

#### ML Services Testing
```bash
cd ml-services
pytest tests/                     # Run Python tests
pytest --cov=services/            # Run with coverage
```

### 🤝 Contributing

We welcome contributions from the community! Please see our [Contributing Guide](CONTRIBUTING.md) for details.

#### Development Workflow
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

### 🙏 Acknowledgments

- **Indian Stock Exchanges** - NSE and BSE for market data
- **Open Source Community** - For amazing libraries and tools
- **Contributors** - Everyone who contributed to this project
- **Users** - Our amazing user community

### 📞 Support

- **Documentation**: Check our [Wiki](https://github.com/adeshkhole/panchmukhi-trading-brain-java/wiki)
- **Issues**: Report bugs on [GitHub Issues](https://github.com/adeshkhole/panchmukhi-trading-brain-java/issues)
- **Discussions**: Join our [GitHub Discussions](https://github.com/adeshkhole/panchmukhi-trading-brain-java/discussions)
- **Email**: support@panchmukhi-trading.com

### 🔄 Changelog

See [CHANGELOG.md](CHANGELOG.md) for version history and changes.

---

**⚠️ Disclaimer**: This platform is for educational and research purposes. Trading involves risk, and past performance does not guarantee future results. Always consult with financial advisors before making investment decisions.

**Made with ❤️ in India** 🇮🇳