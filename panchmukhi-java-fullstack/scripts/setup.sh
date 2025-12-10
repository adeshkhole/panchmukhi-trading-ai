#!/bin/bash

# Panchmukhi Trading Brain Pro - Setup Script
# Java Fullstack Version

set -e

echo "🧠 पंचमुखी ट्रेडिंग ब्रेन प्रो - सेटअप सुरू करत आहे..."
echo "=================================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if running as root
if [ "$EUID" -eq 0 ]; then 
    print_error "कृपया हा स्क्रिप्ट root नसलेल्या युजरसह चालवा"
    exit 1
fi

# Check system requirements
check_requirements() {
    print_status "सिस्टिम आवश्यकता तपासत आहे..."
    
    # Check RAM
    TOTAL_RAM=$(free -g | awk 'NR==2{printf "%.1f", $2}')
    if (( $(echo "$TOTAL_RAM < 8" | bc -l) )); then
        print_warning "कमीतकमी 8GB RAM शिफारस केली आहे. सध्याची RAM: ${TOTAL_RAM}GB"
    fi
    
    # Check CPU cores
    CPU_CORES=$(nproc)
    if [ $CPU_CORES -lt 4 ]; then
        print_warning "कमीतकमी 4 CPU cores शिफारस केले आहे. सध्याचे cores: $CPU_CORES"
    fi
    
    # Check disk space
    DISK_SPACE=$(df -BG . | awk 'NR==2{print $4}' | sed 's/G//')
    if [ $DISK_SPACE -lt 20 ]; then
        print_warning "कमीतकमी 20GB खाली डिस्क स्पेस आवश्यक आहे. सध्याची स्पेस: ${DISK_SPACE}GB"
    fi
    
    print_success "सिस्टिम आवश्यकता तपासणी पूर्ण"
}

# Check and install dependencies
install_dependencies() {
    print_status "आवश्यक अवलंबिता स्थापित करत आहे..."
    
    # Check for required commands
    local commands=("docker" "docker-compose" "git" "curl" "wget")
    local missing_commands=()
    
    for cmd in "${commands[@]}"; do
        if ! command -v $cmd &> /dev/null; then
            missing_commands+=($cmd)
        fi
    done
    
    if [ ${#missing_commands[@]} -ne 0 ]; then
        print_error "खालील आवश्यक कमांड्स सापडले नाहीत: ${missing_commands[*]}"
        echo ""
        echo "कृपया खालील कमांड्स वापरून अवलंबिता स्थापित करा:"
        echo ""
        echo "Ubuntu/Debian:"
        echo "  sudo apt update"
        echo "  sudo apt install -y docker.io docker-compose git curl wget"
        echo ""
        echo "CentOS/RHEL:"
        echo "  sudo yum install -y docker docker-compose git curl wget"
        echo ""
        echo "macOS:"
        echo "  brew install docker docker-compose git curl wget"
        echo ""
        exit 1
    fi
    
    # Check Docker service
    if ! docker info &> /dev/null; then
        print_error "Docker सेवा चालू नाही. कृपया Docker सुरू करा:"
        echo "  sudo systemctl start docker"
        echo "  sudo systemctl enable docker"
        exit 1
    fi
    
    # Check Docker Compose version
    DOCKER_COMPOSE_VERSION=$(docker-compose --version | grep -o '[0-9]\+\.[0-9]\+\.[0-9]\+')
    if [ -z "$DOCKER_COMPOSE_VERSION" ]; then
        print_warning "Docker Compose version तपासता आली नाही"
    fi
    
    print_success "अवलंबिता तपासणी पूर्ण"
}

# Setup environment configuration
setup_environment() {
    print_status "एनव्हायरनमेंट कॉन्फिगरेशन सेटअप करत आहे..."
    
    if [ ! -f ".env" ]; then
        print_status ".env फाईल तयार करत आहे..."
        cat > .env << EOF
# Panchmukhi Trading Brain Pro - Environment Configuration

# Database Configuration
POSTGRES_DB=panchmukhi_trading
POSTGRES_USER=panchmukhi_user
POSTGRES_PASSWORD=panchmukhi_pass

# MongoDB Configuration
MONGO_INITDB_ROOT_USERNAME=panchmukhi_admin
MONGO_INITDB_ROOT_PASSWORD=panchmukhi_admin_pass
MONGO_INITDB_DATABASE=panchmukhi_logs

# Redis Configuration
REDIS_PASSWORD=

# JWT Configuration
JWT_SECRET=panchmukhi-super-secret-key-change-in-production-$(date +%s)

# ML Services Configuration
ML_SERVICE_URL=http://ml-services:8000

# Application Configuration
APP_ENV=development
APP_DEBUG=true
APP_URL=http://localhost:8080

# Security Configuration
CORS_ORIGIN=*
RATE_LIMIT_ENABLED=true
RATE_LIMIT_REQUESTS_PER_MINUTE=100

# Monitoring Configuration
GRAFANA_ADMIN_PASSWORD=admin123
PROMETHEUS_RETENTION_TIME=30d

# Email Configuration (Optional)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=
MAIL_PASSWORD=
MAIL_ENCRYPTION=tls

# SMS Configuration (Optional)
TWILIO_ACCOUNT_SID=
TWILIO_AUTH_TOKEN=
TWILIO_PHONE_NUMBER=
EOF
        print_success ".env फाईल तयार केली"
    else
        print_warning ".env फाईल आधीच अस्तित्वात आहे"
    fi
}

# Create necessary directories
setup_directories() {
    print_status "आवश्यक डायरेक्टरीज तयार करत आहे..."
    
    local directories=(
        "backend/logs"
        "ml-services/logs"
        "ml-services/models"
        "ml-services/data"
        "nginx/logs"
        "monitoring/grafana/provisioning/dashboards"
        "monitoring/grafana/provisioning/datasources"
        "monitoring/prometheus"
    )
    
    for dir in "${directories[@]}"; do
        if [ ! -d "$dir" ]; then
            mkdir -p "$dir"
            print_success "डायरेक्टरी तयार केली: $dir"
        fi
    done
}

# Setup monitoring configuration
setup_monitoring() {
    print_status "मॉनिटरिंग कॉन्फिगरेशन सेटअप करत आहे..."
    
    # Prometheus configuration
    if [ ! -f "monitoring/prometheus.yml" ]; then
        cat > monitoring/prometheus.yml << EOF
global:
  scrape_interval: 15s
  evaluation_interval: 15s

rule_files:
  - "alert_rules.yml"

alerting:
  alertmanagers:
    - static_configs:
        - targets: []

scrape_configs:
  - job_name: 'prometheus'
    static_configs:
      - targets: ['localhost:9090']

  - job_name: 'backend'
    static_configs:
      - targets: ['backend:8083']
    metrics_path: '/actuator/prometheus'
    scrape_interval: 30s

  - job_name: 'ml-services'
    static_configs:
      - targets: ['ml-services:8000']
    metrics_path: '/metrics'
    scrape_interval: 30s

  - job_name: 'postgres'
    static_configs:
      - targets: ['postgres-exporter:9187']

  - job_name: 'redis'
    static_configs:
      - targets: ['redis-exporter:9121']
EOF
        print_success "Prometheus कॉन्फिगरेशन तयार केली"
    fi
    
    # Grafana datasource configuration
    if [ ! -f "monitoring/grafana/provisioning/datasources/prometheus.yml" ]; then
        cat > monitoring/grafana/provisioning/datasources/prometheus.yml << EOF
apiVersion: 1

datasources:
  - name: Prometheus
    type: prometheus
    access: proxy
    url: http://prometheus:9090
    isDefault: true
    editable: true
EOF
        print_success "Grafana datasource कॉन्फिगरेशन तयार केली"
    fi
}

# Pull Docker images
pull_images() {
    print_status "Docker images डाउनलोड करत आहे..."
    
    local images=(
        "postgres:15-alpine"
        "mongo:7.0"
        "redis:7-alpine"
        "nginx:alpine"
        "prom/prometheus:latest"
        "grafana/grafana:latest"
    )
    
    for image in "${images[@]}"; do
        print_status "Image डाउनलोड करत आहे: $image"
        if docker pull "$image"; then
            print_success "Image डाउनलोड केली: $image"
        else
            print_error "Image डाउनलोड अयशस्वी: $image"
            exit 1
        fi
    done
}

# Build custom images
build_images() {
    print_status "Custom Docker images बनवत आहे..."
    
    # Build backend
    print_status "Backend image बनवत आहे..."
    if docker-compose build backend; then
        print_success "Backend image बनवली"
    else
        print_error "Backend image बनवणे अयशस्वी"
        exit 1
    fi
    
    # Build ML services
    print_status "ML Services image बनवत आहे..."
    if docker-compose build ml-services; then
        print_success "ML Services image बनवली"
    else
        print_error "ML Services image बनवणे अयशस्वी"
        exit 1
    fi
}

# Main setup function
main() {
    echo ""
    echo "🚀 पंचमुखी ट्रेडिंग ब्रेन प्रो - Java Fullstack सेटअप"
    echo "========================================================"
    echo ""
    
    # Step 1: Check requirements
    check_requirements
    echo ""
    
    # Step 2: Install dependencies
    install_dependencies
    echo ""
    
    # Step 3: Setup environment
    setup_environment
    echo ""
    
    # Step 4: Setup directories
    setup_directories
    echo ""
    
    # Step 5: Setup monitoring
    setup_monitoring
    echo ""
    
    # Step 6: Pull images
    pull_images
    echo ""
    
    # Step 7: Build images
    build_images
    echo ""
    
    # Completion message
    echo ""
    echo "✅ सेटअप पूर्ण झाले!"
    echo "====================="
    echo ""
    echo "आता तुम्ही खालील कमांड वापरून अ‍ॅप्लिकेशन सुरू करू शकता:"
    echo ""
    echo "  🐳 Docker वापरून:"
    echo "     docker-compose up -d"
    echo ""
    echo "  ▶️  Quick Start:"
    echo "     ./scripts/start_all.sh"
    echo ""
    echo "📍 अ‍ॅप्लिकेशन URL:"
    echo "  🌐 Frontend: http://localhost:8080"
    echo "  🔧 Backend API: http://localhost:8083"
    echo "  🤖 ML Services: http://localhost:8000"
    echo "  📊 Monitoring: http://localhost:3000"
    echo ""
    echo "🔑 डीफॉल्ट लॉगिन:"
    echo "  Email: rajesh@email.com"
    echo "  Password: password123"
    echo ""
    echo "📖 अधिक माहितीसाठी: README.md फाईल पहा"
    echo ""
}

# Run main function
main "$@"