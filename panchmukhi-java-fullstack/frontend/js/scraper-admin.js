// Scraper Admin Page JavaScript

let scrapers = [];
let currentFilter = { category: 'all', status: 'all' };

// Initialize on page load
document.addEventListener('DOMContentLoaded', () => {
    loadScrapers();
    loadStats();
});

// Load all scrapers
async function loadScrapers() {
    try {
        const response = await window.apiClient.get('/scraper-config/public/all');
        scrapers = response;
        renderScrapers(scrapers);
    } catch (error) {
        console.error('Failed to load scrapers:', error);
        showToast('Failed to load scrapers', 'error');
    }
}

// Load statistics
async function loadStats() {
    try {
        const stats = await window.apiClient.get('/scraper-config/public/stats');
        document.getElementById('total-scrapers').textContent = stats.total || 0;
        document.getElementById('active-scrapers').textContent = stats.active || 0;
    } catch (error) {
        console.error('Failed to load stats:', error);
    }
}

// Render scrapers table
function renderScrapers(scraperList) {
    const tbody = document.getElementById('scrapers-tbody');

    if (scraperList.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="empty-state">No scrapers configured</td></tr>';
        return;
    }

    tbody.innerHTML = scraperList.map(scraper => `
        <tr>
            <td>
                <strong>${scraper.websiteName}</strong>
                <br>
                <small>${scraper.baseUrl}</small>
            </td>
            <td>
                <span class="badge badge-${scraper.category}">${scraper.category}</span>
            </td>
            <td>
                <span class="status ${scraper.isActive ? 'active' : 'inactive'}">
                    ${scraper.isActive ? '✓ Active' : '✗ Inactive'}
                </span>
            </td>
            <td>${scraper.scrapingFrequency} min</td>
            <td>${scraper.lastScraped ? formatDateTime(scraper.lastScraped) : 'Never'}</td>
            <td>
                <span class="success-count">${scraper.successCount || 0}</span> / 
                <span class="failure-count">${scraper.failureCount || 0}</span>
            </td>
            <td class="actions">
                <button class="btn-icon" onclick="editScraper('${scraper.id}')" title="Edit">
                    ✏️
                </button>
                <button class="btn-icon" onclick="toggleScraper('${scraper.id}')" title="Toggle">
                    ${scraper.isActive ? '⏸️' : '▶️'}
                </button>
                <button class="btn-icon" onclick="deleteScraper('${scraper.id}')" title="Delete">
                    🗑️
                </button>
            </td>
        </tr>
    `).join('');
}

// Filter scrapers
function filterScrapers() {
    const categoryFilter = document.getElementById('category-filter').value;
    const statusFilter = document.getElementById('status-filter').value;

    currentFilter = { category: categoryFilter, status: statusFilter };

    let filtered = scrapers;

    if (categoryFilter !== 'all') {
        filtered = filtered.filter(s => s.category === categoryFilter);
    }

    if (statusFilter !== 'all') {
        const isActive = statusFilter === 'active';
        filtered = filtered.filter(s => s.isActive === isActive);
    }

    renderScrapers(filtered);
}

// Show add modal
function showAddModal() {
    document.getElementById('modal-title').textContent = 'Add New Scraper';
    document.getElementById('scraper-form').reset();
    document.getElementById('scraper-id').value = '';
    document.getElementById('is-active').checked = true;
    document.getElementById('test-results').style.display = 'none';
    document.getElementById('scraper-modal').style.display = 'flex';
}

// Edit scraper
async function editScraper(id) {
    const scraper = scrapers.find(s => s.id === id);
    if (!scraper) return;

    document.getElementById('modal-title').textContent = 'Edit Scraper';
    document.getElementById('scraper-id').value = scraper.id;
    document.getElementById('website-name').value = scraper.websiteName;
    document.getElementById('base-url').value = scraper.baseUrl;
    document.getElementById('category').value = scraper.category;
    document.getElementById('description').value = scraper.description || '';
    document.getElementById('target-selector').value = scraper.targetSelector || '';
    document.getElementById('title-selector').value = scraper.titleSelector || '';
    document.getElementById('content-selector').value = scraper.contentSelector || '';
    document.getElementById('date-selector').value = scraper.dateSelector || '';
    document.getElementById('image-selector').value = scraper.imageSelector || '';
    document.getElementById('link-selector').value = scraper.linkSelector || '';
    document.getElementById('scraping-frequency').value = scraper.scrapingFrequency;
    document.getElementById('is-active').checked = scraper.isActive;
    document.getElementById('test-results').style.display = 'none';

    document.getElementById('scraper-modal').style.display = 'flex';
}

// Close modal
function closeModal() {
    document.getElementById('scraper-modal').style.display = 'none';
}

// Submit form
document.getElementById('scraper-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const id = document.getElementById('scraper-id').value;
    const scraperData = {
        websiteName: document.getElementById('website-name').value,
        baseUrl: document.getElementById('base-url').value,
        category: document.getElementById('category').value,
        description: document.getElementById('description').value,
        targetSelector: document.getElementById('target-selector').value,
        titleSelector: document.getElementById('title-selector').value,
        contentSelector: document.getElementById('content-selector').value,
        dateSelector: document.getElementById('date-selector').value,
        imageSelector: document.getElementById('image-selector').value,
        linkSelector: document.getElementById('link-selector').value,
        scrapingFrequency: parseInt(document.getElementById('scraping-frequency').value),
        isActive: document.getElementById('is-active').checked
    };

    try {
        let saved;
        if (id) {
            saved = await window.apiClient.put(`/scraper-config/admin/update/${id}`, scraperData);
            showToast('Scraper updated successfully', 'success');
        } else {
            saved = await window.apiClient.post('/scraper-config/admin/create', scraperData);
            showToast('Scraper created successfully', 'success');
        }

        closeModal();
        loadScrapers();
        loadStats();
    } catch (error) {
        console.error('Failed to save scraper:', error);
        showToast('Failed to save scraper', 'error');
    }
});

// Toggle scraper
async function toggleScraper(id) {
    try {
        await window.apiClient.post(`/scraper-config/admin/toggle/${id}`);
        showToast('Scraper status toggled', 'success');
        loadScrapers();
        loadStats();
    } catch (error) {
        console.error('Failed to toggle scraper:', error);
        showToast('Failed to toggle scraper', 'error');
    }
}

// Delete scraper
async function deleteScraper(id) {
    if (!confirm('Are you sure you want to delete this scraper?')) return;

    try {
        await window.apiClient.delete(`/scraper-config/admin/delete/${id}`);
        showToast('Scraper deleted successfully', 'success');
        loadScrapers();
        loadStats();
    } catch (error) {
        console.error('Failed to delete scraper:', error);
        showToast('Failed to delete scraper', 'error');
    }
}

// Test scraper (placeholder - will implement with ML service)
async function testScraper() {
    const testOutput = document.getElementById('test-output');
    const testResults = document.getElementById('test-results');

    testOutput.innerHTML = '<div class="loading">Testing scraper...</div>';
    testResults.style.display = 'block';

    // TODO: Call ML service to test scraper
    setTimeout(() => {
        testOutput.innerHTML = `
            <div class="test-success">
                <h4>✓ Test Successful</h4>
                <p>Found 10 articles</p>
                <div class="sample-result">
                    <strong>Sample Title:</strong> Market opens strong on positive global cues<br>
                    <strong>Date:</strong> 2025-12-08<br>
                    <strong>Content:</strong> The Indian stock market opened on a positive note...
                </div>
            </div>
        `;
    }, 2000);
}

// Utility functions
function formatDateTime(dateTimeString) {
    const date = new Date(dateTimeString);
    const now = new Date();
    const diff = now - date;

    const minutes = Math.floor(diff / 60000);
    const hours = Math.floor(minutes / 60);
    const days = Math.floor(hours / 24);

    if (minutes < 1) return 'Just now';
    if (minutes < 60) return `${minutes} min ago`;
    if (hours < 24) return `${hours}h ago`;
    if (days < 7) return `${days}d ago`;

    return date.toLocaleDateString();
}

function showToast(message, type = 'info') {
    // Simple toast notification
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.textContent = message;
    document.body.appendChild(toast);

    setTimeout(() => toast.classList.add('show'), 100);
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}
