# Panchmukhi Trading Brain Pro - ML Services Dockerfile
# Multi-stage build for Python FastAPI application

# Build stage
FROM python:3.11-slim AS builder

# Install system dependencies
RUN apt-get update && apt-get install -y \
    gcc \
    g++ \
    gfortran \
    libopenblas-dev \
    liblapack-dev \
    libpq-dev \
    libhdf5-dev \
    curl \
    wget \
    git \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy requirements and install Python dependencies
COPY requirements.txt .
RUN pip install --no-cache-dir --upgrade pip && \
    pip install --no-cache-dir -r requirements.txt

# Production stage
FROM python:3.11-slim

# Install runtime dependencies
RUN apt-get update && apt-get install -y \
    libopenblas-dev \
    liblapack-dev \
    libpq-dev \
    libhdf5-dev \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Create non-root user
RUN groupadd -r panchmukhi && useradd -r -g panchmukhi panchmukhi

# Set working directory
WORKDIR /app

# Copy Python packages from builder stage
COPY --from=builder /usr/local/lib/python3.11/site-packages /usr/local/lib/python3.11/site-packages
COPY --from=builder /usr/local/bin /usr/local/bin

# Copy application code
COPY --chown=panchmukhi:panchmukhi . .

# Create necessary directories
RUN mkdir -p /app/logs /app/models /app/data /app/temp && \
    chown -R panchmukhi:panchmukhi /app

# Download NLTK data
RUN python -c "import nltk; nltk.download('punkt'); nltk.download('vader_lexicon'); nltk.download('stopwords')"

# Download spaCy models
RUN python -c "import spacy; spacy.cli.download('en_core_web_sm')"

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8000/health || exit 1

# Switch to non-root user
USER panchmukhi

# Expose port
EXPOSE 8000

# Environment variables
ENV PYTHONPATH=/app \
    PYTHONUNBUFFERED=1 \
    PYTHONDONTWRITEBYTECODE=1 \
    PIP_NO_CACHE_DIR=1 \
    PIP_DISABLE_PIP_VERSION_CHECK=1

# Entry point
CMD ["uvicorn", "app:app", "--host", "0.0.0.0", "--port", "8000", "--workers", "4", "--log-level", "info"]

# Labels for better image management
LABEL maintainer="Panchmukhi Trading Brain Pro Team"
LABEL version="2.0.0"
LABEL description="AI/ML services for trading platform"
LABEL org.opencontainers.image.title="Panchmukhi ML Services"
LABEL org.opencontainers.image.description="Python FastAPI ML services for AI-powered trading platform"
LABEL org.opencontainers.image.version="2.0.0"