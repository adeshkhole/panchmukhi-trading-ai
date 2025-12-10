// Panchmukhi Trading Brain Pro - MongoDB Initialization
// Time-series data and logs collection

db = db.getSiblingDB('panchmukhi_logs');

// Create collections
db.createCollection('user_logs');
db.createCollection('market_logs');
db.createCollection('system_logs');
db.createCollection('trading_signals');
db.createCollection('api_logs');
db.createCollection('performance_metrics');

// Create time-series collections for better performance
db.createCollection('market_data_ts', {
    timeseries: {
        timeField: 'timestamp',
        metaField: 'metadata',
        granularity: 'minutes'
    }
});

db.createCollection('user_activity_ts', {
    timeseries: {
        timeField: 'timestamp',
        metaField: 'metadata',
        granularity: 'minutes'
    }
});

// Create indexes for better performance
db.user_logs.createIndex({ "user_id": 1, "timestamp": -1 });
db.user_logs.createIndex({ "timestamp": -1 });
db.user_logs.createIndex({ "activity_type": 1, "timestamp": -1 });

db.market_logs.createIndex({ "symbol": 1, "timestamp": -1 });
db.market_logs.createIndex({ "timestamp": -1 });
db.market_logs.createIndex({ "log_level": 1, "timestamp": -1 });

db.system_logs.createIndex({ "timestamp": -1 });
db.system_logs.createIndex({ "log_level": 1, "timestamp": -1 });
db.system_logs.createIndex({ "component": 1, "timestamp": -1 });

db.trading_signals.createIndex({ "symbol": 1, "timestamp": -1 });
db.trading_signals.createIndex({ "timestamp": -1 });
db.trading_signals.createIndex({ "signal_type": 1, "timestamp": -1 });

db.api_logs.createIndex({ "timestamp": -1 });
db.api_logs.createIndex({ "endpoint": 1, "timestamp": -1 });
db.api_logs.createIndex({ "user_id": 1, "timestamp": -1 });

db.performance_metrics.createIndex({ "metric_name": 1, "timestamp": -1 });
db.performance_metrics.createIndex({ "timestamp": -1 });

// Insert sample log data
db.user_logs.insertMany([
    {
        user_id: "user123",
        activity_type: "LOGIN",
        description: "User logged in successfully",
        ip_address: "192.168.1.100",
        user_agent: "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
        timestamp: new Date(),
        metadata: {
            location: "Mumbai, India",
            device: "Desktop"
        }
    },
    {
        user_id: "user123",
        activity_type: "ALERT_CREATED",
        description: "Price alert created for RELIANCE at ₹2500",
        ip_address: "192.168.1.100",
        timestamp: new Date(),
        metadata: {
            symbol: "RELIANCE",
            target_price: 2500,
            alert_type: "PRICE_ABOVE"
        }
    },
    {
        user_id: "user456",
        activity_type: "TRADE_EXECUTED",
        description: "Buy order executed for TCS",
        ip_address: "192.168.1.101",
        timestamp: new Date(),
        metadata: {
            symbol: "TCS",
            quantity: 100,
            price: 3421.50,
            order_type: "MARKET"
        }
    }
]);

db.market_logs.insertMany([
    {
        symbol: "RELIANCE",
        log_level: "INFO",
        message: "Price updated successfully",
        old_price: 2450.60,
        new_price: 2456.80,
        change_percent: 0.25,
        timestamp: new Date(),
        metadata: {
            volume: 12500000,
            high: 2467.90,
            low: 2405.20
        }
    },
    {
        symbol: "TCS",
        log_level: "INFO",
        message: "Price updated successfully",
        old_price: 3410.40,
        new_price: 3421.50,
        change_percent: 0.33,
        timestamp: new Date(),
        metadata: {
            volume: 8900000,
            high: 3434.20,
            low: 3376.80
        }
    }
]);

db.system_logs.insertMany([
    {
        log_level: "INFO",
        component: "WEBSOCKET",
        message: "WebSocket server started successfully",
        timestamp: new Date(),
        metadata: {
            port: 8083,
            clients_connected: 0
        }
    },
    {
        log_level: "WARN",
        component: "DATABASE",
        message: "Database connection pool reaching maximum capacity",
        timestamp: new Date(),
        metadata: {
            active_connections: 18,
            max_connections: 20
        }
    },
    {
        log_level: "ERROR",
        component: "API",
        message: "Failed to fetch market data from external API",
        timestamp: new Date(),
        metadata: {
            endpoint: "/api/market/data",
            error_code: "TIMEOUT",
            retry_count: 3
        }
    }
]);

db.trading_signals.insertMany([
    {
        signal_id: "SIG_001",
        symbol: "RELIANCE",
        signal_type: "BUY",
        confidence_score: 0.85,
        target_price: 2500.00,
        stop_loss: 2400.00,
        rationale: "Strong fundamentals and positive market sentiment",
        technical_indicators: {
            rsi: 65.2,
            macd: "BULLISH",
            moving_average: "ABOVE_50_SMA"
        },
        timestamp: new Date(),
        metadata: {
            ml_model: "LSTM_v2.1",
            data_sources: ["market_data", "news_sentiment", "social_media"],
            risk_level: "MEDIUM"
        }
    },
    {
        signal_id: "SIG_002",
        symbol: "TCS",
        signal_type: "HOLD",
        confidence_score: 0.72,
        target_price: 3450.00,
        stop_loss: 3350.00,
        rationale: "Consolidation phase, wait for breakout",
        technical_indicators: {
            rsi: 58.4,
            macd: "NEUTRAL",
            moving_average: "NEAR_50_SMA"
        },
        timestamp: new Date(),
        metadata: {
            ml_model: "LSTM_v2.1",
            data_sources: ["market_data", "news_sentiment"],
            risk_level: "LOW"
        }
    }
]);

db.api_logs.insertMany([
    {
        endpoint: "/api/auth/login",
        method: "POST",
        user_id: "user123",
        ip_address: "192.168.1.100",
        request_size: 256,
        response_size: 1024,
        status_code: 200,
        response_time_ms: 145,
        timestamp: new Date(),
        metadata: {
            user_agent: "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"
        }
    },
    {
        endpoint: "/api/market/data",
        method: "GET",
        user_id: "user123",
        ip_address: "192.168.1.100",
        request_size: 128,
        response_size: 5120,
        status_code: 200,
        response_time_ms: 89,
        timestamp: new Date(),
        metadata: {
            symbols_requested: ["RELIANCE", "TCS", "HDFC"]
        }
    }
]);

db.performance_metrics.insertMany([
    {
        metric_name: "api_response_time",
        value: 145.67,
        unit: "milliseconds",
        timestamp: new Date(),
        metadata: {
            endpoint: "/api/auth/login",
            percentile: 95
        }
    },
    {
        metric_name: "websocket_connections",
        value: 234,
        unit: "count",
        timestamp: new Date(),
        metadata: {
            peak_connections: 250,
            average_connections: 180
        }
    },
    {
        metric_name: "database_query_time",
        value: 12.34,
        unit: "milliseconds",
        timestamp: new Date(),
        metadata: {
            query_type: "SELECT",
            table: "market_data",
            rows_returned: 50
        }
    },
    {
        metric_name: "memory_usage",
        value: 68.5,
        unit: "percentage",
        timestamp: new Date(),
        metadata: {
            total_memory_gb: 16,
            used_memory_gb: 10.96,
            free_memory_gb: 5.04
        }
    },
    {
        metric_name: "cpu_usage",
        value: 45.2,
        unit: "percentage",
        timestamp: new Date(),
        metadata: {
            cores: 8,
            load_average: 3.6
        }
    }
]);

// Create views for analytics
db.createView("daily_user_activity", "user_logs", [
    {
        $group: {
            _id: {
                user_id: "$user_id",
                date: { $dateToString: { format: "%Y-%m-%d", date: "$timestamp" } }
            },
            activity_count: { $sum: 1 },
            activities: { $push: "$activity_type" }
        }
    },
    { $sort: { "_id.date": -1, "_id.user_id": 1 } }
]);

db.createView("hourly_market_data", "market_logs", [
    {
        $group: {
            _id: {
                symbol: "$symbol",
                hour: { $dateToString: { format: "%Y-%m-%d %H:00:00", date: "$timestamp" } }
            },
            avg_change_percent: { $avg: "$change_percent" },
            update_count: { $sum: 1 },
            last_price: { $last: "$new_price" }
        }
    },
    { $sort: { "_id.hour": -1, "_id.symbol": 1 } }
]);

// Create capped collections for high-volume data
db.createCollection("market_data_stream", { capped: true, size: 100000000, max: 10000 });
db.createCollection("user_events_stream", { capped: true, size: 50000000, max: 5000 });

print("MongoDB initialization completed successfully!");