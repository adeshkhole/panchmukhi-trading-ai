# Panchmukhi Trading Brain Pro - ML Services
# FastAPI application for AI/ML services

from fastapi import FastAPI, HTTPException, Depends, BackgroundTasks
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
from pydantic import BaseModel, Field
from typing import List, Optional, Dict, Any
import uvicorn
import os
from datetime import datetime, timedelta
import asyncio
from dotenv import load_dotenv

# Load environment variables
load_dotenv()

# Import ML modules
from services.sentiment_service import SentimentService
from services.prediction_service import PredictionService
from services.risk_service import RiskService
from services.fusion_service import FusionService
from services.scraper_service import ScraperService
from utils.logger import setup_logger
from utils.database import MongoDBConnection, RedisConnection

# Setup logging
logger = setup_logger("ml_services")

# Initialize FastAPI app
app = FastAPI(
    title="Panchmukhi Trading Brain Pro - ML Services",
    description="AI-powered trading predictions and analysis",
    version="2.0.0",
    docs_url="/docs",
    redoc_url="/redoc"
)

# CORS configuration
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Global service instances
sentiment_service = SentimentService()
prediction_service = PredictionService()
risk_service = RiskService()
fusion_service = FusionService()
scraper_service = ScraperService()

# Request/Response Models
class PredictionRequest(BaseModel):
    symbol: str = Field(..., description="Stock symbol")
    timeframe: str = Field(default="1d", description="Prediction timeframe")
    historical_days: int = Field(default=30, description="Days of historical data")
    
class SentimentRequest(BaseModel):
    text: str = Field(..., description="Text to analyze")
    language: str = Field(default="en", description="Language code")
    
class SentimentBatchRequest(BaseModel):
    texts: List[str] = Field(..., description="List of texts to analyze")
    language: str = Field(default="en", description="Language code")
    
class RiskRequest(BaseModel):
    symbol: str = Field(..., description="Stock symbol")
    portfolio_value: float = Field(..., description="Total portfolio value")
    position_size: float = Field(..., description="Position size")
    
class FusionRequest(BaseModel):
    symbol: str = Field(..., description="Stock symbol")
    market_data: Dict[str, Any] = Field(..., description="Current market data")
    news_sentiment: Optional[float] = Field(None, description="News sentiment score")
    social_sentiment: Optional[float] = Field(None, description="Social media sentiment")
    technical_indicators: Dict[str, Any] = Field(..., description="Technical indicators")
    
class TradingSignal(BaseModel):
    signal_id: str
    symbol: str
    signal_type: str  # BUY, SELL, HOLD
    confidence_score: float
    target_price: float
    stop_loss: float
    rationale: str
    technical_indicators: Dict[str, Any]
    timestamp: datetime
    
class HealthResponse(BaseModel):
    status: str
    timestamp: datetime
    services: Dict[str, str]
    version: str

# Health check endpoint
@app.get("/health", response_model=HealthResponse)
async def health_check():
    """Health check endpoint"""
    try:
        # Check database connections
        mongo_status = "healthy" if await MongoDBConnection.test_connection() else "unhealthy"
        redis_status = "healthy" if await RedisConnection.test_connection() else "unhealthy"
        
        # Check ML model status
        sentiment_status = "healthy" if sentiment_service.is_ready() else "unhealthy"
        prediction_status = "healthy" if prediction_service.is_ready() else "unhealthy"
        
        overall_status = "healthy" if all(
            status == "healthy" for status in [
                mongo_status, redis_status, sentiment_status, prediction_status
            ]
        ) else "unhealthy"
        
        return HealthResponse(
            status=overall_status,
            timestamp=datetime.now(),
            services={
                "mongodb": mongo_status,
                "redis": redis_status,
                "sentiment": sentiment_status,
                "prediction": prediction_status
            },
            version="2.0.0"
        )
    except Exception as e:
        logger.error(f"Health check failed: {e}")
        raise HTTPException(status_code=500, detail="Health check failed")

# Sentiment Analysis Endpoints
@app.post("/sentiment/analyze", response_model=Dict[str, Any])
async def analyze_sentiment(request: SentimentRequest):
    """Analyze sentiment of a single text"""
    try:
        result = await sentiment_service.analyze_text(
            text=request.text,
            language=request.language
        )
        return {
            "sentiment_score": result["sentiment_score"],
            "sentiment_label": result["sentiment_label"],
            "confidence": result["confidence"],
            "keywords": result.get("keywords", []),
            "language": request.language,
            "processing_time_ms": result.get("processing_time_ms", 0)
        }
    except Exception as e:
        logger.error(f"Sentiment analysis failed: {e}")
        raise HTTPException(status_code=500, detail="Sentiment analysis failed")

@app.post("/sentiment/analyze-batch", response_model=List[Dict[str, Any]])
async def analyze_sentiment_batch(request: SentimentBatchRequest):
    """Analyze sentiment of multiple texts"""
    try:
        results = await sentiment_service.analyze_batch(
            texts=request.texts,
            language=request.language
        )
        return results
    except Exception as e:
        logger.error(f"Batch sentiment analysis failed: {e}")
        raise HTTPException(status_code=500, detail="Batch sentiment analysis failed")

@app.post("/sentiment/news", response_model=Dict[str, Any])
async def analyze_news_sentiment(news_data: Dict[str, Any]):
    """Analyze sentiment of news articles"""
    try:
        result = await sentiment_service.analyze_news(news_data)
        return result
    except Exception as e:
        logger.error(f"News sentiment analysis failed: {e}")
        raise HTTPException(status_code=500, detail="News sentiment analysis failed")

@app.post("/sentiment/social", response_model=Dict[str, Any])
async def analyze_social_sentiment(social_data: Dict[str, Any]):
    """Analyze sentiment from social media data"""
    try:
        result = await sentiment_service.analyze_social_media(social_data)
        return result
    except Exception as e:
        logger.error(f"Social sentiment analysis failed: {e}")
        raise HTTPException(status_code=500, detail="Social sentiment analysis failed")

# Price Prediction Endpoints
@app.post("/predictions/price", response_model=Dict[str, Any])
async def predict_price(request: PredictionRequest):
    """Predict stock price using LSTM model"""
    try:
        result = await prediction_service.predict_price(
            symbol=request.symbol,
            timeframe=request.timeframe,
            historical_days=request.historical_days
        )
        return result
    except Exception as e:
        logger.error(f"Price prediction failed: {e}")
        raise HTTPException(status_code=500, detail="Price prediction failed")

@app.post("/predictions/trend", response_model=Dict[str, Any])
async def predict_trend(prediction_data: Dict[str, Any]):
    """Predict market trend using multiple indicators"""
    try:
        result = await prediction_service.predict_trend(prediction_data)
        return result
    except Exception as e:
        logger.error(f"Trend prediction failed: {e}")
        raise HTTPException(status_code=500, detail="Trend prediction failed")

@app.post("/predictions/volatility", response_model=Dict[str, Any])
async def predict_volatility(volatility_data: Dict[str, Any]):
    """Predict price volatility"""
    try:
        result = await prediction_service.predict_volatility(volatility_data)
        return result
    except Exception as e:
        logger.error(f"Volatility prediction failed: {e}")
        raise HTTPException(status_code=500, detail="Volatility prediction failed")

# Risk Analysis Endpoints
@app.post("/risk/analyze", response_model=Dict[str, Any])
async def analyze_risk(request: RiskRequest):
    """Analyze risk for a trading position"""
    try:
        result = await risk_service.analyze_risk(
            symbol=request.symbol,
            portfolio_value=request.portfolio_value,
            position_size=request.position_size
        )
        return result
    except Exception as e:
        logger.error(f"Risk analysis failed: {e}")
        raise HTTPException(status_code=500, detail="Risk analysis failed")

@app.post("/risk/portfolio", response_model=Dict[str, Any])
async def analyze_portfolio_risk(portfolio_data: Dict[str, Any]):
    """Analyze portfolio risk"""
    try:
        result = await risk_service.analyze_portfolio(portfolio_data)
        return result
    except Exception as e:
        logger.error(f"Portfolio risk analysis failed: {e}")
        raise HTTPException(status_code=500, detail="Portfolio risk analysis failed")

@app.post("/risk/value-at-risk", response_model=Dict[str, Any])
async def calculate_var(var_data: Dict[str, Any]):
    """Calculate Value at Risk (VaR)"""
    try:
        result = await risk_service.calculate_var(var_data)
        return result
    except Exception as e:
        logger.error(f"VaR calculation failed: {e}")
        raise HTTPException(status_code=500, detail="VaR calculation failed")

# Fusion Score Endpoints
@app.post("/fusion/score", response_model=Dict[str, Any])
async def calculate_fusion_score(request: FusionRequest):
    """Calculate AI fusion score combining multiple data sources"""
    try:
        result = await fusion_service.calculate_fusion_score(
            symbol=request.symbol,
            market_data=request.market_data,
            news_sentiment=request.news_sentiment,
            social_sentiment=request.social_sentiment,
            technical_indicators=request.technical_indicators
        )
        return result
    except Exception as e:
        logger.error(f"Fusion score calculation failed: {e}")
        raise HTTPException(status_code=500, detail="Fusion score calculation failed")

@app.post("/fusion/signals", response_model=TradingSignal)
async def generate_fusion_signals(fusion_data: Dict[str, Any]):
    """Generate trading signals based on fusion analysis"""
    try:
        result = await fusion_service.generate_signals(fusion_data)
        return TradingSignal(**result)
    except Exception as e:
        logger.error(f"Fusion signals generation failed: {e}")
        raise HTTPException(status_code=500, detail="Fusion signals generation failed")

# Batch Processing Endpoints
@app.post("/batch/sentiment", response_model=Dict[str, Any])
async def batch_sentiment_analysis(batch_data: Dict[str, Any], background_tasks: BackgroundTasks):
    """Process sentiment analysis in batch"""
    try:
        task_id = f"sentiment_batch_{datetime.now().timestamp()}"
        
        # Process in background
        background_tasks.add_task(
            sentiment_service.process_batch,
            task_id,
            batch_data
        )
        
        return {
            "task_id": task_id,
            "status": "processing",
            "message": "Batch sentiment analysis started"
        }
    except Exception as e:
        logger.error(f"Batch sentiment analysis failed: {e}")
        raise HTTPException(status_code=500, detail="Batch sentiment analysis failed")

@app.post("/batch/predictions", response_model=Dict[str, Any])
async def batch_predictions(batch_data: Dict[str, Any], background_tasks: BackgroundTasks):
    """Process price predictions in batch"""
    try:
        task_id = f"prediction_batch_{datetime.now().timestamp()}"
        
        # Process in background
        background_tasks.add_task(
            prediction_service.process_batch,
            task_id,
            batch_data
        )
        
        return {
            "task_id": task_id,
            "status": "processing",
            "message": "Batch predictions started"
        }
    except Exception as e:
        logger.error(f"Batch predictions failed: {e}")
        raise HTTPException(status_code=500, detail="Batch predictions failed")

# Model Management Endpoints
@app.get("/models/status", response_model=Dict[str, Any])
async def get_model_status():
    """Get status of all ML models"""
    try:
        return {
            "sentiment_model": sentiment_service.get_model_status(),
            "prediction_model": prediction_service.get_model_status(),
            "risk_model": risk_service.get_model_status(),
            "fusion_model": fusion_service.get_model_status(),
            "timestamp": datetime.now()
        }
    except Exception as e:
        logger.error(f"Failed to get model status: {e}")
        raise HTTPException(status_code=500, detail="Failed to get model status")

@app.post("/models/reload")
async def reload_models(model_type: str = "all"):
    """Reload ML models"""
    try:
        if model_type in ["sentiment", "all"]:
            await sentiment_service.reload_model()
        if model_type in ["prediction", "all"]:
            await prediction_service.reload_model()
        if model_type in ["risk", "all"]:
            await risk_service.reload_model()
        if model_type in ["fusion", "all"]:
            await fusion_service.reload_model()
        
        return {"message": f"Models reloaded successfully", "model_type": model_type}
    except Exception as e:
        logger.error(f"Failed to reload models: {e}")
        raise HTTPException(status_code=500, detail="Failed to reload models")

# Analytics Endpoints
@app.get("/analytics/sentiment-trends", response_model=Dict[str, Any])
async def get_sentiment_trends(symbol: str, days: int = 30):
    """Get sentiment trends for a symbol"""
    try:
        result = await sentiment_service.get_trends(symbol, days)
        return result
    except Exception as e:
        logger.error(f"Failed to get sentiment trends: {e}")
        raise HTTPException(status_code=500, detail="Failed to get sentiment trends")

@app.get("/analytics/accuracy", response_model=Dict[str, Any])
async def get_model_accuracy(model_type: str, days: int = 30):
    """Get accuracy metrics for ML models"""
    try:
        if model_type == "sentiment":
            result = await sentiment_service.get_accuracy_metrics(days)
        elif model_type == "prediction":
            result = await prediction_service.get_accuracy_metrics(days)
        elif model_type == "risk":
            result = await risk_service.get_accuracy_metrics(days)
        else:
            raise HTTPException(status_code=400, detail="Invalid model type")
        
        return result
    except Exception as e:
        logger.error(f"Failed to get accuracy metrics: {e}")
        raise HTTPException(status_code=500, detail="Failed to get accuracy metrics")

# Data Scraping Endpoints
@app.get("/scrape/all", response_model=Dict[str, Any])
async def scrape_all_data():
    """Trigger scraping for all data sources"""
    try:
        results = await scraper_service.scrape_all()
        return results
    except Exception as e:
        logger.error(f"Scraping failed: {e}")
        raise HTTPException(status_code=500, detail="Scraping failed")

@app.get("/data/isro", response_model=List[Dict[str, Any]])
async def get_isro_data():
    """Get ISRO mission data"""
    try:
        return await scraper_service.scrape_isro()
    except Exception as e:
        logger.error(f"ISRO data fetch failed: {e}")
        raise HTTPException(status_code=500, detail="ISRO data fetch failed")

@app.get("/data/options", response_model=Dict[str, Any])
async def get_option_chain():
    """Get Option Chain data"""
    try:
        return await scraper_service.scrape_option_chain()
    except Exception as e:
        logger.error(f"Option chain fetch failed: {e}")
        raise HTTPException(status_code=500, detail="Option chain fetch failed")

@app.get("/data/social", response_model=List[Dict[str, Any]])
async def get_social_data():
    """Get Social Media data"""
    try:
        return await scraper_service.scrape_social_media()
    except Exception as e:
        logger.error(f"Social data fetch failed: {e}")
        raise HTTPException(status_code=500, detail="Social data fetch failed")

# Startup and shutdown events
@app.on_event("startup")
async def startup_event():
    """Initialize services on startup"""
    try:
        logger.info("Starting ML Services...")
        
        # Initialize database connections
        await MongoDBConnection.initialize()
        await RedisConnection.initialize()
        
        # Initialize ML services
        await sentiment_service.initialize()
        await prediction_service.initialize()
        await risk_service.initialize()
        await fusion_service.initialize()
        
        logger.info("ML Services started successfully")
    except Exception as e:
        logger.error(f"Failed to start ML services: {e}")
        raise

@app.on_event("shutdown")
async def shutdown_event():
    """Cleanup on shutdown"""
    try:
        logger.info("Shutting down ML Services...")
        
        # Close database connections
        await MongoDBConnection.close()
        await RedisConnection.close()
        
        logger.info("ML Services shut down successfully")
    except Exception as e:
        logger.error(f"Error during shutdown: {e}")

# Main execution
if __name__ == "__main__":
    port = int(os.getenv("PORT", 8000))
    host = os.getenv("HOST", "0.0.0.0")
    
    uvicorn.run(
        "app:app",
        host=host,
        port=port,
        reload=True,
        log_level="info",
        workers=1
    )