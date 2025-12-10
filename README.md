# Panchmukhi Trading AI Platform

## 📌 Overview
Panchmukhi Trading AI is a comprehensive trading platform that combines machine learning with financial analysis to provide intelligent trading insights and automation.

## 🚀 Features
- Real-time market data analysis
- AI-powered trading signals
- Portfolio management
- Sentiment analysis of financial news
- Automated trading strategies
- User authentication and authorization

## 🏗️ Project Structure
```
panchmukhi-trading-ai/
├── panchmukhi-java-fullstack/  # Main Java Spring Boot application
│   ├── backend/                # Backend Java code
│   ├── frontend/               # Frontend assets
│   └── database/               # Database scripts
├── ml-services/                # Python ML services
│   ├── services/               # ML service implementations
│   └── utils/                  # Utility functions
├── scripts/                    # Utility scripts
└── nginx/                      # Nginx configuration
```

## 🛠️ Prerequisites
- Java 17 or higher
- Python 3.8+
- Node.js 16+
- Docker & Docker Compose
- Maven
- PostgreSQL
- MongoDB

## 🚀 Getting Started

### Clone the Repository
```bash
git clone https://github.com/adeshkhole/panchmukhi-trading-ai.git
cd panchmukhi-trading-ai
```

### Using Docker (Recommended)
1. Make sure Docker and Docker Compose are installed
2. Run the following command:
   ```bash
   docker-compose up --build
   ```
3. Access the application at `http://localhost:8080`

### Manual Setup

#### Backend Setup
1. Navigate to the backend directory:
   ```bash
   cd panchmukhi-java-fullstack/backend
   ```
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

#### Frontend Setup
1. Navigate to the frontend directory:
   ```bash
   cd panchmukhi-java-fullstack/frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm start
   ```

#### ML Services Setup
1. Navigate to the ML services directory:
   ```bash
   cd ml-services
   ```
2. Create a virtual environment:
   ```bash
   python -m venv venv
   source venv/bin/activate  # On Windows: .\venv\Scripts\activate
   ```
3. Install dependencies:
   ```bash
   pip install -r requirements.txt
   ```
4. Start the ML service:
   ```bash
   python app.py
   ```

## 🔄 Development Workflow
1. Create a new branch for your feature:
   ```bash
   git checkout -b feature/your-feature-name
   ```
2. Make your changes and commit them:
   ```bash
   git add .
   git commit -m "Add your commit message"
   ```
3. Push your changes to the remote repository:
   ```bash
   git push origin feature/your-feature-name
   ```
4. Create a pull request from your branch to `develop`

## 📝 Environment Variables
Create a `.env` file in the root directory with the following variables:
```
# Database
DB_URL=jdbc:postgresql://localhost:5432/panchmukhi_trading
DB_USERNAME=your_username
DB_PASSWORD=your_password

# JWT
JWT_SECRET=your_jwt_secret
JWT_EXPIRATION_MS=86400000

# ML Service
ML_SERVICE_URL=http://localhost:5000
```

## 🤝 Contributing
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments
- [Spring Boot](https://spring.io/projects/spring-boot)
- [React](https://reactjs.org/)
- [Docker](https://www.docker.com/)
- [Python ML Libraries](https://scikit-learn.org/)
