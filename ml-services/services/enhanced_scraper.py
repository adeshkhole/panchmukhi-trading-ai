# Phase 2: Enhanced Scraping Engine with Dynamic Selectors

from bs4 import BeautifulSoup
import requests
from newspaper import Article
import time
import random

class EnhancedScraper:
    """
    Dynamic scraper that uses ScraperConfig from database
    to scrape any website with CSS selectors
    """
    
    def __init__(self):
        self.headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'
        }
        
    def scrape_with_config(self, config):
        """
        Scrape website using configuration
        
        Args:
            config: Dictionary with keys:
                - baseUrl
                - targetSelector
                - titleSelector
                - contentSelector
                - dateSelector
                - imageSelector
                - linkSelector
        
        Returns:
            List of scraped articles
        """
        try:
            # Fetch the page
            response = requests.get(config['baseUrl'], headers=self.headers, timeout=30)
            response.raise_for_status()
            
            soup = BeautifulSoup(response.content, 'html.parser')
            
            # Find all article containers
            articles = []
            containers = soup.select(config.get('targetSelector', 'article'))
            
            for container in containers[:20]:  # Limit to 20 articles per scrape
                try:
                    article_data = self._extract_article_data(container, config)
                    if article_data:
                        articles.append(article_data)
                except Exception as e:
                    print(f"Error extracting article: {e}")
                    continue
            
            return articles
            
        except Exception as e:
            print(f"Error scraping {config.get('baseUrl')}: {e}")
            return []
    
    def _extract_article_data(self, container, config):
        """Extract individual article data from container"""
        data = {}
        
        # Title
        if config.get('titleSelector'):
            title_elem = container.select_one(config['titleSelector'])
            data['title'] = title_elem.get_text(strip=True) if title_elem else None
        
        # Content/Summary
        if config.get('contentSelector'):
            content_elem = container.select_one(config['contentSelector'])
            data['content'] = content_elem.get_text(strip=True) if content_elem else None
        
        # Date
        if config.get('dateSelector'):
            date_elem = container.select_one(config['dateSelector'])
            data['date'] = date_elem.get_text(strip=True) if date_elem else None
        
        # Image
        if config.get('imageSelector'):
            img_elem = container.select_one(config['imageSelector'])
            if img_elem:
                data['image'] = img_elem.get('src') or img_elem.get('data-src')
        
        # Link
        if config.get('linkSelector'):
            link_elem = container.select_one(config['linkSelector'])
            if link_elem:
                data['url'] = link_elem.get('href')
        
        # Only return if we have at least a title
        if data.get('title'):
            return data
        return None
    
    def extract_full_article(self, url):
        """
        Extract full article content using newspaper3k
        """
        try:
            article = Article(url)
            article.download()
            article.parse()
            article.nlp()  # For keywords and summary
            
            return {
                'title': article.title,
                'text': article.text,
                'summary': article.summary,
                'authors': article.authors,
                'publish_date': article.publish_date.isoformat() if article.publish_date else None,
                'top_image': article.top_image,
                'images': list(article.images)[:5],  # Limit to 5 images
                'keywords': article.keywords[:10]  # Limit to 10 keywords
            }
        except Exception as e:
            print(f"Error extracting article from {url}: {e}")
            return None
    
    def rate_limit(self, min_delay=1, max_delay=3):
        """Respectful rate limiting"""
        time.sleep(random.uniform(min_delay, max_delay))


# Pre-configured top sites
PRECONFIGURED_SCRAPERS = [
    {
        'websiteName': 'MoneyControl',
        'baseUrl': 'https://www.moneycontrol.com/news/business/',
        'category': 'news',
        'targetSelector': '.clearfix.newslist',
        'titleSelector': 'h2 a',
        'contentSelector': 'p',
        'dateSelector': 'span.ago',
        'imageSelector': 'img',
        'linkSelector': 'h2 a',
        'description': 'MoneyControl Business News'
    },
    {
        'websiteName': 'Economic Times',
        'baseUrl': 'https://economictimes.indiatimes.com/markets/stocks/news',
        'category': 'stocks',
        'targetSelector': '.eachStory',
        'titleSelector': 'h3',
        'imageSelector': 'img',
        'linkSelector': 'a',
        'description': 'Economic Times Stock News'
    },
    {
        'websiteName': 'NSE India',
        'baseUrl': 'https://www.nseindia.com/market-data/live-equity-market',
        'category': 'stocks',
        'targetSelector': '.table-row',
        'description': 'NSE Live Market Data'
    },
    {
        'websiteName': 'ISRO Official',
        'baseUrl': 'https://www.isro.gov.in/Updates.html',
        'category': 'isro',
        'targetSelector': '.content-wrapper',
        'titleSelector': 'h3',
        'dateSelector': '.date',
        'description': 'ISRO Official Updates'
    }
]

