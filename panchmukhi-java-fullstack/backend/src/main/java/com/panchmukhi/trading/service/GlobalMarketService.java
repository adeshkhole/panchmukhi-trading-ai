package com.panchmukhi.trading.service;

import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class GlobalMarketService {

    private static final List<GlobalMarket> MARKETS = Arrays.asList(
            new GlobalMarket("NSE", "India (NSE)", "Asia/Kolkata", "09:00", "09:15", "15:30",
                    "\uD83C\uDDEE\uD83C\uDDF3"),
            new GlobalMarket("NYSE", "USA (NYSE)", "America/New_York", "04:00", "09:30", "16:00",
                    "\uD83C\uDDFA\uD83C\uDDF8"),
            new GlobalMarket("LSE", "UK (LSE)", "Europe/London", null, "08:00", "16:30", "\uD83C\uDDEC\uD83C\uDDE7"),
            new GlobalMarket("TSE", "Japan (TSE)", "Asia/Tokyo", null, "09:00", "15:00", "\uD83C\uDDEF\uD83C\uDDF5"),
            new GlobalMarket("HKEX", "Hong Kong (HKEX)", "Asia/Hong_Kong", null, "09:30", "16:00",
                    "\uD83C\uDDED\uD83C\uDDF0"),
            new GlobalMarket("XETRA", "Germany (XETRA)", "Europe/Berlin", null, "09:00", "17:30",
                    "\uD83C\uDDE9\uD83C\uDDEA"));

    public List<GlobalMarket> getAllMarkets() {
        List<GlobalMarket> marketsWithStatus = new ArrayList<>();
        for (GlobalMarket market : MARKETS) {
            marketsWithStatus.add(enrichMarketWithStatus(market));
        }
        return marketsWithStatus;
    }

    public GlobalMarket getMarketByCode(String marketCode) {
        return MARKETS.stream()
                .filter(m -> m.getCode().equals(marketCode))
                .findFirst()
                .map(this::enrichMarketWithStatus)
                .orElse(null);
    }

    public Map<String, Object> getNextOpening() {
        LocalDateTime now = LocalDateTime.now();

        GlobalMarket nextMarket = null;
        Duration minDuration = null;

        for (GlobalMarket market : MARKETS) {
            Duration duration = getTimeUntilOpen(market);
            if (duration != null && duration.toMillis() > 0) {
                if (minDuration == null || duration.compareTo(minDuration) < 0) {
                    minDuration = duration;
                    nextMarket = market;
                }
            }
        }

        if (nextMarket != null) {
            return Map.of(
                    "market", nextMarket,
                    "timeUntilOpen", formatDuration(minDuration));
        }

        return Map.of("message", "All markets closed");
    }

    private GlobalMarket enrichMarketWithStatus(GlobalMarket market) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(market.getTimezone()));
        LocalTime currentTime = now.toLocalTime();

        LocalTime openTime = LocalTime.parse(market.getOpenTime());
        LocalTime closeTime = LocalTime.parse(market.getCloseTime());

        String status;
        if (currentTime.isAfter(openTime) && currentTime.isBefore(closeTime)) {
            status = "OPEN";
        } else if (market.getPreMarketTime() != null) {
            LocalTime preMarketTime = LocalTime.parse(market.getPreMarketTime());
            if (currentTime.isAfter(preMarketTime) && currentTime.isBefore(openTime)) {
                status = "PRE-MARKET";
            } else {
                status = "CLOSED";
            }
        } else {
            status = "CLOSED";
        }

        market.setCurrentStatus(status);
        market.setLocalTime(currentTime.toString());

        return market;
    }

    private Duration getTimeUntilOpen(GlobalMarket market) {
        try {
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of(market.getTimezone()));
            LocalTime openTime = LocalTime.parse(market.getOpenTime());

            ZonedDateTime nextOpen = now.with(openTime);
            if (nextOpen.isBefore(now)) {
                nextOpen = nextOpen.plusDays(1);
            }

            return Duration.between(now, nextOpen);
        } catch (Exception e) {
            return null;
        }
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;

        if (hours > 0) {
            return hours + "h " + minutes + "m";
        }
        return minutes + "m";
    }

    // Inner class for Market data
    public static class GlobalMarket {
        private String code;
        private String name;
        private String timezone;
        private String preMarketTime;
        private String openTime;
        private String closeTime;
        private String flag;
        private String currentStatus;
        private String localTime;

        public GlobalMarket(String code, String name, String timezone, String preMarketTime, String openTime,
                String closeTime, String flag) {
            this.code = code;
            this.name = name;
            this.timezone = timezone;
            this.preMarketTime = preMarketTime;
            this.openTime = openTime;
            this.closeTime = closeTime;
            this.flag = flag;
        }

        // Getters and Setters
        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getTimezone() {
            return timezone;
        }

        public String getPreMarketTime() {
            return preMarketTime;
        }

        public String getOpenTime() {
            return openTime;
        }

        public String getCloseTime() {
            return closeTime;
        }

        public String getFlag() {
            return flag;
        }

        public String getCurrentStatus() {
            return currentStatus;
        }

        public void setCurrentStatus(String status) {
            this.currentStatus = status;
        }

        public String getLocalTime() {
            return localTime;
        }

        public void setLocalTime(String time) {
            this.localTime = time;
        }
    }
}
