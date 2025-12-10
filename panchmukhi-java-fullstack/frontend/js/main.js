// Panchmukhi Trading Brain Pro - Main JavaScript
// Multi-language support and real-time trading features

class ApiClient {
    constructor() {
        this.baseUrl = 'http://localhost:8083/api';
        this.token = localStorage.getItem('authToken');
    }

    async call(method, endpoint, data = null) {
        const url = `${this.baseUrl}${endpoint}`;
        const headers = {
            'Content-Type': 'application/json'
        };

        if (this.token) {
            headers['Authorization'] = `Bearer ${this.token}`;
        }

        const options = {
            method,
            headers
        };
        if (data) options.body = JSON.stringify(data);

        try {
            const response = await fetch(url, options);
            if (response.status === 401) {
                // Token expired or invalid
                localStorage.removeItem('authToken');
                window.location.href = 'login.html';
                return;
            }
            if (!response.ok) {
                throw new Error(`API call failed: ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            console.error(`API Error (${method} ${endpoint}):`, error);
            throw error;
        }
    }

    // Auth
    async login(email, password) { return this.call('POST', '/auth/signin', { email, password }); }
    async register(data) { return this.call('POST', '/auth/signup', data); }

    // Market Data
    async getMarketSymbols() { return this.call('GET', '/market/public/all'); }
    async getMarketData(symbol) { return this.call('GET', `/market/public/${symbol}`); }
    async getMarketDashboard() { return this.call('GET', '/market/public/dashboard'); }

    // Domain Specific
    async getIPOs(status = 'all') {
        if (status === 'all') return this.call('GET', '/ipo/public/all');
        return this.call('GET', `/ipo/public/status/${status}`);
    }
    async getISROMissions() { return this.call('GET', '/isro/missions'); }
    async getNews(params = {}) {
        if (params.category) return this.call('GET', `/news/public/category/${params.category}`);
        if (params.q) return this.call('GET', `/news/public/search?q=${params.q}`);
        return this.call('GET', '/news/public/all');
    }
    async getOptionChain() { return this.call('GET', '/options/chain'); }
}

// Expose globally
window.apiClient = new ApiClient();

class PanchmukhiTradingApp {
    constructor() {
        this.currentLanguage = 'en';
        this.user = null;
        this.authToken = localStorage.getItem('authToken');
        this.websocket = null;
        this.charts = {};

        // API Configuration
        // API Configuration
        this.apiBaseUrl = 'http://localhost:8083/api';
        this.wsUrl = 'ws://localhost:8083/ws';

        // Language translations
        this.translations = {
            en: {
                title: "Panchmukhi Trading Brain Pro",
                subtitle: "AI-powered trading platform with multi-language support for Indian markets",
                getStarted: "Get Started Now",
                viewDemo: "View Demo",
                login: "Login",
                register: "Register",
                dashboard: "Dashboard",
                market: "Market",
                news: "News",
                ipo: "IPO",
                sectors: "Sectors",
                options: "Options",
                isro: "ISRO",
                alerts: "Alerts",
                loginTitle: "Login",
                registerTitle: "Register",
                loginBtn: "Login",
                registerBtn: "Register",
                switchToRegister: "Register Now",
                switchToLogin: "Login Now",
                emailPlaceholder: "Email Address",
                passwordPlaceholder: "Password",
                namePlaceholder: "Full Name",
                phonePlaceholder: "Phone Number",
                noAccount: "Don't have an account?",
                hasAccount: "Already have an account?",
                pleaseLogin: "Please login first",
                dashboardTitle: "Trading Dashboard",
                marketChart: "Market Chart",
                sectorPerformance: "Sector Performance",
                topGainers: "Top Gainers",
                topLosers: "Top Losers",
                marketData: "Market Data",
                searchStock: "Search stock...",
                allStocks: "All Stocks",
                stock: "Stock",
                price: "Price",
                change: "Change",
                volume: "Volume",
                action: "Action",
                marketNews: "Market News",
                ipoAnalysis: "IPO Analysis",
                openIpo: "Open IPO",
                upcomingIpo: "Upcoming IPO",
                listedIpo: "Listed IPO",
                performance: "Performance",
                optionChain: "Option Chain (NIFTY)",
                isroMissions: "ISRO Missions",
                socialTrends: "Social Media Trends",
                sectorAnalysis: "Sector Analysis",
                tradingAlerts: "Trading Alerts",
                createAlert: "Create New Alert",
                stockSymbol: "Stock Symbol",
                targetPrice: "Target Price",
                alertType: "Alert Type",
                priceAbove: "Price Above",
                priceBelow: "Price Below",
                percentChange: "Percentage Change",
                expiry: "Expiry",
                message: "Message",
                createAlertBtn: "Create Alert",
                alertBtn: "Alert",
                activeAlerts: "Active Alerts",
                fullName: "Full Name",
                phoneNumber: "Phone Number",
                email: "Email",
                password: "Password",
                demoStart: "Demo starting... loading real-time market data!",
                alertCreated: "Alert created successfully!",
                alertFailed: "Failed to create alert. Please try again.",
                target: "Target",
                expiry: "Expiry",
                off: "Off",
                on: "On",
                delete: "Delete",
                logout: "Logout"
            },
            hi: {
                title: "पंचमुखी ट्रेडिंग ब्रेन प्रो",
                subtitle: "भारतीय बाजारों के लिए बहुभाषी समर्थन के साथ AI-संचालित ट्रेडिंग प्लेटफॉर्म",
                getStarted: "अभी शुरू करें",
                viewDemo: "डेमो देखें",
                login: "लॉगिन",
                register: "रजिस्टर",
                dashboard: "डैशबोर्ड",
                market: "मार्केट",
                news: "समाचार",
                ipo: "IPO",
                sectors: "सेक्टर्स",
                options: "विकल्प",
                isro: "इस्रो",
                alerts: "अलर्ट्स",
                loginTitle: "लॉगिन",
                registerTitle: "रजिस्टर",
                loginBtn: "लॉगिन",
                registerBtn: "रजिस्टर",
                switchToRegister: "रजिस्टर करें",
                switchToLogin: "लॉगिन करें",
                emailPlaceholder: "ईमेल पता",
                passwordPlaceholder: "पासवर्ड",
                namePlaceholder: "पूरा नाम",
                phonePlaceholder: "फोन नंबर",
                noAccount: "खाता नहीं है?",
                hasAccount: "पहले से खाता है?",
                pleaseLogin: "कृपया पहले लॉगिन करें",
                alertBtn: "अलर्ट",
                demoStart: "डेमो शुरू हो रहा है...",
                alertCreated: "अलर्ट सफलतापूर्वक बनाया गया!",
                target: "लक्ष्य",
                expiry: "समाप्ति",
                off: "बंद",
                on: "चालू",
                delete: "हटाएं"
            },
            gu: {
                title: "પંચમુખી ટ્રેડિંગ બ્રેન પ્રો",
                subtitle: "ભારતીય બજારો માટે બહુભાષી સમર્થન સાથે AI-ચલિત ટ્રેડિંગ પ્લેટફોર્મ",
                getStarted: "આજે શરૂ કરો",
                viewDemo: "ડેમો જુઓ",
                login: "લોગિન",
                register: "રજિસ્ટર",
                dashboard: "ડેશબોર્ડ",
                market: "માર્કેટ",
                news: "સમાચાર",
                ipo: "IPO",
                sectors: "સેક્ટર્સ",
                options: "વિકલ્પો",
                isro: "ઇસરો",
                alerts: "અલર્ટ્સ",
                loginTitle: "લોગિન",
                registerTitle: "રજિસ્ટર",
                loginBtn: "લોગિન",
                registerBtn: "રજિસ્ટર",
                switchToRegister: "રજિસ્ટર કરો",
                switchToLogin: "લોગિન કરો",
                emailPlaceholder: "ઇમેઇલ સરનામું",
                passwordPlaceholder: "પાસવર્ડ",
                namePlaceholder: "પૂરું નામ",
                phonePlaceholder: "ફોન નંબર",
                noAccount: "ખાતું નથી?",
                hasAccount: "પહેલેથી ખાતું છે?",
                pleaseLogin: "કૃપા કરીને પહેલા લોગિન કરો",
                alertBtn: "અલર્ટ",
                demoStart: "ડેમો શરૂ થઈ રહ્યો છે...",
                alertCreated: "અલર્ટ સફળતાપૂર્વક બનાવવામાં આવ્યું!",
                target: "લક્ષ્ય",
                expiry: "સમાપ્તિ",
                off: "બંધ",
                on: "ચાલુ",
                delete: "કાઢી નાખો"
            },
            kn: {
                title: "ಪಂಚಮುಖಿ ಟ್ರೇಡಿಂಗ್ ಬ್ರೇನ್ ಪ್ರೋ",
                subtitle: "ಭಾರತೀಯ ಮಾರುಕಟ್ಟೆಗಳಿಗೆ ಬಹುಭಾಷಾ ಬೆಂಬಲದೊಂದಿಗೆ AI-ಚಾಲಿತ ಟ್ರೇಡಿಂಗ್ ಪ್ಲಾಟ್‌ಫಾರ್ಮ್",
                getStarted: "ಈಗ ಪ್ರಾರಂಭಿಸಿ",
                viewDemo: "ಡೆಮೋ ನೋಡಿ",
                login: "ಲಾಗಿನ್",
                register: "ನೋಂದಣಿ",
                dashboard: "ಡ್ಯಾಶ್‌ಬೋರ್ಡ್",
                market: "ಮಾರ್ಕೆಟ್",
                news: "ಸುದ್ದಿ",
                ipo: "IPO",
                sectors: "ಸೆಕ್ಟರ್ಸ್",
                options: "ಆಯ್ಕೆಗಳು",
                isro: "ಇಸ್ರೋ",
                alerts: "ಅಲರ್ಟ್‌ಗಳು",
                loginTitle: "ಲಾಗಿನ್",
                registerTitle: "ನೋಂದಣಿ",
                loginBtn: "ಲಾಗಿನ್",
                registerBtn: "ನೋಂದಣಿ",
                switchToRegister: "ನೋಂದಾಯಿಸಿ",
                switchToLogin: "ಲಾಗಿನ್ ಮಾಡಿ",
                emailPlaceholder: "ಇಮೇಲ್ ವಿಳಾಸ",
                passwordPlaceholder: "ಪಾಸ್‌ವರ್ಡ್",
                namePlaceholder: "ಪೂರ್ಣ ಹೆಸರು",
                phonePlaceholder: "ದೂರವಾಣಿ ಸಂಖ್ಯೆ",
                noAccount: "ಖಾತೆ ಇಲ್ಲವೇ?",
                hasAccount: "ಈಗಾಗಲೇ ಖಾತೆ ಇದೆಯೇ?",
                pleaseLogin: "ದಯವಿಟ್ಟು ಮೊದಲು ಲಾಗಿನ್ ಮಾಡಿ",
                alertBtn: "ಅಲರ್ಟ್",
                demoStart: "ಡೆಮೊ ಪ್ರಾರಂಭವಾಗುತ್ತಿದೆ...",
                alertCreated: "ಅಲರ್ಟ್ ಯಶಸ್ವಿಯಾಗಿ ರಚಿಸಲಾಗಿದೆ!",
                target: "ಗುರಿ",
                expiry: "ಮುಕ್ತಾಯ",
                off: "ಆಫ್",
                on: "ಆನ್",
                delete: "ಅಳಿಸಿ"
            }
        };

        // Expose app instance globally
        window.app = this;

        this.init();
    }

    async init() {
        this.setupEventListeners();
        this.initializeParticleSystem();
        this.setupTypewriterEffect();
        this.connectWebSocket();
        this.loadMarketData();
        this.setupCharts();
        this.startRealTimeUpdates();

        // Check for saved auth token
        const token = localStorage.getItem('authToken');
        if (token) {
            this.authToken = token;
            await this.loadUserData();
        }
    }

    setupEventListeners() {
        // Language selector
        document.getElementById('language-selector').addEventListener('change', (e) => {
            this.changeLanguage(e.target.value);
        });

        // Navigation
        document.querySelectorAll('.nav-link').forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                const target = e.target.getAttribute('href').substring(1);
                this.scrollToSection(target);
            });
        });

        // Auth buttons
        document.getElementById('login-btn').addEventListener('click', () => this.showAuthModal('login'));
        document.getElementById('register-btn').addEventListener('click', () => this.showAuthModal('register'));
        document.getElementById('get-started-btn').addEventListener('click', () => this.showAuthModal('register'));
        document.getElementById('demo-btn').addEventListener('click', () => this.startDemo());

        // Modal controls
        document.getElementById('close-modal').addEventListener('click', () => this.hideAuthModal());
        document.getElementById('auth-switch').addEventListener('click', () => this.toggleAuthMode());
        document.getElementById('auth-form').addEventListener('submit', (e) => this.handleAuth(e));

        // Alert form
        document.getElementById('alert-form').addEventListener('submit', (e) => this.handleAlertSubmission(e));

        // Stock search
        document.getElementById('stock-search').addEventListener('input', (e) => this.searchStocks(e.target.value));

        // Filter buttons
        document.querySelectorAll('.filter-btn').forEach(btn => {
            btn.addEventListener('click', (e) => this.filterStocks(e.target.dataset.filter));
        });

        // News categories
        document.querySelectorAll('.news-category-btn').forEach(btn => {
            btn.addEventListener('click', (e) => this.filterNews(e.target.dataset.category));
        });

        // IPO tabs
        document.querySelectorAll('.ipo-tab-btn').forEach(btn => {
            btn.addEventListener('click', (e) => this.switchIPOTab(e.target.dataset.tab));
        });
    }

    changeLanguage(lang) {
        this.currentLanguage = lang;
        const t = this.translations[lang];

        // Update all elements with data-lang-key
        document.querySelectorAll('[data-lang-key]').forEach(element => {
            const key = element.getAttribute('data-lang-key');
            if (t[key]) {
                if (element.tagName === 'INPUT' || element.tagName === 'TEXTAREA') {
                    element.placeholder = t[key];
                } else {
                    element.textContent = t[key];
                }
            }
        });

        // Update specific elements (legacy support or dynamic content)
        if (document.getElementById('hero-title')) document.getElementById('hero-title').textContent = t.title;
        if (document.getElementById('hero-subtitle')) document.getElementById('hero-subtitle').textContent = t.subtitle;

        // Update body class for language-specific styling
        document.body.className = document.body.className.replace(/\b(marathi-text|hindi-text|gujarati-text|kannada-text)\b/g, '');
        if (lang !== 'en') {
            document.body.classList.add(`${lang}-text`);
        }
    }

    setupTypewriterEffect() {
        const heroTitle = document.getElementById('hero-title');
        const text = this.translations[this.currentLanguage].title;

        new Typed(heroTitle, {
            strings: [text],
            typeSpeed: 50,
            showCursor: false,
            onComplete: () => {
                // Add gradient animation after typing
                heroTitle.classList.add('animate-pulse');
            }
        });
    }

    initializeParticleSystem() {
        const canvas = document.getElementById('particle-canvas');
        const ctx = canvas.getContext('2d');

        canvas.width = window.innerWidth;
        canvas.height = window.innerHeight;

        const particles = [];
        const particleCount = 50;

        // Create particles
        for (let i = 0; i < particleCount; i++) {
            particles.push({
                x: Math.random() * canvas.width,
                y: Math.random() * canvas.height,
                vx: (Math.random() - 0.5) * 0.5,
                vy: (Math.random() - 0.5) * 0.5,
                size: Math.random() * 2 + 1,
                opacity: Math.random() * 0.5 + 0.2
            });
        }

        function animate() {
            ctx.clearRect(0, 0, canvas.width, canvas.height);

            particles.forEach(particle => {
                // Update position
                particle.x += particle.vx;
                particle.y += particle.vy;

                // Wrap around edges
                if (particle.x < 0) particle.x = canvas.width;
                if (particle.x > canvas.width) particle.x = 0;
                if (particle.y < 0) particle.y = canvas.height;
                if (particle.y > canvas.height) particle.y = 0;

                // Draw particle
                ctx.beginPath();
                ctx.arc(particle.x, particle.y, particle.size, 0, Math.PI * 2);
                ctx.fillStyle = `rgba(59, 130, 246, ${particle.opacity})`;
                ctx.fill();
            });

            requestAnimationFrame(animate);
        }

        animate();

        // Resize handler
        window.addEventListener('resize', () => {
            canvas.width = window.innerWidth;
            canvas.height = window.innerHeight;
        });
    }

    connectWebSocket() {
        try {
            this.websocket = new WebSocket(this.wsUrl);

            this.websocket.onopen = () => {
                console.log('WebSocket connected');
                this.websocket.send(JSON.stringify({ type: 'subscribe', channel: 'market_updates' }));
            };

            this.websocket.onmessage = (event) => {
                const data = JSON.parse(event.data);
                this.handleWebSocketMessage(data);
            };

            this.websocket.onerror = (error) => {
                console.error('WebSocket error:', error);
            };

            this.websocket.onclose = () => {
                console.log('WebSocket disconnected');
                // Attempt to reconnect after 5 seconds
                setTimeout(() => this.connectWebSocket(), 5000);
            };
        } catch (error) {
            console.error('Failed to connect WebSocket:', error);
        }
    }

    handleWebSocketMessage(data) {
        switch (data.type) {
            case 'market_update':
                this.updateMarketData(data.data);
                break;
            case 'news_update':
                this.updateNews(data.data);
                break;
            case 'alert_update':
                this.handleAlert(data.data);
                break;
        }
    }

    updateMarketData(stock) {
        // Update Ticker Logic (if applicable)
        const normalizeSymbol = stock.symbol.toLowerCase().replace(/\s+/g, '');
        const tickerElement = document.getElementById(`${normalizeSymbol}-value`);
        if (tickerElement) {
            tickerElement.textContent = stock.currentPrice.toLocaleString('en-IN', { maximumFractionDigits: 2 });
            // Add flash effect
            tickerElement.parentElement.classList.add('flash-update');
            setTimeout(() => tickerElement.parentElement.classList.remove('flash-update'), 1000);
        }

        // Update Stock Table Row
        // We look for a row that corresponds to this symbol. 
        // Since we don't have IDs on tr, we search by text content or re-render (expensive).
        // Better: Find the specific cells if possible, or iterating rows.
        const rows = document.querySelectorAll('#stock-table-body tr');
        rows.forEach(row => {
            const symbolCell = row.querySelector('td:first-child .font-medium');
            if (symbolCell && symbolCell.textContent.trim() === stock.symbol) {
                // Update Price
                const priceCell = row.children[1];
                const oldPrice = parseFloat(priceCell.textContent.replace(/[₹,]/g, ''));
                priceCell.textContent = `₹${stock.currentPrice.toLocaleString('en-IN', { minimumFractionDigits: 2 })}`;

                // Color update based on price movement
                if (stock.currentPrice > oldPrice) {
                    priceCell.classList.add('text-green-400');
                    priceCell.classList.remove('text-red-400', 'text-white');
                } else if (stock.currentPrice < oldPrice) {
                    priceCell.classList.add('text-red-400');
                    priceCell.classList.remove('text-green-400', 'text-white');
                }

                // Update Change
                const changeCell = row.children[2];
                changeCell.textContent = `${stock.changeAmount >= 0 ? '+' : ''}${stock.changeAmount}`;
                changeCell.className = `py-3 px-4 text-right font-mono ${stock.changeAmount >= 0 ? 'text-green-400' : 'text-red-400'}`;

                // Update Percent
                const percentCell = row.children[3];
                percentCell.textContent = `${stock.changePercent >= 0 ? '+' : ''}${stock.changePercent.toFixed(2)}%`;
                percentCell.className = `py-3 px-4 text-right font-mono ${stock.changePercent >= 0 ? 'text-green-400' : 'text-red-400'}`;
            }
        });
    }

    updateNews(newsItem) {
        // Prepend new news item to the list
        const newsContainer = document.getElementById('market-news-body'); // Assuming this ID exists or use generic container
        if (newsContainer) {
            const newsHtml = `
                <div class="glass-effect p-4 rounded-lg mb-4 border-l-4 border-blue-500 animate-fade-in-down">
                    <div class="flex justify-between items-start mb-2">
                        <h3 class="font-bold text-lg text-white">${newsItem.title}</h3>
                        <span class="text-xs text-slate-400">${new Date(newsItem.publishedAt).toLocaleTimeString()}</span>
                    </div>
                    <p class="text-slate-300 text-sm">${newsItem.summary}</p>
                    <div class="flex justify-between items-center mt-3">
                        <span class="text-xs bg-slate-700 px-2 py-1 rounded text-slate-300">${newsItem.source}</span>
                        <span class="text-xs ${newsItem.sentimentLabel === 'Positive' ? 'text-green-400' : 'text-red-400'}">${newsItem.sentimentLabel}</span>
                    </div>
                </div>
            `;
            newsContainer.insertAdjacentHTML('afterbegin', newsHtml);
        }
    }

    async loadMarketData() {
        try {
            // Use the global apiClient
            console.log('Loading market data...');
            const dashboardData = await window.apiClient.getMarketDashboard();

            if (dashboardData && (dashboardData.gainers || dashboardData.allStocks)) {
                this.updateDashboard(dashboardData);
            } else {
                console.warn('Dashboard data is empty, loading mock data for demo.');
                this.loadMockMarketData();
            }

            // Load other data sources
            this.loadISROData();
            this.loadOptionChain();

        } catch (error) {
            console.error('Failed to load market data:', error);
            // Fallback to mock data so the UI isn't empty
            this.loadMockMarketData();
        }
    }

    async loadISROData() {
        try {
            const missions = await this.apiCall('/isro/missions');
            const container = document.getElementById('isro-grid');
            if (container && missions) {
                container.innerHTML = missions.map(mission => `
                    <div class="glass-effect rounded-lg p-6 hover-lift border-l-4 ${mission.status === 'Success' ? 'border-green-500' : 'border-blue-500'}">
                        <h3 class="text-xl font-bold text-white mb-2">${mission.name}</h3>
                        <div class="flex justify-between items-center mb-4">
                            <span class="text-sm text-slate-400">${mission.date}</span>
                            <span class="px-2 py-1 rounded text-xs font-medium ${mission.status === 'Success' ? 'bg-green-900 text-green-300' : 'bg-blue-900 text-blue-300'}">
                                ${mission.status}
                            </span>
                        </div>
                        <p class="text-slate-300 text-sm">${mission.description}</p>
                    </div>
                `).join('');
            }
        } catch (error) {
            console.error('Failed to load ISRO data:', error);
        }
    }

    async loadSocialData() {
        try {
            const posts = await this.apiCall('/social/feed');
            const container = document.getElementById('social-grid');
            if (container && posts) {
                container.innerHTML = posts.map(post => `
                    <div class="glass-effect rounded-lg p-6 hover-lift">
                        <div class="flex items-center mb-3">
                            <div class="w-8 h-8 rounded-full ${post.source === 'Twitter' ? 'bg-blue-500' : 'bg-orange-500'} flex items-center justify-center mr-3">
                                <span class="text-white text-xs">${post.source[0]}</span>
                            </div>
                            <div>
                                <div class="font-bold text-sm text-white">${post.user}</div>
                                <div class="text-xs text-slate-400">${post.source}</div>
                            </div>
                            <div class="ml-auto">
                                <span class="px-2 py-1 rounded text-xs font-medium ${post.sentiment === 'Bullish' ? 'bg-green-900 text-green-300' : 'bg-red-900 text-red-300'}">
                                    ${post.sentiment}
                                </span>
                            </div>
                        </div>
                        <p class="text-slate-300 text-sm mb-3">${post.content}</p>
                        <div class="flex items-center text-slate-400 text-xs">
                            <span>❤️ ${post.likes}</span>
                        </div>
                    </div>
                `).join('');
            }
        } catch (error) {
            console.error('Failed to load Social data:', error);
        }
    }

    async loadOptionChain() {
        try {
            const data = await this.apiCall('/options/chain');
            if (data) {
                document.getElementById('option-spot-price').textContent = data.spot_price;
                document.getElementById('option-pcr').textContent = data.pcr;

                const tbody = document.getElementById('option-chain-body');
                if (tbody && data.strikes) {
                    tbody.innerHTML = data.strikes.map(strike => `
                        <tr class="border-b border-slate-700 hover:bg-slate-700">
                            <td class="p-2 text-right text-green-400">${(strike.ce_oi / 1000).toFixed(1)}k</td>
                            <td class="p-2 text-right">₹${strike.ce_price}</td>
                            <td class="p-2 text-center font-bold bg-slate-800">${strike.strike}</td>
                            <td class="p-2 text-right">₹${strike.pe_price}</td>
                            <td class="p-2 text-right text-red-400">${(strike.pe_oi / 1000).toFixed(1)}k</td>
                        </tr>
                    `).join('');
                }
            }
        } catch (error) {
            console.error('Failed to load Option Chain:', error);
        }
    }

    loadMockMarketData() {
        // Mock data for demonstration
        const mockData = {
            gainers: [
                { symbol: 'RELIANCE', price: 2456.80, change: 45.20, changePercent: 1.88 },
                { symbol: 'TCS', price: 3421.50, change: 32.10, changePercent: 0.95 },
                { symbol: 'HDFC', price: 1654.30, change: 28.90, changePercent: 1.78 }
            ],
            losers: [
                { symbol: 'INFY', price: 1456.20, change: -23.40, changePercent: -1.58 },
                { symbol: 'ICICI', price: 987.60, change: -15.30, changePercent: -1.53 },
                { symbol: 'SBI', price: 654.80, change: -12.20, changePercent: -1.83 }
            ],
            volume: [
                { symbol: 'RELIANCE', volume: 12500000 },
                { symbol: 'TCS', volume: 8900000 },
                { symbol: 'HDFC', volume: 6700000 }
            ]
        };

        this.updateDashboard(mockData);
    }

    updateDashboard(data) {
        // Update top gainers
        const gainersContainer = document.getElementById('top-gainers');
        if (gainersContainer && data.gainers) {
            gainersContainer.innerHTML = data.gainers.map(stock => `
                <div class="flex justify-between items-center p-3 bg-slate-800 rounded">
                    <span class="font-medium">${stock.symbol}</span>
                    <div class="text-right">
                        <div class="text-green-400">₹${stock.price}</div>
                        <div class="text-green-400 text-sm">+${stock.changePercent}%</div>
                    </div>
                </div>
            `).join('');
        }

        // Update top losers
        const losersContainer = document.getElementById('top-losers');
        if (losersContainer && data.losers) {
            losersContainer.innerHTML = data.losers.map(stock => `
                <div class="flex justify-between items-center p-3 bg-slate-800 rounded">
                    <span class="font-medium">${stock.symbol}</span>
                    <div class="text-right">
                        <div class="text-red-400">₹${stock.price}</div>
                        <div class="text-red-400 text-sm">${stock.changePercent}%</div>
                    </div>
                </div>
            `).join('');
        }

        // Update stock table
        this.updateStockTable(data.allStocks || []);
    }

    updateStockTable(stocks) {
        const tableBody = document.getElementById('stock-table-body');
        if (!tableBody) return;

        tableBody.innerHTML = stocks.map(stock => `
            <tr class="border-b border-slate-700 hover:bg-slate-800">
                <td class="py-3 px-4">
                    <div class="font-medium">${stock.symbol}</div>
                    <div class="text-sm text-slate-400">${stock.exchange}</div>
                </td>
                <td class="py-3 px-4 text-right font-mono">₹${stock.currentPrice}</td>
                <td class="py-3 px-4 text-right font-mono ${stock.changeAmount >= 0 ? 'text-green-400' : 'text-red-400'}">
                    ${stock.changeAmount >= 0 ? '+' : ''}${stock.changeAmount}
                </td>
                <td class="py-3 px-4 text-right font-mono ${stock.changePercent >= 0 ? 'text-green-400' : 'text-red-400'}">
                    ${stock.changePercent >= 0 ? '+' : ''}${stock.changePercent.toFixed(2)}%
                </td>
                <td class="py-3 px-4 text-right font-mono">${(stock.volume / 1000000).toFixed(1)}M</td>
                <td class="py-3 px-4 text-center">
                    <button class="bg-blue-600 hover:bg-blue-700 px-3 py-1 rounded text-sm" 
                            onclick="app.createAlert('${stock.symbol}')">
                        ${this.translations[this.currentLanguage].alertBtn}
                    </button>
                </td>
            </tr>
        `).join('');
    }

    setupCharts() {
        // Market Chart
        const marketChart = echarts.init(document.getElementById('market-chart'));
        const marketOption = {
            backgroundColor: 'transparent',
            textStyle: { color: '#94a3b8' },
            grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
            xAxis: {
                type: 'category',
                data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri'],
                axisLine: { lineStyle: { color: '#475569' } }
            },
            yAxis: {
                type: 'value',
                axisLine: { lineStyle: { color: '#475569' } }
            },
            series: [{
                data: [21450, 21560, 21420, 21680, 21456],
                type: 'line',
                smooth: true,
                lineStyle: { color: '#3b82f6', width: 3 },
                areaStyle: {
                    color: {
                        type: 'linear',
                        x: 0, y: 0, x2: 0, y2: 1,
                        colorStops: [
                            { offset: 0, color: 'rgba(59, 130, 246, 0.3)' },
                            { offset: 1, color: 'rgba(59, 130, 246, 0.1)' }
                        ]
                    }
                }
            }]
        };
        marketChart.setOption(marketOption);
        this.charts.market = marketChart;

        // Sector Chart
        const sectorChart = echarts.init(document.getElementById('sector-chart'));
        const sectorOption = {
            backgroundColor: 'transparent',
            textStyle: { color: '#94a3b8' },
            tooltip: { trigger: 'item' },
            series: [{
                type: 'pie',
                radius: '70%',
                data: [
                    { value: 35, name: 'Technology' },
                    { value: 25, name: 'Finance' },
                    { value: 20, name: 'Healthcare' },
                    { value: 12, name: 'Energy' },
                    { value: 8, name: 'Others' }
                ],
                itemStyle: {
                    color: function (params) {
                        const colors = ['#3b82f6', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6'];
                        return colors[params.dataIndex];
                    }
                }
            }]
        };
        sectorChart.setOption(sectorOption);
        this.charts.sector = sectorChart;
    }

    startRealTimeUpdates() {
        // Update market data every 30 seconds
        setInterval(() => {
            this.updateMarketValues();
        }, 30000);

        // Update charts every 5 minutes
        setInterval(() => {
            this.updateCharts();
        }, 300000);
    }

    updateMarketValues() {
        // Simulate real-time market updates
        const indices = ['nifty', 'sensex', 'banknifty', 'vix'];
        indices.forEach(index => {
            const element = document.getElementById(`${index}-value`);
            if (element) {
                const currentValue = parseFloat(element.textContent.replace(/,/g, ''));
                const change = (Math.random() - 0.5) * currentValue * 0.002; // ±0.2% change
                const newValue = currentValue + change;
                element.textContent = newValue.toLocaleString('en-IN', { maximumFractionDigits: 2 });
            }
        });
    }

    async updateCharts() {
        // Update chart data from ML service
        if (this.charts.market) {
            try {
                const prediction = await this.apiCall('/ml/predict', 'POST', {
                    symbol: 'NIFTY',
                    timeframe: '1d',
                    historical_days: 30
                });

                if (prediction && prediction.predictions) {
                    const dates = prediction.dates || [];
                    const values = prediction.predictions;

                    this.charts.market.setOption({
                        xAxis: {
                            data: dates.map(d => new Date(d).toLocaleDateString())
                        },
                        series: [{
                            data: values,
                            type: 'line',
                            smooth: true
                        }]
                    });
                }
            } catch (error) {
                console.error('Failed to update charts:', error);
            }
        }
    }

    async analyzeSentiment(text) {
        try {
            return await this.apiCall('/ml/sentiment', 'POST', { text });
        } catch (error) {
            console.error('Sentiment analysis failed:', error);
            return null;
        }
    }

    async analyzeRisk(symbol, portfolioValue, positionSize) {
        try {
            return await this.apiCall('/ml/risk', 'POST', {
                symbol,
                portfolio_value: portfolioValue,
                position_size: positionSize
            });
        } catch (error) {
            console.error('Risk analysis failed:', error);
            return null;
        }
    }

    // Authentication methods
    showAuthModal(mode = 'login') {
        const modal = document.getElementById('auth-modal');
        const title = document.getElementById('modal-title');
        const submitBtn = document.getElementById('auth-submit');
        const switchBtn = document.getElementById('auth-switch');
        const switchText = document.getElementById('auth-switch-text');

        const nameField = document.getElementById('name-field');
        const phoneField = document.getElementById('phone-field');
        const planField = document.getElementById('plan-field'); // New plan field

        if (mode === 'login') {
            title.textContent = this.translations[this.currentLanguage].loginTitle;
            submitBtn.textContent = this.translations[this.currentLanguage].loginBtn;
            switchBtn.textContent = this.translations[this.currentLanguage].switchToRegister;
            switchText.textContent = this.translations[this.currentLanguage].noAccount;
            nameField.classList.add('hidden');
            phoneField.classList.add('hidden');
            if (planField) planField.classList.add('hidden');
        } else {
            title.textContent = this.translations[this.currentLanguage].registerTitle;
            submitBtn.textContent = this.translations[this.currentLanguage].registerBtn;
            switchBtn.textContent = this.translations[this.currentLanguage].switchToLogin;
            switchText.textContent = this.translations[this.currentLanguage].hasAccount;
            nameField.classList.remove('hidden');
            phoneField.classList.remove('hidden');
            if (planField) planField.classList.remove('hidden');
        }

        modal.classList.remove('hidden');
        this.authMode = mode;
    }

    hideAuthModal() {
        document.getElementById('auth-modal').classList.add('hidden');
    }

    toggleAuthMode() {
        const newMode = this.authMode === 'login' ? 'register' : 'login';
        this.showAuthModal(newMode);
    }

    async handleAuth(e) {
        e.preventDefault();

        const formData = {
            email: document.getElementById('auth-email').value,
            password: document.getElementById('auth-password').value
        };

        if (this.authMode === 'register') {
            formData.name = document.getElementById('auth-name').value;
            formData.phone = document.getElementById('auth-phone').value;
            formData.plan = document.getElementById('auth-plan').value; // Add plan selection
        }

        try {
            const endpoint = this.authMode === 'login' ? '/auth/signin' : '/auth/signup';
            const response = await this.apiCall(endpoint, 'POST', formData);

            if (response) {
                this.authToken = response.accessToken;
                localStorage.setItem('authToken', this.authToken);
                this.user = response.user;
                this.hideAuthModal();
                this.updateUIAfterAuth();
            }
        } catch (error) {
            console.error('Authentication failed:', error);
            alert('Authentication failed: ' + error.message);
        }
    }

    updateUIAfterAuth() {
        // Update UI to show logged-in state
        const loginBtn = document.getElementById('login-btn');
        const registerBtn = document.getElementById('register-btn');

        if (this.user) {
            loginBtn.textContent = this.user.name;
            registerBtn.textContent = this.translations[this.currentLanguage].logout;
            registerBtn.onclick = () => this.logout();
        }
    }

    logout() {
        this.authToken = null;
        this.user = null;
        localStorage.removeItem('authToken');

        // Reset UI
        document.getElementById('login-btn').textContent = this.translations[this.currentLanguage].loginBtn;
        document.getElementById('register-btn').textContent = this.translations[this.currentLanguage].registerBtn;
        document.getElementById('register-btn').onclick = () => this.showAuthModal('register');
    }

    // API helper methods
    // API helper methods (Delegate to global ApiClient)
    async apiCall(endpoint, method = 'GET', data = null) {
        return window.apiClient.call(method, endpoint, data);
    }

    // Utility methods
    scrollToSection(sectionId) {
        const element = document.getElementById(sectionId);
        if (element) {
            element.scrollIntoView({ behavior: 'smooth' });
        }
    }

    startDemo() {
        // Start a guided demo of the platform
        alert(this.translations[this.currentLanguage].demoStart);
        this.loadMarketData();
    }

    createAlert(symbol) {
        document.getElementById('alert-symbol').value = symbol;
        this.scrollToSection('alerts');
    }

    async handleAlertSubmission(e) {
        e.preventDefault();

        if (!this.authToken) {
            alert(this.translations[this.currentLanguage].pleaseLogin);
            this.showAuthModal('login');
            return;
        }

        const alertData = {
            symbol: document.getElementById('alert-symbol').value,
            targetPrice: parseFloat(document.getElementById('alert-target').value),
            alertType: document.getElementById('alert-type').value,
            expiresAt: document.getElementById('alert-expiry').value,
            message: document.getElementById('alert-message').value
        };

        try {
            const response = await this.apiCall('/alerts/create', 'POST', alertData);
            if (response) {
                alert(this.translations[this.currentLanguage].alertCreated);
                document.getElementById('alert-form').reset();
                this.loadAlerts();
            }
        } catch (error) {
            console.error('Failed to create alert:', error);
            alert(this.translations[this.currentLanguage].alertFailed);
        }
    }

    async loadAlerts() {
        if (!this.authToken) return;

        try {
            const alerts = await this.apiCall('/alerts/my');
            this.displayAlerts(alerts);
        } catch (error) {
            console.error('Failed to load alerts:', error);
        }
    }

    displayAlerts(alerts) {
        const container = document.getElementById('active-alerts');
        if (!container || !alerts) return;

        container.innerHTML = alerts.map(alert => `
            <div class="flex justify-between items-center p-4 bg-slate-800 rounded-lg">
                <div>
                    <div class="font-medium">${alert.symbol}</div>
                    <div class="text-sm text-slate-400">${this.translations[this.currentLanguage].target}: ₹${alert.targetPrice}</div>
                    <div class="text-sm text-slate-400">${this.translations[this.currentLanguage].expiry}: ${new Date(alert.expiresAt).toLocaleDateString()}</div>
                </div>
                <div class="flex space-x-2">
                    <button class="bg-yellow-600 hover:bg-yellow-700 px-3 py-1 rounded text-sm" 
                            onclick="app.toggleAlert('${alert.id}')">
                        ${alert.isActive ? this.translations[this.currentLanguage].off : this.translations[this.currentLanguage].on}
                    </button>
                    <button class="bg-red-600 hover:bg-red-700 px-3 py-1 rounded text-sm" 
                            onclick="app.deleteAlert('${alert.id}')">
                        ${this.translations[this.currentLanguage].delete}
                    </button>
                </div>
            </div>
        `).join('');
    }

    searchStocks(query) {
        // Implement stock search functionality
        console.log('Searching for:', query);
    }

    filterStocks(filter) {
        // Implement stock filtering
        console.log('Filtering by:', filter);

        // Update active filter button
        document.querySelectorAll('.filter-btn').forEach(btn => {
            btn.classList.remove('bg-blue-600');
            btn.classList.add('bg-slate-700');
        });

        document.querySelector(`[data-filter="${filter}"]`).classList.remove('bg-slate-700');
        document.querySelector(`[data-filter="${filter}"]`).classList.add('bg-blue-600');
    }

    filterNews(category) {
        // Implement news filtering
        console.log('Filtering news by:', category);

        // Update active category button
        document.querySelectorAll('.news-category-btn').forEach(btn => {
            btn.classList.remove('bg-blue-600');
            btn.classList.add('bg-slate-700');
        });

        document.querySelector(`[data-category="${category}"]`).classList.remove('bg-slate-700');
        document.querySelector(`[data-category="${category}"]`).classList.add('bg-blue-600');
    }

    switchIPOTab(tab) {
        // Implement IPO tab switching
        console.log('Switching IPO tab to:', tab);

        // Update active tab button
        document.querySelectorAll('.ipo-tab-btn').forEach(btn => {
            btn.classList.remove('bg-blue-600');
            btn.classList.add('bg-slate-700');
        });

        document.querySelector(`[data-tab="${tab}"]`).classList.remove('bg-slate-700');
        document.querySelector(`[data-tab="${tab}"]`).classList.add('bg-blue-600');
    }

    async loadUserData() {
        try {
            const user = await this.apiCall('/auth/me');
            this.user = user;
            this.updateUIAfterAuth();
        } catch (error) {
            console.error('Failed to load user data:', error);
        }
    }
}

// Initialize the application
const app = new PanchmukhiTradingApp();

// Make app globally available for onclick handlers
window.app = app;