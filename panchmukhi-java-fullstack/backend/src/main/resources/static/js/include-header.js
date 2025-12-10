document.addEventListener('DOMContentLoaded', () => {
    const headerContainer = document.getElementById('site-header');
    if (headerContainer) {
        headerContainer.innerHTML = `
        <nav class="fixed w-full z-50 nav-blur border-b border-gray-800">
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div class="flex items-center justify-between h-16">
                    <div class="flex items-center">
                        <div class="flex-shrink-0 cursor-pointer" onclick="window.location.href='index.html'">
                            <div class="flex items-center space-x-2">
                                <div class="w-8 h-8 bg-gradient-to-r from-orange-500 to-yellow-500 rounded-lg flex items-center justify-center">
                                    <span class="text-white font-bold text-lg">पं</span>
                                </div>
                                <span class="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-orange-500 to-yellow-500 marathi-text hidden md:block">
                                    Panchmukhi Trading
                                </span>
                            </div>
                        </div>
                        <div class="hidden md:block">
                            <div class="ml-10 flex items-baseline space-x-4">
                                <a href="index.html" class="text-gray-300 hover:text-white hover:bg-gray-700 px-3 py-2 rounded-md text-sm font-medium transition-all nav-link" data-lang-key="dashboard">Dashboard</a>
                                <a href="isro.html" class="text-gray-300 hover:text-white hover:bg-gray-700 px-3 py-2 rounded-md text-sm font-medium transition-all nav-link" data-lang-key="satellite">Satellite</a>
                                <a href="news.html" class="text-gray-300 hover:text-white hover:bg-gray-700 px-3 py-2 rounded-md text-sm font-medium transition-all nav-link" data-lang-key="news">News</a>
                                <a href="options.html" class="text-gray-300 hover:text-white hover:bg-gray-700 px-3 py-2 rounded-md text-sm font-medium transition-all nav-link" data-lang-key="options">Options</a>
                                <a href="ipo.html" class="text-gray-300 hover:text-white hover:bg-gray-700 px-3 py-2 rounded-md text-sm font-medium transition-all nav-link" data-lang-key="ipo">IPO</a>
                            </div>
                        </div>
                    </div>
                    <div class="flex items-center space-x-4">
                        <div class="language-selector relative">
                            <select id="languageSelect" class="bg-gray-800 text-white text-sm rounded-lg focus:ring-orange-500 focus:border-orange-500 block w-full p-2 border border-gray-700">
                                <option value="mr">मराठी</option>
                                <option value="hi">हिंदी</option>
                                <option value="en" selected>English</option>
                                <option value="gu">ગુજરાતી</option>
                            </select>
                        </div>
                        <div class="relative">
                            <button class="text-gray-300 hover:text-white p-2 rounded-full hover:bg-gray-800 transition-colors">
                                <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" />
                                </svg>
                                <span class="notification-badge">3</span>
                            </button>
                        </div>
                        <div id="auth-section">
                            <a href="login.html" class="bg-orange-600 hover:bg-orange-700 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors">Login</a>
                        </div>
                    </div>
                </div>
            </div>
        </nav>
        `;
    }
    const authSection = document.getElementById('auth-section');
    const userJson = localStorage.getItem('user');

    if (userJson && authSection) {
        try {
            const user = JSON.parse(userJson);
            // Show Profile Dropdown
            authSection.innerHTML = `
                <div class="relative group">
                    <button class="flex items-center space-x-2 text-gray-300 hover:text-white focus:outline-none">
                        <div class="w-8 h-8 rounded-full bg-gradient-to-r from-orange-500 to-red-500 flex items-center justify-center text-white font-bold">
                            ${user.name ? user.name.charAt(0).toUpperCase() : 'U'}
                        </div>
                        <span class="hidden md:block font-medium">${user.name || 'User'}</span>
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path></svg>
                    </button>
                    <!-- Dropdown Menu -->
                    <div class="absolute right-0 mt-2 w-48 bg-gray-800 rounded-lg shadow-xl border border-gray-700 hidden group-hover:block py-2 z-50">
                        <div class="px-4 py-2 border-b border-gray-700">
                            <p class="text-xs text-gray-400">Signed in as</p>
                            <p class="text-sm font-bold text-white truncate">${user.email}</p>
                        </div>
                        <a href="#" class="block px-4 py-2 text-sm text-gray-300 hover:bg-gray-700 hover:text-white">my Profile</a>
                        <a href="#" class="block px-4 py-2 text-sm text-gray-300 hover:bg-gray-700 hover:text-white">Settings</a>
                        <div class="border-t border-gray-700 my-1"></div>
                        <a href="#" onclick="logout()" class="block px-4 py-2 text-sm text-red-400 hover:bg-gray-700 hover:text-red-300">Sign out</a>
                    </div>
                </div>
            `;
        } catch (e) {
            console.error('Error parsing user data:', e);
        }
    }
});

// Global Logout needed for the header
window.logout = function () {
    localStorage.removeItem('authToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
    window.location.href = 'login.html';
};
