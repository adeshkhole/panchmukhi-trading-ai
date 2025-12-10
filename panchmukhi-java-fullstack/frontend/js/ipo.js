document.addEventListener('DOMContentLoaded', async () => {
    // Ensure ApiClient is available
    if (!window.apiClient) {
        console.error('ApiClient not initialized');
        return;
    }

    const ipoGrid = document.querySelector('.ipo-grid');
    const filterSelect = document.getElementById('filterSelect');

    // Initial load
    await loadIPOs('all');

    // Filter change handler
    if (filterSelect) {
        filterSelect.addEventListener('change', (e) => {
            loadIPOs(e.target.value);
        });
    }

    async function loadIPOs(status) {
        if (!ipoGrid) return;

        ipoGrid.innerHTML = '<div class="col-span-full text-center py-8"><div class="animate-spin h-8 w-8 border-4 border-orange-500 rounded-full border-t-transparent mx-auto"></div><p class="mt-2 text-gray-400">Loading IPOs...</p></div>';

        try {
            const ipos = await window.apiClient.getIPOs(status);
            renderIPOs(ipos);
        } catch (error) {
            console.error('Failed to load IPOs:', error);
            // Fallback to static content or show error if strictly backend driven
            // For now, if error (e.g. backend not ready), we might want to keep the static HTML. 
            // But since I am overwriting the grid, I should handle this gracefully.
            ipoGrid.innerHTML = `
                <div class="col-span-full text-center py-8 text-red-400">
                    <p>Failed to load IPO data.</p>
                    <button onclick="location.reload()" class="mt-4 bg-gray-800 px-4 py-2 rounded">Retry</button>
                    <!-- Fallback demo data for visualization if backend is down -->
                </div>
            `;

            // OPTIONAL: Load demo data if backend fails, to impress user as requested "make it work"
            // renderIPOs(getDemoIPOData());
        }
    }

    function renderIPOs(ipos) {
        if (!ipos || ipos.length === 0) {
            ipoGrid.innerHTML = '<div class="col-span-full text-center py-8 text-gray-400">No IPOs found for this category.</div>';
            return;
        }

        ipoGrid.innerHTML = ipos.map(ipo => createIPOCard(ipo)).join('');
    }

    function createIPOCard(ipo) {
        // Safe defaults
        const name = ipo.name || 'Unknown IPO';
        const symbol = ipo.symbol || 'N/A';
        const priceRange = ipo.priceBand || '₹0 - ₹0';
        const lotSize = ipo.lotSize || '0';
        const issueSize = ipo.issueSize || '0 Cr';
        const closeDate = ipo.closeDate || 'TBD';
        const status = ipo.status || 'Upcoming';
        const subStatus = ipo.subscription || 0;
        const gmp = ipo.gmp || 0;
        const gmpPercent = ipo.gmpPercent || 0;

        let statusColor = 'blue';
        if (status === 'Open') statusColor = 'green';
        if (status === 'Closed') statusColor = 'red';

        return `
            <div class="ipo-card glass-card p-6 fade-in visible">
                <div class="flex items-center justify-between mb-4">
                    <div class="flex items-center space-x-3">
                        <div class="w-12 h-12 bg-${statusColor}-500 rounded-lg flex items-center justify-center text-xl">
                            🏢
                        </div>
                        <div>
                            <h3 class="text-xl font-bold marathi-text">${name}</h3>
                            <p class="text-sm text-gray-400">${symbol}</p>
                        </div>
                    </div>
                    <div class="status-${status.toLowerCase()} px-3 py-1 rounded-full text-sm font-medium bg-${statusColor}-500/20 text-${statusColor}-400 border border-${statusColor}-500/30">
                        ${status}
                    </div>
                </div>
                
                <div class="grid grid-cols-2 gap-4 mb-4">
                    <div>
                        <div class="text-sm text-gray-400">Price Range</div>
                        <div class="font-semibold mono-text">${priceRange}</div>
                    </div>
                    <div>
                        <div class="text-sm text-gray-400">Minimum Lot</div>
                        <div class="font-semibold">${lotSize} Shares</div>
                    </div>
                    <div>
                        <div class="text-sm text-gray-400">Issue Size</div>
                        <div class="font-semibold mono-text">${issueSize}</div>
                    </div>
                    <div>
                        <div class="text-sm text-gray-400">Closing Date</div>
                        <div class="font-semibold">${closeDate}</div>
                    </div>
                </div>
                
                <div class="mb-4">
                    <div class="flex justify-between text-sm mb-2">
                        <span>Subscription Status</span>
                        <span class="font-semibold">${subStatus}x</span>
                    </div>
                    <div class="subscription-bar w-full bg-gray-700 h-2 rounded-full overflow-hidden">
                        <div class="subscription-fill bg-gradient-to-r from-green-400 to-green-600 h-full" style="width: ${Math.min(subStatus * 10, 100)}%"></div>
                    </div>
                </div>
                
                <div class="flex justify-between items-center">
                    <div class="text-sm">
                        <span class="text-gray-400">GMP: </span>
                        <span class="${gmp > 0 ? 'text-green-400' : 'text-red-400'} font-semibold">₹${gmp} (${gmpPercent}%)</span>
                    </div>
                    <button class="bg-orange-500 hover:bg-orange-600 px-4 py-2 rounded-lg text-sm font-medium transition-colors">
                        View Details
                    </button>
                </div>
            </div>
        `;
    }
});
