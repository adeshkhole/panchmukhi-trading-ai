# Phase 3: AI-Powered Sentiment Analysis

from textblob import TextBlob
import re
from typing import List, Dict
import spacy

# Load spacy model for NER (Named Entity Recognition)
try:
    nlp = spacy.load("en_core_web_sm")
except:
    print("Warning: Spacy model not loaded. Install with: python -m spacy download en_core_web_sm")
    nlp = None

class SentimentAnalyzer:
    """
    Multi-dimensional sentiment analysis for financial news
    """
    
    def __init__(self):
        # Financial keywords for impact detection
        self.bullish_keywords = [
            'growth', 'profit', 'surge', 'gain', 'rise', 'up', 'positive', 
            'strong', 'boost', 'rally', 'expansion', 'launch', 'record',
            'improve', 'outperform', 'beat', 'exceed'
        ]
        
        self.bearish_keywords = [
            'loss', 'decline', 'fall', 'down', 'negative', 'weak', 'drop',
            'crash', 'slump', 'concern', 'worry', 'miss', 'disappoint',
            'underperform', 'cut', 'reduce', 'layoff', 'probe', 'scandal'
        ]
        
        self.sector_keywords = {
            'IT': ['software', 'technology', 'IT', 'digital', 'cloud', 'AI', 'cyber'],
            'Banking': ['bank', 'loan', 'credit', 'deposit', 'interest rate', 'RBI'],
            'Pharma': ['pharma', 'drug', 'medicine', 'healthcare', 'hospital'],
            'Auto': ['automobile', 'car', 'vehicle', 'EV', 'electric vehicle'],
            'Energy': ['power', 'oil', 'gas', 'energy', 'renewable', 'solar'],
            'Infrastructure': ['construction', 'infrastructure', 'cement', 'steel'],
            'FMCG': ['consumer', 'FMCG', 'retail', 'brand']
        }
        
        self.event_keywords = {
            'IPO': ['ipo', 'initial public offering', 'listing'],
            'Merger': ['merger', 'acquisition', 'takeover', 'buy', 'acquire'],
            'Expansion': ['expand', 'expansion', 'new plant', 'investment'],
            'Earnings': ['earnings', 'quarterly', 'results', 'Q1', 'Q2', 'Q3', 'Q4'],
            'Dividend': ['dividend', 'payout', 'distribution']
        }
    
    def analyze_news(self, text: str, language='en') -> Dict:
        """
        Comprehensive analysis of news article
        
        Returns:
            {
                'sentiment_score': float,  # -1 to +1
                'market_impact': str,      # Bullish/Bearish/Neutral
                'affected_sectors': list,
                'mentioned_companies': list,
                'key_events': list,
                'confidence': float
            }
        """
        if not text:
            return self._empty_analysis()
        
        # 1. Sentiment Score
        sentiment_score = self._calculate_sentiment(text)
        
        # 2. Market Impact
        market_impact = self._detect_market_impact(text, sentiment_score)
        
        # 3. Sector Relevance
        sectors = self._identify_sectors(text)
        
        # 4. Company Mentions
        companies = self._extract_companies(text)
        
        # 5. Key Events
        events = self._extract_events(text)
        
        # 6. Confidence Score
        confidence = self._calculate_confidence(text, sentiment_score)
        
        return {
            'sentiment_score': round(sentiment_score, 3),
            'market_impact': market_impact,
            'affected_sectors': sectors,
            'mentioned_companies': companies,
            'key_events': events,
            'confidence': round(confidence, 2)
        }
    
    def _calculate_sentiment(self, text: str) -> float:
        """Calculate sentiment using TextBlob and keyword matching"""
        # TextBlob sentiment
        blob = TextBlob(text.lower())
        textblob_score = blob.sentiment.polarity
        
        # Keyword-based adjustment
        bullish_count = sum(1 for kw in self.bullish_keywords if kw in text.lower())
        bearish_count = sum(1 for kw in self.bearish_keywords if kw in text.lower())
        
        keyword_score = (bullish_count - bearish_count) / max(bullish_count + bearish_count, 1)
        
        # Combined score (weighted average)
        final_score = (textblob_score * 0.6) + (keyword_score * 0.4)
        
        return max(-1, min(1, final_score))
    
    def _detect_market_impact(self, text: str, sentiment_score: float) -> str:
        """Determine market impact based on sentiment and keywords"""
        text_lower = text.lower()
        
        # Check for explicit market mentions
        market_up = any(word in text_lower for word in ['market rally', 'market surge', 'nifty up', 'sensex up'])
        market_down = any(word in text_lower for word in ['market crash', 'market fall', 'nifty down', 'sensex down'])
        
        if market_up or sentiment_score > 0.3:
            return "Bullish 📈"
        elif market_down or sentiment_score < -0.3:
            return "Bearish 📉"
        else:
            return "Neutral ⚖️"
    
    def _identify_sectors(self, text: str) -> List[str]:
        """Identify sectors mentioned in text"""
        text_lower = text.lower()
        identified_sectors = []
        
        for sector, keywords in self.sector_keywords.items():
            if any(kw in text_lower for kw in keywords):
                identified_sectors.append(sector)
        
        return identified_sectors
    
    def _extract_companies(self, text: str) -> List[str]:
        """Extract company names using NER"""
        if not nlp:
            # Fallback: Simple pattern matching for common Indian companies
            common_companies = [
                'Reliance', 'TCS', 'Infosys', 'HDFC', 'ICICI', 'Wipro',
                'HUL', 'ITC', 'Bajaj', 'Tata', 'Adani', 'Bharti Airtel'
            ]
            return [comp for comp in common_companies if comp in text]
        
        doc = nlp(text)
        companies = [ent.text for ent in doc.ents if ent.label_ == 'ORG']
        return list(set(companies))[:5]  # Limit to 5 unique companies
    
    def _extract_events(self, text: str) -> List[str]:
        """Identify key financial events"""
        text_lower = text.lower()
        events = []
        
        for event_type, keywords in self.event_keywords.items():
            if any(kw in text_lower for kw in keywords):
                events.append(event_type)
        
        return events
    
    def _calculate_confidence(self, text: str, sentiment_score: float) -> float:
        """Calculate confidence in the analysis"""
        # Factors affecting confidence:
        # 1. Text length (longer = more confident)
        # 2. Sentiment strength (stronger = more confident)
        # 3. Keyword presence
        
        length_score = min(len(text) / 1000, 1.0)  # Max at 1000 chars
        sentiment_strength = abs(sentiment_score)
        keyword_count = sum(1 for kw in self.bullish_keywords + self.bearish_keywords if kw in text.lower())
        keyword_score = min(keyword_count / 10, 1.0)
        
        confidence = (length_score * 0.3) + (sentiment_strength * 0.4) + (keyword_score * 0.3)
        return min(confidence, 1.0)
    
    def _empty_analysis(self) -> Dict:
        """Return empty analysis structure"""
        return {
            'sentiment_score': 0.0,
            'market_impact': 'Neutral ⚖️',
            'affected_sectors': [],
            'mentioned_companies': [],
            'key_events': [],
            'confidence': 0.0
        }
    
    def detect_market_mood(self, news_batch: List[str]) -> Dict:
        """
        Aggregate sentiment from multiple news articles
        to determine overall market mood
        """
        if not news_batch:
            return {'mood': 'Neutral ⚖️', 'description': 'No data', 'score': 0.0}
        
        scores = [self._calculate_sentiment(news) for news in news_batch]
        avg_score = sum(scores) / len(scores)
        
        if avg_score > 0.3:
            return {
                'mood': 'BULLISH 📈',
                'description': 'Market is optimistic and positive',
                'score': round(avg_score, 3),
                'articles_analyzed': len(news_batch)
            }
        elif avg_score < -0.3:
            return {
                'mood': 'BEARISH 📉',
                'description': 'Market is cautious and negative',
                'score': round(avg_score, 3),
                'articles_analyzed': len(news_batch)
            }
        else:
            return {
                'mood': 'NEUTRAL ⚖️',
                'description': 'Market sentiment is balanced',
                'score': round(avg_score, 3),
                'articles_analyzed': len(news_batch)
            }
