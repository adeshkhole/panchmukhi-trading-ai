package com.panchmukhi.trading.repository;

import com.panchmukhi.trading.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByPhone(String phone);
    
    boolean existsByEmail(String email);
    
    boolean existsByPhone(String phone);
    
    List<User> findByStatusAndLastLoginBefore(User.UserStatus status, LocalDateTime date);
    
    List<User> findByPlanAndStatus(User.PlanType plan, User.UserStatus status);
    
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.status = :status")
    List<User> findByRoleAndStatus(@Param("role") User.Role role, @Param("status") User.UserStatus status);
    
    @Query("SELECT u FROM User u WHERE u.twoFactorEnabled = true AND u.twoFactorSecret IS NOT NULL")
    List<User> findUsersWithTwoFactorEnabled();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.status = :status")
    long countByStatus(@Param("status") User.UserStatus status);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.plan = :plan")
    long countByPlan(@Param("plan") User.PlanType plan);
}