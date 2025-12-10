import logging
from typing import Dict, Any, List, Optional
import numpy as np
import pandas as pd
import yfinance as yf
from sklearn.linear_model import LinearRegression
from datetime import datetime, timedelta

logger = logging.getLogger(__name__)

class PredictionService:
    """Service for making price predictions."""
    
    def __init__(self):
        logger.info("Initializing Prediction Service")
        self.model = LinearRegression()
        
    async def initialize(self):
        pass
        
    def is_ready(self) -> bool:
        return True

    async def predict_price(self, symbol: str, timeframe: str = "1d", historical_days: int = 30) -> Dict[str, Any]:
        """Predict future prices for a given symbol."""
        try:
            # Fetch data
            ticker = f"{symbol}.NS" if not symbol.endswith(".NS") else symbol
            stock = yf.Ticker(ticker)
            hist = stock.history(period=f"{historical_days}d")
            
            if hist.empty:
                raise ValueError(f"No data found for symbol {symbol}")
            
            # Prepare data for regression
            hist['Date_Ordinal'] = pd.to_datetime(hist.index).map(pd.Timestamp.toordinal)
            X = hist['Date_Ordinal'].values.reshape(-1, 1)
            y = hist['Close'].values
            
            # Train model
            self.model.fit(X, y)
            
            # Predict next 7 days
            last_date = pd.to_datetime(hist.index[-1])
            future_dates = [last_date + timedelta(days=i+1) for i in range(7)]
            future_ordinals = np.array([d.toordinal() for d in future_dates]).reshape(-1, 1)
            predictions = self.model.predict(future_ordinals)
            
            return {
                "symbol": symbol,
                "current_price": float(hist['Close'].iloc[-1]),
                "predictions": predictions.tolist(),
                "dates": [d.isoformat() for d in future_dates],
                "confidence": float(self.model.score(X, y)),
                "trend": "UP" if predictions[-1] > predictions[0] else "DOWN",
                "timestamp": datetime.now().isoformat()
            }
        except Exception as e:
            logger.error(f"Prediction failed for {symbol}: {e}")
            raise

    async def predict_trend(self, prediction_data: Dict[str, Any]) -> Dict[str, Any]:
        """Predict market trend using multiple indicators."""
        # Simple trend logic based on moving averages if data provided
        prices = prediction_data.get("prices", [])
        if not prices:
            return {"trend": "NEUTRAL", "confidence": 0.0}
            
        sma_20 = np.mean(prices[-20:]) if len(prices) >= 20 else np.mean(prices)
        current = prices[-1]
        
        return {
            "trend": "BULLISH" if current > sma_20 else "BEARISH",
            "strength": abs(current - sma_20) / sma_20,
            "timestamp": datetime.now().isoformat()
        }

    async def predict_volatility(self, volatility_data: Dict[str, Any]) -> Dict[str, Any]:
        """Predict price volatility."""
        prices = volatility_data.get("prices", [])
        if not prices:
            return {"volatility": 0.0}
            
        returns = np.diff(prices) / prices[:-1]
        volatility = np.std(returns) * np.sqrt(252)  # Annualized volatility
        
        return {
            "volatility": float(volatility),
            "risk_level": "HIGH" if volatility > 0.3 else "MEDIUM" if volatility > 0.15 else "LOW"
        }
    
    async def get_accuracy_metrics(self, days: int = 30) -> Dict[str, float]:
        """Get accuracy metrics for the prediction model."""
        return {
            "mae": 0.0, # Placeholder as we retrain per request
            "rmse": 0.0,
            "r2": 0.0
        }
        
    def get_model_status(self) -> str:
        return "active"
        
    async def reload_model(self):
        self.model = LinearRegression()
