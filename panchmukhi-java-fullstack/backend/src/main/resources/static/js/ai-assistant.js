// AI Assistant Logic
class AiAssistant {
    constructor() {
        this.isOpen = false;
        this.messages = [];
        this.init();
    }

    init() {
        this.injectStyles();
        this.renderLauncher();
    }

    injectStyles() {
        const style = document.createElement('style');
        style.textContent = `
            .ai-chat-widget {
                position: fixed;
                bottom: 24px;
                right: 24px;
                z-index: 90;
                font-family: 'Inter', sans-serif;
            }
            .chat-window {
                position: absolute;
                bottom: 80px;
                right: 0;
                width: 350px;
                height: 500px;
                background: rgba(20, 20, 20, 0.95);
                backdrop-filter: blur(20px);
                border: 1px solid rgba(255, 107, 53, 0.3);
                border-radius: 16px;
                display: flex;
                flex-direction: column;
                box-shadow: 0 20px 50px rgba(0,0,0,0.5);
                transform-origin: bottom right;
                transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
                opacity: 0;
                transform: scale(0.9);
                pointer-events: none;
            }
            .chat-window.open {
                opacity: 1;
                transform: scale(1);
                pointer-events: all;
            }
            .chat-header {
                padding: 16px;
                border-bottom: 1px solid rgba(255, 255, 255, 0.1);
                background: linear-gradient(90deg, #1a1a1a, #2a2a2a);
                border-radius: 16px 16px 0 0;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            .chat-messages {
                flex: 1;
                overflow-y: auto;
                padding: 16px;
                display: flex;
                flex-direction: column;
                gap: 12px;
            }
            .chat-input-area {
                padding: 16px;
                border-top: 1px solid rgba(255, 255, 255, 0.1);
            }
            .message {
                max-width: 80%;
                padding: 10px 14px;
                border-radius: 12px;
                font-size: 0.9rem;
                line-height: 1.4;
            }
            .message.user {
                align-self: flex-end;
                background: linear-gradient(135deg, #FF6B35, #FF8C42);
                color: white;
                border-bottom-right-radius: 4px;
            }
            .message.ai {
                align-self: flex-start;
                background: #2a2a2a;
                color: #e0e0e0;
                border-bottom-left-radius: 4px;
                border: 1px solid rgba(255, 255, 255, 0.1);
            }
            .typing-indicator span {
                display: inline-block;
                width: 6px;
                height: 6px;
                background: #aaa;
                border-radius: 50%;
                animation: typing 1s infinite;
                margin: 0 2px;
            }
            .typing-indicator span:nth-child(2) { animation-delay: 0.2s; }
            .typing-indicator span:nth-child(3) { animation-delay: 0.4s; }
            @keyframes typing {
                0%, 100% { transform: translateY(0); }
                50% { transform: translateY(-4px); }
            }
        `;
        document.head.appendChild(style);
    }

    renderLauncher() {
        const widget = document.createElement('div');
        widget.className = 'ai-chat-widget';
        widget.innerHTML = `
            <div id="aiChatWindow" class="chat-window">
                <div class="chat-header">
                    <div class="flex items-center gap-3">
                        <div class="w-8 h-8 rounded-full bg-gradient-to-r from-blue-500 to-purple-600 flex items-center justify-center">
                            🤖
                        </div>
                        <div>
                            <h3 class="font-bold text-white text-sm">Panchmukhi AI</h3>
                            <p class="text-xs text-green-400">● Online</p>
                        </div>
                    </div>
                    <button id="closeChat" class="text-gray-400 hover:text-white">
                        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
                    </button>
                </div>
                <div id="messagesContainer" class="chat-messages">
                    <div class="message ai">
                        Hello! I am your AI trading assistant. Ask me about market trends, specific stocks, or ISRO data.
                    </div>
                </div>
                <div class="chat-input-area">
                    <form id="chatForm" class="flex gap-2">
                        <input type="text" id="chatInput" placeholder="Ask anything..." class="flex-1 bg-gray-800 border border-gray-700 rounded-lg px-4 py-2 text-sm text-white focus:outline-none focus:border-orange-500 transition-colors">
                        <button type="submit" class="bg-orange-600 hover:bg-orange-700 text-white p-2 rounded-lg transition-colors">
                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8"></path></svg>
                        </button>
                    </form>
                </div>
            </div>
            <button id="chatLauncher" class="w-14 h-14 bg-gradient-to-r from-orange-500 to-red-600 rounded-full shadow-lg flex items-center justify-center text-white transform hover:scale-110 transition-all duration-300 group">
                <span class="absolute -top-1 -right-1 w-4 h-4 bg-red-500 rounded-full border-2 border-gray-900 bloack animate-pulse"></span>
                <svg class="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z"></path></svg>
            </button>
        `;
        document.body.appendChild(widget);

        // Event Listeners
        document.getElementById('chatLauncher').addEventListener('click', () => this.toggleChat());
        document.getElementById('closeChat').addEventListener('click', () => this.toggleChat());
        document.getElementById('chatForm').addEventListener('submit', (e) => this.handleSubmit(e));
    }

    toggleChat() {
        this.isOpen = !this.isOpen;
        const window = document.getElementById('aiChatWindow');
        if (this.isOpen) {
            window.classList.add('open');
            document.getElementById('chatInput').focus();
        } else {
            window.classList.remove('open');
        }
    }

    async handleSubmit(e) {
        e.preventDefault();
        const input = document.getElementById('chatInput');
        const text = input.value.trim();
        if (!text) return;

        // User Message
        this.addMessage(text, 'user');
        input.value = '';

        // Typing Indicator
        const typingId = this.addTypingIndicator();

        // Simulate AI Response (Mock for now, can connect to backend later)
        setTimeout(() => {
            this.removeMessage(typingId);
            this.generateResponse(text);
        }, 1500);
    }

    addMessage(text, type, id = null) {
        const container = document.getElementById('messagesContainer');
        const div = document.createElement('div');
        div.className = `message ${type}`;
        if (id) div.id = id;
        div.textContent = text;
        container.appendChild(div);
        container.scrollTop = container.scrollHeight;
    }

    addTypingIndicator() {
        const id = 'typing-' + Date.now();
        const container = document.getElementById('messagesContainer');
        const div = document.createElement('div');
        div.className = 'message ai typing-indicator';
        div.id = id;
        div.innerHTML = '<span></span><span></span><span></span>';
        container.appendChild(div);
        container.scrollTop = container.scrollHeight;
        return id;
    }

    removeMessage(id) {
        const el = document.getElementById(id);
        if (el) el.remove();
    }

    generateResponse(query) {
        const lowerQ = query.toLowerCase();
        let response = "I'm analyzing the market data for you...";

        if (lowerQ.includes('reliance')) {
            response = "Reliance is currently BULLISH based on 5-way analysis. ISRO data shows high activity at Jamnagar, and News sentiment is positive.";
        } else if (lowerQ.includes('nifty')) {
            response = "NIFTY 50 is facing resistance at 19,850. Option chain suggests strong call writing at 19,900. Proceed with caution.";
        } else if (lowerQ.includes('buy') || lowerQ.includes('sell')) {
            response = "Based on current Fusion Score (0.80), the overall market signal is BUY, heavily driven by ISRO satellite data indicating industrial growth.";
        } else {
            response = "I can track mainly NIFTY 50 stocks. Try asking about 'Reliance', 'TCS', or 'Market Trend'.";
        }

        this.addMessage(response, 'ai');
    }
}

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    window.aiAssistant = new AiAssistant();
});
