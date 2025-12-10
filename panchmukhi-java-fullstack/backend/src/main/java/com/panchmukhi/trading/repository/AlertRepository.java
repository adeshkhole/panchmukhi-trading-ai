package com.panchmukhi.trading.repository;

import com.panchmukhi.trading.model.Alert;
import com.panchmukhi.trading.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, String> {
    
    List<Alert> findByUserOrderByCreatedAtDesc(User user);
    
    List<Alert> findByUserAndIsActiveTrue(User user);
    
    List<Alert> findBySymbolAndIsActiveTrue(String symbol);
    
    List<Alert> findByIsTriggeredTrueAndNotificationSentFalse();
    
    List<Alert> findByExpiresAtBeforeAndIsActiveTrue(LocalDateTime dateTime);
    
    @Query("SELECT a FROM Alert a WHERE a.user = :user AND a.isActive = true AND a.symbol = :symbol")
    List<Alert> findActiveAlertsByUserAndSymbol(@Param("user") User user, @Param("symbol") String symbol);
    
    @Query("SELECT a FROM Alert a WHERE a.alertType = :alertType AND a.isActive = true")
    List<Alert> findActiveAlertsByType(@Param("alertType") Alert.AlertType alertType);
    
    @Query("SELECT COUNT(a) FROM Alert a WHERE a.user = :user AND a.isActive = true")
    long countActiveAlertsByUser(@Param("user") User user);
    
    @Query("SELECT a FROM Alert a WHERE a.isActive = true AND a.createdAt < :cutoffDate")
    List<Alert> findOldActiveAlerts(@Param("cutoffDate") LocalDateTime cutoffDate);
}