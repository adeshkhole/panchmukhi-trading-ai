# Panchmukhi Trading Brain Pro - Deployment Guide

## 🚀 Quick Deployment

### Prerequisites
- Docker and Docker Compose installed
- Minimum 8GB RAM, 4 CPU cores
- 20GB free disk space
- Internet connection for image downloads

### One-Command Deployment
```bash
# Clone and deploy
git clone https://github.com/adeshkhole/panchmukhi-trading-brain-java.git
cd panchmukhi-trading-brain-java
./scripts/setup.sh && ./scripts/start_all.sh
```

## 📋 Detailed Deployment Steps

### Step 1: Environment Setup
```bash
# Copy environment template
cp .env.example .env

# Edit configuration
nano .env
```

### Step 2: System Validation
```bash
# Check system requirements
./scripts/validate_setup.sh

# Fix any issues reported
```

### Step 3: Deploy Application
```bash
# Start all services
docker-compose up -d

# Or use the start script
./scripts/start_all.sh
```

### Step 4: Verify Deployment
```bash
# Check service status
docker-compose ps

# Check logs
docker-compose logs -f backend

# Test endpoints
curl http://localhost:8083/actuator/health
```

## 🏭 Production Deployment

### Production Environment Setup
```bash
# Production environment file
cp .env.example .env.production

# Edit production settings
nano .env.production
```

### Production Configuration
```bash
# Use production compose file
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d

# Scale services
docker-compose up -d --scale backend=3 --scale ml-services=2
```

### SSL/TLS Configuration
```bash
# Generate SSL certificates
mkdir -p nginx/ssl
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout nginx/ssl/privkey.pem \
  -out nginx/ssl/fullchain.pem

# Update nginx configuration for HTTPS
```

## ☁️ Cloud Deployment

### AWS Deployment
```bash
# Using ECS
aws ecs create-cluster --cluster-name panchmukhi-trading

# Deploy using ECS CLI
ecs-cli compose --project-name panchmukhi service up
```

### Google Cloud Deployment
```bash
# Using Cloud Run
gcloud run deploy panchmukhi-backend \
  --image gcr.io/PROJECT_ID/backend \
  --platform managed
```

### Azure Deployment
```bash
# Using Azure Container Instances
az container create \
  --resource-group myResourceGroup \
  --name panchmukhi-backend \
  --image panchmukhi/backend:latest
```

## 🔧 Configuration Management

### Environment Variables
```bash
# Core configuration
APP_ENV=production
APP_DEBUG=false
APP_URL=https://yourdomain.com

# Database
POSTGRES_DB=panchmukhi_trading
POSTGRES_USER=panchmukhi_user
POSTGRES_PASSWORD=secure_password

# Security
JWT_SECRET=your-super-secure-jwt-secret
CORS_ORIGIN=https://yourdomain.com
```

### Database Configuration
```bash
# PostgreSQL
spring.datasource.url=jdbc:postgresql://postgres:5432/panchmukhi_trading
spring.datasource.username=panchmukhi_user
spring.datasource.password=secure_password

# Connection Pool
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
```

## 📊 Monitoring Setup

### Prometheus Configuration
```yaml
# prometheus.yml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'backend'
    static_configs:
      - targets: ['backend:8083']
    metrics_path: '/actuator/prometheus'
```

### Grafana Dashboard
```bash
# Import dashboard
curl -X POST \
  http://admin:admin123@localhost:3000/api/dashboards/db \
  -H 'Content-Type: application/json' \
  -d @monitoring/dashboard.json
```

## 🔄 CI/CD Pipeline

### GitHub Actions
```yaml
# .github/workflows/deploy.yml
name: Deploy to Production

on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      
      - name: Build and Deploy
        run: |
          docker-compose build
          docker-compose push
          
      - name: Deploy to Server
        run: |
          ssh user@server 'cd /app && docker-compose pull && docker-compose up -d'
```

## 🛡️ Security Configuration

### SSL Certificate
```bash
# Using Let's Encrypt
docker run -it --rm \
  -v nginx/ssl:/etc/letsencrypt \
  certbot/certbot certonly --standalone \
  -d yourdomain.com
```

### Firewall Configuration
```bash
# UFW (Ubuntu)
ufw allow 80/tcp
ufw allow 443/tcp
ufw allow 22/tcp
ufw enable
```

### Security Headers
```nginx
# nginx.conf
add_header X-Frame-Options "SAMEORIGIN" always;
add_header X-Content-Type-Options "nosniff" always;
add_header X-XSS-Protection "1; mode=block" always;
add_header Strict-Transport-Security "max-age=31536000" always;
```

## 📈 Scaling Configuration

### Horizontal Scaling
```bash
# Scale backend services
docker-compose up -d --scale backend=3

# Scale ML services
docker-compose up -d --scale ml-services=2

# Load balancer configuration
# Update nginx upstream configuration
```

### Database Scaling
```bash
# PostgreSQL read replicas
# Configure connection pooling
# Implement database sharding
```

### Cache Scaling
```bash
# Redis cluster setup
docker-compose -f docker-compose.redis-cluster.yml up -d
```

## 🔍 Troubleshooting

### Common Issues

1. **Port Already in Use**
```bash
# Check port usage
lsof -i :8080

# Kill process using port
kill -9 $(lsof -t -i :8080)
```

2. **Database Connection Failed**
```bash
# Check database container
docker-compose logs postgres

# Test database connection
docker-compose exec postgres pg_isready
```

3. **Memory Issues**
```bash
# Check memory usage
docker stats --no-stream

# Increase JVM heap size
export JAVA_OPTS="-Xmx2g -Xms1g"
```

4. **Performance Issues**
```bash
# Check slow queries
# Enable query logging
# Add database indexes
# Optimize connection pooling
```

### Log Analysis
```bash
# View service logs
docker-compose logs -f backend

# Search for errors
docker-compose logs | grep ERROR

# Export logs
docker-compose logs --no-color > logs.txt
```

## 🚨 Backup and Recovery

### Database Backup
```bash
# PostgreSQL backup
docker-compose exec postgres pg_dump -U panchmukhi_user panchmukhi_trading > backup.sql

# MongoDB backup
docker-compose exec mongodb mongodump --out /backup
```

### Application Backup
```bash
# Backup configuration
tar -czf config_backup.tar.gz .env docker-compose.yml nginx/

# Backup application data
docker-compose exec backend tar -czf /backup/app_data.tar.gz /app/data
```

### Recovery Process
```bash
# Restore database
docker-compose exec postgres psql -U panchmukhi_user -d panchmukhi_trading < backup.sql

# Restore configuration
tar -xzf config_backup.tar.gz
```

## 🧹 Maintenance

### Regular Updates
```bash
# Update Docker images
docker-compose pull

# Update application
docker-compose build --no-cache

# Cleanup old images
docker image prune -f
```

### Health Checks
```bash
# Automated health check script
./scripts/health_check.sh

# Database maintenance
docker-compose exec postgres vacuumdb -U panchmukhi_user panchmukhi_trading
```

## 📚 Documentation

### API Documentation
- Swagger UI: `http://yourdomain.com/docs`
- OpenAPI Spec: `http://yourdomain.com/v3/api-docs`

### System Documentation
- Architecture: `/docs/ARCHITECTURE.md`
- API Reference: `/docs/API.md`
- Deployment: `/docs/DEPLOYMENT.md`

### User Guide
- Getting Started: `/docs/USER_GUIDE.md`
- Features: `/docs/FEATURES.md`
- Troubleshooting: `/docs/TROUBLESHOOTING.md`

## 🤝 Support

### Getting Help
- **Documentation**: Check docs directory
- **Issues**: GitHub Issues
- **Community**: GitHub Discussions
- **Email**: support@panchmukhi-trading.com

### Professional Support
- **Enterprise Support**: Available
- **Custom Development**: On request
- **Training**: Available
- **Consulting**: Available

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**🎉 Your Panchmukhi Trading Brain Pro is ready for deployment!**

For any issues or questions, please refer to the troubleshooting section or contact support.