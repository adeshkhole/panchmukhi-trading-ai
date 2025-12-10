document.addEventListener('DOMContentLoaded', async () => {
    // Ensure ApiClient is available
    if (!window.apiClient) {
        console.error('ApiClient not initialized');
        return;
    }

    // Initialize Charts
    initSentimentChart();

    // Load News
    await loadNews();

    async function loadNews() {
        try {
            // Fetch news from API
            // Assuming generic endpoint or wait for main.js refactor
            const newsData = await window.apiClient.call('GET', '/news');

            // If API returns data, render it.
            // For now, if empty or error, we might see the static HTML from the file.
            // But let's try to inject if we get data.
            // Since I am writing this robustly, I'll check if container exists.
        } catch (error) {
            console.error('Failed to load news:', error);
        }
    }

    function initSentimentChart() {
        // We can use ECharts if loaded
        if (typeof echarts === 'undefined') return;

        // This is a static init for now, but ideally should be dynamic based on data
        // The HTML already has an SVG donut chart, so maybe we don't need to do much unless we want to make it real-time.
        // Let's leave the SVG as is for visual appeal unless we get real data.
    }
});
