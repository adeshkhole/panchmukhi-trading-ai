#!/bin/bash

# Panchmukhi Trading Brain Pro - Setup Validation Script
# Java Fullstack Version

set -e

echo "🔍 पंचमुखी ट्रेडिंग ब्रेन प्रो - सेटअप वैधता तपासणी"
echo "======================================================"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Counters
TOTAL_CHECKS=0
PASSED_CHECKS=0
FAILED_CHECKS=0

# Function to print colored output
print_status() {
    echo -e "${BLUE}[CHECK]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[PASS]${NC} $1"
    ((PASSED_CHECKS++))
}

print_warning() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

print_error() {
    echo -e "${RED}[FAIL]${NC} $1"
    ((FAILED_CHECKS++))
}

# Function to run a check
run_check() {
    local check_name="$1"
    local check_command="$2"
    local success_message="$3"
    local error_message="$4"
    
    ((TOTAL_CHECKS++))
    print_status "$check_name"
    
    if eval "$check_command" > /dev/null 2>&1; then
        print_success "$success_message"
        return 0
    else
        print_error "$error_message"
        return 1
    fi
}

# Check system requirements
check_system_requirements() {
    echo ""
    echo "🖥️  सिस्टिम आवश्यकता तपासणी"
    echo "=============================="
    
    # Check RAM
    TOTAL_RAM=$(free -m | awk 'NR==2{printf "%.0f", $2}')
    if [ $TOTAL_RAM -ge 8192 ]; then
        print_success "RAM: ${TOTAL_RAM}MB (शिफारस केलेली 8GB+)"
    elif [ $TOTAL_RAM -ge 4096 ]; then
        print_warning "RAM: ${TOTAL_RAM}MB (कमी पण चालू शकते)"
    else
        print_error "RAM: ${TOTAL_RAM}MB (खूप कमी - किमान 4GB आवश्यक)"
    fi
    
    # Check CPU cores
    CPU_CORES=$(nproc)
    if [ $CPU_CORES -ge 4 ]; then
        print_success "CPU Cores: $CPU_CORES (शिफारस केलेली 4+)"
    elif [ $CPU_CORES -ge 2 ]; then
        print_warning "CPU Cores: $CPU_CORES (कमी पण चालू शकते)"
    else
        print_error "CPU Cores: $CPU_CORES (खूप कमी - किमान 2 आवश्यक)"
    fi
    
    # Check disk space
    DISK_SPACE=$(df -m . | awk 'NR==2{print $4}')
    if [ $DISK_SPACE -ge 20480 ]; then
        print_success "Disk Space: ${DISK_SPACE}MB (20GB+ उपलब्ध)"
    elif [ $DISK_SPACE -ge 10240 ]; then
        print_warning "Disk Space: ${DISK_SPACE}MB (10GB उपलब्ध - कमी पण चालू शकते)"
    else
        print_error "Disk Space: ${DISK_SPACE}MB (खूप कमी - किमान 10GB आवश्यक)"
    fi
}

# Check dependencies
check_dependencies() {
    echo ""
    echo "🔧 अवलंबिता तपासणी"
    echo "====================="
    
    # Check Docker
    run_check "Docker स्थापित" \
        "command -v docker" \
        "Docker स्थापित आहे" \
        "Docker स्थापित नाही"
    
    # Check Docker Compose
    run_check "Docker Compose स्थापित" \
        "command -v docker-compose" \
        "Docker Compose स्थापित आहे" \
        "Docker Compose स्थापित नाही"
    
    # Check Git
    run_check "Git स्थापित" \
        "command -v git" \
        "Git स्थापित आहे" \
        "Git स्थापित नाही"
    
    # Check Curl
    run_check "Curl स्थापित" \
        "command -v curl" \
        "Curl स्थापित आहे" \
        "Curl स्थापित नाही"
    
    # Check Docker service
    run_check "Docker सेवा चालू आहे" \
        "docker info" \
        "Docker सेवा चालू आहे" \
        "Docker सेवा चालू नाही"
}

# Check file structure
check_file_structure() {
    echo ""
    echo "📁 फाईल स्ट्रक्चर तपासणी"
    echo "==========================="
    
    local required_files=(
        "docker-compose.yml"
        "backend/pom.xml"
        "frontend/index.html"
        "ml-services/requirements.txt"
        "database/init.sql"
        "nginx/nginx.conf"
    )
    
    for file in "${required_files[@]}"; do
        run_check "फाईल अस्तित्व: $file" \
            "test -f $file" \
            "फाईल अस्तित्वात आहे: $file" \
            "फाईल सापडली नाही: $file"
    done
    
    local required_dirs=(
        "backend/src"
        "frontend/js"
        "ml-services/services"
        "database"
        "nginx"
        "scripts"
    )
    
    for dir in "${required_dirs[@]}"; do
        run_check "डायरेक्टरी अस्तित्व: $dir" \
            "test -d $dir" \
            "डायरेक्टरी अस्तित्वात आहे: $dir" \
            "डायरेक्टरी सापडली नाही: $dir"
    done
}

# Check environment configuration
check_environment() {
    echo ""
    echo "⚙️  एनव्हायरनमेंट कॉन्फिगरेशन तपासणी"
    echo "======================================"
    
    # Check .env file
    run_check ".env फाईल अस्तित्व" \
        "test -f .env" \
        ".env फाईल अस्तित्वात आहे" \
        ".env फाईल सापडली नाही"
    
    if [ -f ".env" ]; then
        # Check required variables in .env
        local required_vars=(
            "POSTGRES_DB"
            "POSTGRES_USER"
            "POSTGRES_PASSWORD"
            "JWT_SECRET"
        )
        
        for var in "${required_vars[@]}"; do
            run_check "एनव्हायरनमेंट व्हेरिएबल: $var" \
                "grep -q ^$var= .env" \
                "$var व्हेरिएबल सेट केली आहे" \
                "$var व्हेरिएबल सेट केली नाही"
        done
    fi
}

# Check Docker images
check_docker_images() {
    echo ""
    echo "🐳 Docker Images तपासणी"
    echo "========================="
    
    local required_images=(
        "postgres:15-alpine"
        "mongo:7.0"
        "redis:7-alpine"
        "nginx:alpine"
    )
    
    for image in "${required_images[@]}"; do
        run_check "Docker image: $image" \
            "docker image inspect $image" \
            "Image उपलब्ध आहे: $image" \
            "Image उपलब्ध नाही: $image"
    done
}

# Check network connectivity
check_network() {
    echo ""
    echo "🌐 नेटवर्क कनेक्टिव्हिटी तपासणी"
    echo "=================================="
    
    # Check internet connectivity
    run_check "इंटरनेट कनेक्टिव्हिटी" \
        "curl -s --connect-timeout 5 https://google.com" \
        "इंटरनेट कनेक्टिव्हिटी उपलब्ध आहे" \
        "इंटरनेट कनेक्टिव्हिटी उपलब्ध नाही"
    
    # Check Docker registry access
    run_check "Docker Registry कनेक्टिव्हिटी" \
        "curl -s --connect-timeout 5 https://registry-1.docker.io" \
        "Docker Registry कनेक्टिव्हिटी उपलब्ध आहे" \
        "Docker Registry कनेक्टिव्हिटी उपलब्ध नाही"
}

# Check ports availability
check_ports() {
    echo ""
    echo "🔌 पोर्ट्स उपलब्धता तपासणी"
    echo "============================"
    
    local required_ports=(
        "8080:Frontend"
        "8083:Backend API"
        "8000:ML Services"
        "3000:Grafana"
        "9090:Prometheus"
        "5432:PostgreSQL"
        "27017:MongoDB"
        "6379:Redis"
    )
    
    for port_info in "${required_ports[@]}"; do
        local port=$(echo $port_info | cut -d: -f1)
        local service=$(echo $port_info | cut -d: -f2)
        
        if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null 2>&1; then
            print_error "पोर्ट वापरात आहे: $port ($service)"
        else
            print_success "पोर्ट उपलब्ध आहे: $port ($service)"
        fi
    done
}

# Check permissions
check_permissions() {
    echo ""
    echo "🔒 परवानगी तपासणी"
    echo "===================="
    
    # Check Docker permissions
    if docker ps &> /dev/null; then
        print_success "Docker परवानगी योग्य आहे"
    else
        print_error "Docker परवानगी अयोग्य आहे. कृपया युजरला Docker group मध्ये जोडा:"
        echo "  sudo usermod -aG docker $USER"
        echo "  newgrp docker"
    fi
    
    # Check script permissions
    local scripts=("scripts/setup.sh" "scripts/start_all.sh" "scripts/validate_setup.sh")
    
    for script in "${scripts[@]}"; do
        if [ -f "$script" ]; then
            if [ -x "$script" ]; then
                print_success "स्क्रिप्ट परवानगी: $script"
            else
                print_warning "स्क्रिप्ट परवानगी नाही: $script"
            fi
        fi
    done
}

# Generate summary report
generate_report() {
    echo ""
    echo "📊 तपासणी अहवाल"
    echo "=================="
    echo ""
    echo "एकूण तपासण्या: $TOTAL_CHECKS"
    echo "यशस्वी: $PASSED_CHECKS"
    echo "अयशस्वी: $FAILED_CHECKS"
    echo ""
    
    if [ $FAILED_CHECKS -eq 0 ]; then
        print_success "सर्व तपासण्या यशस्वी! सेटअप योग्य आहे."
        echo ""
        echo "✅ तुम्ही आता अ‍ॅप्लिकेशन सुरू करू शकता:"
        echo "   ./scripts/start_all.sh"
        echo ""
        exit 0
    else
        print_error "काही तपासण्या अयशस्वी झाल्या आहेत."
        echo ""
        echo "❌ कृपया खालील समस्या दुरुस्त करा:"
        echo ""
        
        if [ $FAILED_CHECKS -gt 0 ]; then
            echo "🔧 सूचना:"
            echo "  1. अवलंबिता स्थापित करा: ./scripts/setup.sh"
            echo "  2. Docker सेवा सुरू करा: sudo systemctl start docker"
            echo "  3. पोर्ट्स मोकळे करा किंवा बदला"
            echo "  4. .env फाईल तपासा"
            echo ""
        fi
        
        exit 1
    fi
}

# Main validation function
main() {
    echo ""
    echo "🔍 पंचमुखी ट्रेडिंग ब्रेन प्रो - सेटअप वैधता तपासणी"
    echo "========================================================"
    echo ""
    
    # Step 1: System requirements
    check_system_requirements
    
    # Step 2: Dependencies
    check_dependencies
    
    # Step 3: File structure
    check_file_structure
    
    # Step 4: Environment configuration
    check_environment
    
    # Step 5: Docker images
    check_docker_images
    
    # Step 6: Network connectivity
    check_network
    
    # Step 7: Ports availability
    check_ports
    
    # Step 8: Permissions
    check_permissions
    
    # Generate final report
    generate_report
}

# Handle script arguments
case "${1:-}" in
    --help|-h)
        echo "पंचमुखी ट्रेडिंग ब्रेन प्रो - सेटअप वैधता तपासणी स्क्रिप्ट"
        echo ""
        echo "वापर: $0 [OPTIONS]"
        echo ""
        echo "पर्याय:"
        echo "  --help, -h     मदत दाखवा"
        echo "  --quick        फक्त मुख्य तपासण्या चालवा"
        echo "  --detailed     सर्व तपासण्या चालवा"
        echo ""
        exit 0
        ;;
    --quick)
        # Quick validation - only essential checks
        echo "⚡ द्रुत तपासणी सुरू करत आहे..."
        check_dependencies
        check_file_structure
        check_environment
        generate_report
        exit 0
        ;;
esac

# Run main function
main "$@"