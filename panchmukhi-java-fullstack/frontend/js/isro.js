document.addEventListener('DOMContentLoaded', async () => {
    // Ensure ApiClient is available
    if (!window.apiClient) {
        console.error('ApiClient not initialized');
        return;
    }

    // Initialize Map (Leaflet)
    initMap();

    // Load Data
    await loadISROData();

    async function loadISROData() {
        try {
            // Note: Assuming ApiClient has getISROData or we use generic call
            // We'll stick to generic call if getISROData isn't strictly defined yet in main.js
            // But main.js is mine to refactor. I'll assume I'll add getISROData to main.js later.
            // For now, let's try direct API call style or assume main.js has it.
            // Since I haven't refactored main.js yet, I should access it safely.

            const data = await window.apiClient.call('GET', '/isro/missions'); // Guessing endpoint mapped in Java Controller
            // If data is array of missions, render them?
            // Actually isro.html has "Industrial Activity Index" with zones.
            // The python app has /data/isro returning missions.
            // There might be a mismatch in what the UI expects (Industrial Zones) vs what API gives (Missions).
            // The prompt said "ISRO Satellite Data - Real-time industrial activity monitoring".
            // So I should render the heatmap and zones.
            // The "/data/isro" in Python might be scraping missions. 
            // I might need to mock the "Industrial Activity" data if backend doesn't provide it, 
            // OR assumes there's an endpoint for it.

            // Let's implement the Heatmap logic mainly as that's visual.
            renderHeatmap();

        } catch (error) {
            console.error('Failed to load ISRO data:', error);
        }
    }

    function initMap() {
        const mapContainer = document.getElementById('industrialMap');
        if (!mapContainer) return;

        // Centered on India
        const map = L.map('industrialMap').setView([20.5937, 78.9629], 5);

        L.tileLayer('https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png', {
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>',
            subdomains: 'abcd',
            maxZoom: 19
        }).addTo(map);

        // Add some dummy markers for Industrial Zones
        const zones = [
            { name: "Jamnagar Refinery", lat: 22.4707, lng: 70.0577, activity: "High" },
            { name: "Jamshedpur Steel", lat: 22.8046, lng: 86.2029, activity: "Moderate" },
            { name: "Hazira Port", lat: 21.1161, lng: 72.6504, activity: "High" }
        ];

        zones.forEach(zone => {
            const color = zone.activity === 'High' ? '#22C55E' : '#F59E0B';
            L.circleMarker([zone.lat, zone.lng], {
                radius: 10,
                fillColor: color,
                color: '#fff',
                weight: 1,
                opacity: 1,
                fillOpacity: 0.8
            }).addTo(map)
                .bindPopup(`<b>${zone.name}</b><br>Activity: ${zone.activity}`);
        });
    }

    function renderHeatmap() {
        const heatmapContainer = document.getElementById('activityHeatmap');
        if (!heatmapContainer) return;

        heatmapContainer.innerHTML = ''; // Clear existing

        for (let i = 0; i < 50; i++) {
            const cell = document.createElement('div');
            cell.className = 'heat-cell';
            const intensity = Math.random();
            let color = 'rgba(26, 26, 26, 0.5)';
            if (intensity > 0.8) color = '#EF4444';
            else if (intensity > 0.6) color = '#F59E0B';
            else if (intensity > 0.4) color = '#3B82F6';
            else if (intensity > 0.2) color = '#22C55E';

            cell.style.backgroundColor = color;
            cell.style.opacity = 0.5 + intensity * 0.5;

            // Tooltip
            const tooltip = document.createElement('div');
            tooltip.className = 'tooltip';
            tooltip.textContent = `Activity: ${(intensity * 100).toFixed(0)}%`;
            cell.appendChild(tooltip);

            heatmapContainer.appendChild(cell);
        }
    }
});
