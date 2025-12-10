// Enhanced News Page with Live Data, Filters, and Market Mood

let allNews = [];
let filteredNews = [];
let currentFilters = {
    sector: 'all',
    sentiment: 'all',
    dateFrom: null,
    dateTo: null
};

// Initialize on page load
document.addEventListener('DOMContentLoaded', () => {
    loadMarketMood();
    loadLiveNews();
    setupFilters();
    setupInfiniteScroll();
});

// Load Market Mood
async function loadMarketMood() {
    try {
        // Get recent news for mood analysis
        const news = await window.apiClient.get('/news/public/all?limit=50');

        if (news && news.length > 0) {
            const articles = news.map(n => n.title + ' ' + (n.content || '')).slice(0, 50);

            // Call sentiment API
            const response = await window.apiClient.post('/sentiment/public/market-mood', articles);

            if (response.success && response.mood) {
                displayMarketMood(response.mood);
            }
        }
    } catch (error) {
        console.error('Failed to load market mood:', error);
        displayMarketMood({ mood: 'NEUTRAL ⚖️', description: 'Unable to determine', score: 0 });
    }
}

// Display Market Mood Card
function displayMarketMood(mood) {
    const moodCard = document.getElementById('market-mood-card');
    if (!moodCard) return;

    const moodClass = mood.mood.includes('BULLISH') ? 'bullish' :
        mood.mood.includes('BEARISH') ? 'bearish' : 'neutral';

    moodCard.innerHTML = `
        <div class="mood-header">
            <h3>📊 Today's Market Mood</h3>
            <span class="last-updated">Updated just now</span>
        </div>
        <div class="mood-indicator ${moodClass}">
            <div class="mood-icon">${mood.mood}</div>
            <div class="mood-score">Score: ${mood.score.toFixed(2)}</div>
        </div>
        <p class="mood-description">${mood.description}</p>
        ${mood.articles_analyzed ? `<p class="mood-meta">Based on ${mood.articles_analyzed} articles</p>` : ''}
    `;
}

// Load Live News
async function loadLiveNews() {
    try {
        showLoading();

        const news = await window.apiClient.get('/news/public/all');

        if (news && news.length > 0) {
            allNews = news;
            applyFilters();
        } else {
            showEmptyState();
        }
    } catch (error) {
        console.error('Failed to load news:', error);
        showError('Failed to load news. Please try again.');
    }
}

// Setup Filters
function setupFilters() {
    const sectorFilter = document.getElementById('sector-filter');
    const sentimentFilter = document.getElementById('sentiment-filter');
    const dateFromInput = document.getElementById('date-from');
    const dateToInput = document.getElementById('date-to');

    if (sectorFilter) {
        sectorFilter.addEventListener('change', (e) => {
            currentFilters.sector = e.target.value;
            applyFilters();
        });
    }

    if (sentimentFilter) {
        sentimentFilter.addEventListener('change', (e) => {
            currentFilters.sentiment = e.target.value;
            applyFilters();
        });
    }

    if (dateFromInput) {
        dateFromInput.addEventListener('change', (e) => {
            currentFilters.dateFrom = e.target.value;
            applyFilters();
        });
    }

    if (dateToInput) {
        dateToInput.addEventListener('change', (e) => {
            currentFilters.dateTo = e.target.value;
            applyFilters();
        });
    }
}

// Apply Filters
function applyFilters() {
    filteredNews = allNews.filter(article => {
        // Sector filter
        if (currentFilters.sector !== 'all') {
            if (!article.sector || article.sector !== currentFilters.sector) {
                return false;
            }
        }

        // Sentiment filter
        if (currentFilters.sentiment !== 'all') {
            const sentiment = getSentimentCategory(article.sentimentScore);
            if (sentiment !== currentFilters.sentiment) {
                return false;
            }
        }

        // Date filters
        if (currentFilters.dateFrom) {
            const articleDate = new Date(article.publishedAt);
            const filterDate = new Date(currentFilters.dateFrom);
            if (articleDate < filterDate) {
                return false;
            }
        }

        if (currentFilters.dateTo) {
            const articleDate = new Date(article.publishedAt);
            const filterDate = new Date(currentFilters.dateTo);
            if (articleDate > filterDate) {
                return false;
            }
        }

        return true;
    });

    renderNews(filteredNews);
}

// Render News
function renderNews(newsArray) {
    const newsContainer = document.getElementById('news-list');
    if (!newsContainer) return;

    if (newsArray.length === 0) {
        newsContainer.innerHTML = '<div class="empty-state">No news found matching filters</div>';
        return;
    }

    newsContainer.innerHTML = newsArray.map(article => `
        <div class="news-card" data-sentiment="${getSentimentCategory(article.sentimentScore)}">
            ${article.imageUrl ? `<img src="${article.imageUrl}" alt="${article.title}" class="news-image">` : ''}
            <div class="news-content">
                <div class="news-header">
                    ${article.sector ? `<span class="badge badge-${article.sector.toLowerCase()}">${article.sector}</span>` : ''}
                    ${article.sentimentScore !== undefined ? `
                        <span class="sentiment-badge ${getSentimentCategory(article.sentimentScore)}">
                            ${getSentimentIcon(article.sentimentScore)} ${getSentimentLabel(article.sentimentScore)}
                        </span>
                    ` : ''}
                </div>
                <h3 class="news-title">${article.title}</h3>
                <p class="news-summary">${article.content || article.summary || ''}</p>
                <div class="news-meta">
                    <span class="news-date">${formatDate(article.publishedAt)}</span>
                    ${article.language === 'mr' ? '<span class="lang-badge">मराठी</span>' : ''}
                </div>
            </div>
        </div>
    `).join('');
}

// Infinite Scroll
function setupInfiniteScroll() {
    let loading = false;
    let page = 1;

    window.addEventListener('scroll', async () => {
        if (loading) return;

        const scrollPosition = window.innerHeight + window.scrollY;
        const pageHeight = document.documentElement.scrollHeight;

        if (scrollPosition >= pageHeight - 500) {
            loading = true;
            page++;

            try {
                const moreNews = await window.apiClient.get(`/news/public/all?page=${page}`);
                if (moreNews && moreNews.length > 0) {
                    allNews = [...allNews, ...moreNews];
                    applyFilters();
                }
            } catch (error) {
                console.error('Failed to load more news:', error);
            } finally {
                loading = false;
            }
        }
    });
}

// Utility Functions
function getSentimentCategory(score) {
    if (score === undefined || score === null) return 'neutral';
    if (score > 0.3) return 'bullish';
    if (score < -0.3) return 'bearish';
    return 'neutral';
}

function getSentimentIcon(score) {
    if (score > 0.3) return '📈';
    if (score < -0.3) return '📉';
    return '⚖️';
}

function getSentimentLabel(score) {
    if (score > 0.3) return 'Bullish';
    if (score < -0.3) return 'Bearish';
    return 'Neutral';
}

function formatDate(dateString) {
    const date = new Date(dateString);
    const now = new Date();
    const diff = now - date;
    const hours = Math.floor(diff / 3600000);
    const days = Math.floor(hours / 24);

    if (hours < 1) return 'Just now';
    if (hours < 24) return `${hours}h ago`;
    if (days < 7) return `${days}d ago`;

    return date.toLocaleDateString();
}

function showLoading() {
    const newsContainer = document.getElementById('news-list');
    if (newsContainer) {
        newsContainer.innerHTML = '<div class="loading">Loading news...</div>';
    }
}

function showEmptyState() {
    const newsContainer = document.getElementById('news-list');
    if (newsContainer) {
        newsContainer.innerHTML = '<div class="empty-state">No news available</div>';
    }
}

function showError(message) {
    const newsContainer = document.getElementById('news-list');
    if (newsContainer) {
        newsContainer.innerHTML = `<div class="error-state">${message}</div>`;
    }
}
