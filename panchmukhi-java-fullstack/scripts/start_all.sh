#!/bin/bash

# Panchmukhi Trading Brain Pro - Start All Services Script
# Java Fullstack Version

set -e

echo "🚀 पंचमुखी ट्रेडिंग ब्रेन प्रो - सर्व सेवा सुरू करत आहे..."
echo "======================================================="

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

# Function to check if service is healthy
check_service_health() {
    local service=$1
    local url=$2
    local max_attempts=30
    local attempt=0
    
    print_status "$service सेवेची तपासणी करत आहे..."
    
    while [ $attempt -lt $max_attempts ]; do
        if curl -f -s "$url" > /dev/null 2>&1; then
            print_success "$service सेवा सुरू झाली"
            return 0
        fi
        
        attempt=$((attempt + 1))
        echo -n "."
        sleep 2
    done
    
    print_error "$service सेवा सुरू होण्यात अयशस्वी"
    return 1
}

# Function to show progress bar
show_progress() {
    local duration=$1
    local message=$2
    
    echo -n "$message"
    for ((i=0; i<duration; i++)); do
        echo -n "."
        sleep 1
    done
    echo ""
}

# Check if Docker is running
check_docker() {
    print_status "Docker सेवा तपासत आहे..."
    
    if ! docker info &> /dev/null; then
        print_error "Docker सेवा चालू नाही. कृपया Docker सुरू करा:"
        echo "  sudo systemctl start docker"
        echo "  sudo systemctl enable docker"
        exit 1
    fi
    
    print_success "Docker सेवा चालू आहे"
}

# Setup environment if needed
setup_environment() {
    if [ ! -f ".env" ]; then
        print_warning ".env फाईल सापडली नाही. सेटअप स्क्रिप्ट चालवत आहे..."
        ./scripts/setup.sh
    fi
}

# Start infrastructure services
start_infrastructure() {
    print_status "इन्फ्रास्ट्रक्चर सेवा सुरू करत आहे..."
    
    # Start database services first
    docker-compose up -d postgres mongodb redis
    
    # Wait for databases to be ready
    print_status "डेटाबेस सेवा सुरू होण्याची वाट पाहत आहे..."
    show_progress 30 "डेटाबेस प्रारंभ होत आहे"
    
    # Check database health
    check_service_health "PostgreSQL" "http://localhost:5432" || true
    check_service_health "MongoDB" "http://localhost:27017" || true
    check_service_health "Redis" "http://localhost:6379" || true
    
    print_success "इन्फ्रास्ट्रक्चर सेवा सुरू झाली"
}

# Start application services
start_applications() {
    print_status "अ‍ॅप्लिकेशन सेवा सुरू करत आहे..."
    
    # Start backend and ML services
    docker-compose up -d backend ml-services
    
    # Wait for applications to start
    print_status "अ‍ॅप्लिकेशन सेवा सुरू होण्याची वाट पाहत आहे..."
    show_progress 60 "अ‍ॅप्लिकेशन प्रारंभ होत आहे"
    
    # Check application health
    check_service_health "Backend API" "http://localhost:8083/actuator/health"
    check_service_health "ML Services" "http://localhost:8000/health"
    
    print_success "अ‍ॅप्लिकेशन सेवा सुरू झाली"
}

# Start proxy and frontend
start_proxy() {
    print_status "प्रॉक्सी आणि फ्रंटएंड सेवा सुरू करत आहे..."
    
    # Start nginx and frontend
    docker-compose up -d nginx frontend
    
    # Wait for proxy to start
    print_status "प्रॉक्सी सेवा सुरू होण्याची वाट पाहत आहे..."
    show_progress 15 "प्रॉक्सी प्रारंभ होत आहे"
    
    # Check proxy health
    check_service_health "Nginx Proxy" "http://localhost/health"
    check_service_health "Frontend" "http://localhost:8080"
    
    print_success "प्रॉक्सी आणि फ्रंटएंड सेवा सुरू झाली"
}

# Start monitoring services
start_monitoring() {
    print_status "मॉनिटरिंग सेवा सुरू करत आहे..."
    
    # Start Prometheus and Grafana
    docker-compose up -d prometheus grafana
    
    # Wait for monitoring to start
    print_status "मॉनिटरिंग सेवा सुरू होण्याची वाट पाहत आहे..."
    show_progress 20 "मॉनिटरिंग प्रारंभ होत आहे"
    
    # Check monitoring health
    check_service_health "Prometheus" "http://localhost:9090"
    check_service_health "Grafana" "http://localhost:3000/api/health"
    
    print_success "मॉनिटरिंग सेवा सुरू झाली"
}

# Show service status
show_status() {
    print_status "सेवा स्थिती तपासत आहे..."
    echo ""
    
    # Get running containers
    RUNNING_CONTAINERS=$(docker-compose ps --services --filter "status=running" | wc -l)
    TOTAL_CONTAINERS=$(docker-compose ps --services | wc -l)
    
    echo "🐳 Docker Compose Status:"
    echo "  सुरू असलेल्या सेवा: $RUNNING_CONTAINERS/$TOTAL_CONTAINainers"
    echo ""
    
    # Show container details
    docker-compose ps
    echo ""
    
    # Show resource usage
    print_status "संसाधन वापर:"
    docker stats --no-stream --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.MemPerc}}\t{{.NetIO}}\t{{.BlockIO}}"
}

# Display access URLs
show_access_urls() {
    echo ""
    echo "🌐 अ‍ॅप्लिकेशन प्रविष्ट URL:"
    echo "================================"
    echo ""
    echo "  🏠 मुख्य अ‍ॅप्लिकेशन:    http://localhost:8080"
    echo "  🔧 API डॉक्युमेंटेशन:  http://localhost:8083/docs"
    echo "  🤖 ML सेवा:           http://localhost:8000/docs"
    echo "  📊 मॉनिटरिंग:         http://localhost:3000"
    echo "  📈 मेट्रिक्स:         http://localhost:9090"
    echo ""
    echo "🔑 डीफॉल्ट लॉगिन:"
    echo "  Email: rajesh@email.com"
    echo "  Password: password123"
    echo ""
}

# Main function
main() {
    echo ""
    echo "🚀 पंचमुखी ट्रेडिंग ब्रेन प्रो - सर्व सेवा सुरू करणे"
    echo "========================================================"
    echo ""
    
    # Step 1: Check Docker
    check_docker
    echo ""
    
    # Step 2: Setup environment
    setup_environment
    echo ""
    
    # Step 3: Start infrastructure
    start_infrastructure
    echo ""
    
    # Step 4: Start applications
    start_applications
    echo ""
    
    # Step 5: Start proxy and frontend
    start_proxy
    echo ""
    
    # Step 6: Start monitoring (optional)
    read -p "मॉनिटरिंग सेवा सुरू करायची का? (y/n): " -n 1 -r
    echo ""
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        start_monitoring
        echo ""
    fi
    
    # Step 7: Show status
    show_status
    echo ""
    
    # Step 8: Show access URLs
    show_access_urls
    
    # Completion message
    echo ""
    echo "✅ सर्व सेवा यशस्वीरित्या सुरू झाल्या!"
    echo "======================================="
    echo ""
    echo "🔧 उपयोगी कमांड्स:"
    echo "  📊 लॉग पहा:        docker-compose logs -f [service_name]"
    echo "  🛑 थांबवा:          docker-compose down"
    echo "  🔄 रीस्टार्ट:       docker-compose restart [service_name]"
    echo "  📈 स्केल:           docker-compose up -d --scale backend=3"
    echo ""
    echo "💡 टिप्स:"
    echo "  - पहिल्या प्रारंभासाठी 2-3 मिनिटे वाट पाहा"
    echo "  - सर्व सेवा सुरू होईपर्यंत वेबसाईट रिफ्रेश करा"
    echo "  - कोणत्याही समस्येसाठी logs तपासा"
    echo ""
    echo "🙏 धन्यवाद! पंचमुखी ट्रेडिंग ब्रेन प्रो वापरण्यासाठी!"
    echo ""
}

# Handle script arguments
case "${1:-}" in
    --help|-h)
        echo "पंचमुखी ट्रेडिंग ब्रेन प्रो - सर्व सेवा सुरू करण्याची स्क्रिप्ट"
        echo ""
        echo "वापर: $0 [OPTIONS]"
        echo ""
        echo "पर्याय:"
        echo "  --help, -h     मदत दाखवा"
        echo "  --status       सेवा स्थिती तपासा"
        echo "  --logs         सर्व लॉग दाखवा"
        echo "  --stop         सर्व सेवा थांबवा"
        echo ""
        exit 0
        ;;
    --status)
        show_status
        exit 0
        ;;
    --logs)
        docker-compose logs -f
        exit 0
        ;;
    --stop)
        docker-compose down
        print_success "सर्व सेवा थांबवल्या गेल्या"
        exit 0
        ;;
esac

# Run main function
main "$@"