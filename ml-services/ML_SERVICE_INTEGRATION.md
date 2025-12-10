# Update ML Service Main with New Endpoints

Add these endpoints to `ml-services/main.py`:

```python
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List, Dict, Optional
from services.enhanced_scraper import EnhancedScraper, PRECONFIGURED_SCRAPERS
from services.sentiment_analyzer import SentimentAnalyzer

app = FastAPI(title="Panchmukhi Trading ML Service")

# Initialize services
scraper = EnhancedScraper()
analyzer = SentimentAnalyzer()

# Models
class ScraperConfig(BaseModel):
    baseUrl: str
    targetSelector: Optional[str] = None
    titleSelector: Optional[str] = None
    contentSelector: Optional[str] = None
    dateSelector: Optional[str] = None
    imageSelector: Optional[str] = None
    linkSelector: Optional[str] = None

class SentimentRequest(BaseModel):
    text: str
    language: str = 'en'

# Existing endpoints (keep these)
@app.get("/")
def root():
    return {"message": "Panchmukhi Trading ML Service"}

# NEW ENDPOINTS FOR PHASE 2
@app.post("/scrape/with-config")
def scrape_with_config(config: ScraperConfig):
    """Scrape website using configuration"""
    try:
        config_dict = config.dict()
        articles = scraper.scrape_with_config(config_dict)
        return {"success": True, "articles": articles, "count": len(articles)}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/scrape/article/{url:path}")
def scrape_full_article(url: str):
    """Extract full article content from URL"""
    try:
        article = scraper.extract_full_article(url)
        if article:
            return {"success": True, "article": article}
        raise HTTPException(status_code=404, detail="Failed to extract article")
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/scrape/preconfigured")
def get_preconfigured_scrapers():
    """Get list of pre-configured scraper sites"""
    return {"scrapers": PRECONFIGURED_SCRAPERS}

# NEW ENDPOINTS FOR PHASE 3
@app.post("/analyze/sentiment")
def analyze_sentiment(request: SentimentRequest):
    """Analyze sentiment of text"""
    try:
        analysis = analyzer.analyze_news(request.text, request.language)
        return {"success": True, "analysis": analysis}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/analyze/market-mood")
def analyze_market_mood(articles: List[str]):
    """Analyze overall market mood from batch of articles"""
    try:
        mood = analyzer.detect_market_mood(articles)
        return {"success": True, "mood": mood}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

# Health check
@app.get("/health")
def health():
    return {"status": "healthy"}
```

## Installation Requirements

Add to `ml-services/requirements.txt`:
```
fastapi
uvicorn
beautifulsoup4
requests
newspaper3k
textblob
spacy
pydantic
```

## Setup Commands:
```bash
cd ml-services
pip install -r requirements.txt
python -m spacy download en_core_web_sm
python -m textblob.download_corpora
```

## Test Endpoints:
```bash
# Start service
uvicorn main:app --reload --port 8000

# Test scraping
curl -X POST http://localhost:8000/scrape/with-config \
  -H "Content-Type: application/json" \
  -d '{"baseUrl": "https://www.moneycontrol.com/news/business/"}'

# Test sentiment
curl -X POST http://localhost:8000/analyze/sentiment \
  -H "Content-Type: application/json" \
  -d '{"text": "Market rallies on strong earnings"}'
```
