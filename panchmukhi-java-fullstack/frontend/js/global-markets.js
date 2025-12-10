// Global Markets Widget with Live Status and Pre-Market Alerts

class GlobalMarketsWidget {
    constructor() {
        this.markets = [];
        this.updateInterval = null;
        this.alertsEnabled = false;
        this.notifiedMarkets = new Set();
    }

    async init() {
        await this.loadMarkets();
        this.render();
        this.startAutoUpdate();
        this.setupPreMarketAlerts();
    }

    async loadMarkets() {
        try {
            this.markets = await window.apiClient.get('/markets/public/all');
        } catch (error) {
            console.error('Failed to load markets:', error);
            this.markets = [];
        }
    }

    render() {
        const container = document.getElementById('global-markets-widget');
        if (!container) return;

        container.innerHTML = `
            <div class="widget-header">
                <h3>🌍 Global Markets</h3>
                <button onclick="globalMarketsWidget.toggleAlerts()" class="btn-alerts">
                    ${this.alertsEnabled ? '🔔 Alerts ON' : '🔕 Alerts OFF'}
                </button>
            </div>
            
            <div class="markets-grid">
                ${this.markets.map(market => this.renderMarketCard(market)).join('')}
            </div>
            
            <div class="next-opening">
                <h4>⏰ Next Market Opening</h4>
                <div id="next-opening-info">Loading...</div>
            </div>
        `;

        this.updateNextOpening();
    }

    renderMarketCard(market) {
        const statusClass = market.currentStatus.toLowerCase().replace('-', '');
        const isOpen = market.currentStatus === 'OPEN';
        const isPreMarket = market.currentStatus === 'PRE-MARKET';

        return `
            <div class="market-card ${statusClass}">
                <div class="market-flag">${market.flag}</div>
                <div class="market-info">
                    <h4>${market.name}</h4>
                    <div class="market-time">${market.localTime}</div>
                </div>
                <div class="market-status ${statusClass}">
                    ${market.currentStatus}
                </div>
                <div class="market-hours">
                    ${market.preMarketTime ? `Pre: ${market.preMarketTime}<br>` : ''}
                    Open: ${market.openTime} - ${market.closeTime}
                </div>
            </div>
        `;
    }

    async updateNextOpening() {
        try {
            const response = await window.apiClient.get('/markets/public/next-opening');
            const infoDiv = document.getElementById('next-opening-info');

            if (infoDiv && response.market) {
                infoDiv.innerHTML = `
                    <div class="next-market">
                        <span class="flag">${response.market.flag}</span>
                        <span class="name">${response.market.name}</span>
                        <span class="countdown">opens in: <strong>${response.timeUntilOpen}</strong></span>
                    </div>
                `;
            }
        } catch (error) {
            console.error('Failed to get next opening:', error);
        }
    }

    startAutoUpdate() {
        // Update every minute
        this.updateInterval = setInterval(() => {
            this.loadMarkets().then(() => {
                this.render();
                this.checkPreMarketAlerts();
            });
        }, 60000);
    }

    setupPreMarketAlerts() {
        // Request notification permission
        if ('Notification' in window && Notification.permission === 'default') {
            Notification.requestPermission();
        }
    }

    toggleAlerts() {
        this.alertsEnabled = !this.alertsEnabled;
        localStorage.setItem('marketAlertsEnabled', this.alertsEnabled);
        this.render();

        if (this.alertsEnabled) {
            this.setupPreMarketAlerts();
            this.showNotification('Market Alerts Enabled', 'You will be notified 10 and 5 minutes before markets open');
        }
    }

    async checkPreMarketAlerts() {
        if (!this.alertsEnabled) return;

        for (const market of this.markets) {
            const timeUntilOpen = this.getTimeUntilOpen(market);

            // Alert 10 minutes before
            if (timeUntilOpen > 9 && timeUntilOpen <= 10 && !this.notifiedMarkets.has(`${market.code}-10`)) {
                this.showNotification(
                    `${market.flag} ${market.name}`,
                    `Market opening in 10 minutes!`
                );
                this.notifiedMarkets.add(`${market.code}-10`);
            }

            // Alert 5 minutes before
            if (timeUntilOpen > 4 && timeUntilOpen <= 5 && !this.notifiedMarkets.has(`${market.code}-5`)) {
                this.showNotification(
                    `${market.flag} ${market.name}`,
                    `Market opening in 5 minutes! Get ready.`
                );
                this.notifiedMarkets.add(`${market.code}-5`);

                // Load opening analysis
                this.fetchOpeningAnalysis(market.code);
            }
        }
    }

    getTimeUntilOpen(market) {
        // Calculate time until market opens (simplified)
        // In production, use proper timezone calculations
        return 999; // Placeholder
    }

    async fetchOpeningAnalysis(marketCode) {
        // TODO: Implement when MarketOpeningAnalysisService is ready
        console.log(`Fetching opening analysis for ${marketCode}`);
    }

    showNotification(title, body) {
        if ('Notification' in window && Notification.permission === 'granted') {
            new Notification(title, {
                body: body,
                icon: '/assets/logo.png',
                badge: '/assets/logo.png'
            });
        }
    }

    destroy() {
        if (this.updateInterval) {
            clearInterval(this.updateInterval);
        }
    }
}

// Initialize widget
let globalMarketsWidget;
document.addEventListener('DOMContentLoaded', () => {
    globalMarketsWidget = new GlobalMarketsWidget();
    globalMarketsWidget.init();
});
