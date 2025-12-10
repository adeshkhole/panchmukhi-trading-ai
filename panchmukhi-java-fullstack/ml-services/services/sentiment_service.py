import logging
from typing import Dict, Any, List
from textblob import TextBlob
from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer

logger = logging.getLogger(__name__)

class SentimentService:
    """Service for sentiment analysis of text data."""
    
    def __init__(self):
        logger.info("Initializing Sentiment Service")
        self.vader = SentimentIntensityAnalyzer()
        
    async def initialize(self):
        """Initialize any async resources."""
        pass
        
    def is_ready(self) -> bool:
        """Check if service is ready."""
        return True

    async def analyze_text(self, text: str, language: str = "en") -> Dict[str, Any]:
        """Analyze sentiment of a single text."""
        try:
            # VADER analysis
            vader_scores = self.vader.polarity_scores(text)
            
            # TextBlob analysis
            blob = TextBlob(text)
            polarity = blob.sentiment.polarity
            subjectivity = blob.sentiment.subjectivity
            
            # Determine label
            compound = vader_scores['compound']
            if compound >= 0.05:
                label = "POSITIVE"
            elif compound <= -0.05:
                label = "NEGATIVE"
            else:
                label = "NEUTRAL"
                
            return {
                "sentiment_score": compound,
                "sentiment_label": label,
                "confidence": abs(compound),
                "details": {
                    "vader": vader_scores,
                    "textblob": {
                        "polarity": polarity,
                        "subjectivity": subjectivity
                    }
                },
                "keywords": list(blob.noun_phrases)
            }
        except Exception as e:
            logger.error(f"Error analyzing text: {e}")
            raise

    async def analyze_batch(self, texts: List[str], language: str = "en") -> List[Dict[str, Any]]:
        """Analyze sentiment of multiple texts."""
        results = []
        for text in texts:
            result = await self.analyze_text(text, language)
            results.append(result)
        return results

    async def analyze_news(self, news_data: Dict[str, Any]) -> Dict[str, Any]:
        """Analyze sentiment of news articles."""
        articles = news_data.get("articles", [])
        sentiments = []
        
        for article in articles:
            text = f"{article.get('title', '')} {article.get('description', '')}"
            if text.strip():
                result = await self.analyze_text(text)
                sentiments.append(result["sentiment_score"])
        
        avg_sentiment = sum(sentiments) / len(sentiments) if sentiments else 0
        
        return {
            "overall_sentiment": avg_sentiment,
            "article_count": len(articles),
            "sentiment_label": "POSITIVE" if avg_sentiment > 0.05 else "NEGATIVE" if avg_sentiment < -0.05 else "NEUTRAL"
        }

    async def analyze_social_media(self, social_data: Dict[str, Any]) -> Dict[str, Any]:
        """Analyze sentiment from social media data."""
        posts = social_data.get("posts", [])
        sentiments = []
        
        for post in posts:
            text = post.get("text", "")
            if text.strip():
                result = await self.analyze_text(text)
                sentiments.append(result["sentiment_score"])
                
        avg_sentiment = sum(sentiments) / len(sentiments) if sentiments else 0
        
        return {
            "social_sentiment": avg_sentiment,
            "post_count": len(posts),
            "sentiment_label": "POSITIVE" if avg_sentiment > 0.05 else "NEGATIVE" if avg_sentiment < -0.05 else "NEUTRAL"
        }
        
    def get_model_status(self) -> str:
        return "active"
        
    async def reload_model(self):
        self.vader = SentimentIntensityAnalyzer()
