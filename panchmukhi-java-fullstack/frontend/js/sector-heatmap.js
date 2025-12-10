// Sector Heatmap Visualization using ECharts

class SectorHeatmap {
    constructor(containerId) {
        this.containerId = containerId;
        this.chart = null;
        this.sectors = [];
    }

    async init() {
        await this.loadSectorData();
        this.render();

        // Auto-refresh every 5 minutes
        setInterval(() => {
            this.loadSectorData().then(() => this.updateChart());
        }, 300000);
    }

    async loadSectorData() {
        try {
            this.sectors = await window.apiClient.get('/sector-intelligence/public/all');
        } catch (error) {
            console.error('Failed to load sector data:', error);
            this.sectors = [];
        }
    }

    render() {
        const container = document.getElementById(this.containerId);
        if (!container) return;

        // Initialize ECharts
        this.chart = echarts.init(container);
        this.updateChart();

        // Resize on window resize
        window.addEventListener('resize', () => {
            this.chart.resize();
        });
    }

    updateChart() {
        if (!this.chart || this.sectors.length === 0) return;

        const data = this.sectors.map(sector => ({
            name: sector.sectorName,
            value: Math.abs(parseFloat(sector.sentimentScore || 0)) * 100,
            sentiment: parseFloat(sector.sentimentScore || 0),
            trend: sector.currentTrend,
            newsCount: sector.newsCount
        }));

        const option = {
            title: {
                text: 'Sector Sentiment Heatmap',
                left: 'center',
                textStyle: {
                    color: '#fff',
                    fontSize: 18
                }
            },
            tooltip: {
                formatter: function (params) {
                    const sector = params.data;
                    const sentimentLabel = sector.sentiment > 0.3 ? 'Bullish 📈' :
                        sector.sentiment < -0.3 ? 'Bearish 📉' : 'Neutral ⚖️';
                    return `
                        <div style="padding: 10px;">
                            <strong>${sector.name}</strong><br>
                            Sentiment: ${sentimentLabel}<br>
                            Score: ${sector.sentiment.toFixed(2)}<br>
                            Trend: ${sector.trend || 'N/A'}<br>
                            News: ${sector.newsCount || 0} articles
                        </div>
                    `;
                }
            },
            series: [{
                type: 'treemap',
                width: '100%',
                height: '100%',
                roam: false,
                nodeClick: false,
                data: data,
                breadcrumb: { show: false },
                label: {
                    show: true,
                    formatter: '{b}\n{@sentiment}',
                    fontSize: 14,
                    color: '#fff'
                },
                itemStyle: {
                    borderColor: '#1a1a2e',
                    borderWidth: 2,
                    gapWidth: 2
                },
                levels: [
                    {
                        itemStyle: {
                            borderWidth: 0,
                            gapWidth: 5
                        }
                    },
                    {
                        itemStyle: {
                            gapWidth: 1,
                            borderColorSaturation: 0.6
                        },
                        colorMappingBy: 'value',
                        color: data.map(d => {
                            if (d.sentiment > 0.3) return '#10b981'; // Bullish green
                            if (d.sentiment < -0.3) return '#ef4444'; // Bearish red
                            return '#6b7280'; // Neutral gray
                        })
                    }
                ]
            }]
        };

        this.chart.setOption(option);
    }

    destroy() {
        if (this.chart) {
            this.chart.dispose();
        }
    }
}

// Initialize heatmap
let sectorHeatmap;
document.addEventListener('DOMContentLoaded', () => {
    sectorHeatmap = new SectorHeatmap('sector-heatmap');
    sectorHeatmap.init();
});
