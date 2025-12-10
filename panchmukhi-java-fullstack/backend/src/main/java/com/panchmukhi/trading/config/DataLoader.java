package com.panchmukhi.trading.config;

import com.panchmukhi.trading.model.User;
import com.panchmukhi.trading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            // Check if admin user exists
            if (!userRepository.existsByEmail("admin@panchmukhi.com")) {
                User admin = new User();
                admin.setName("Admin User");
                admin.setEmail("admin@panchmukhi.com");
                admin.setPhone("9999999999");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setPlan(User.PlanType.PRO);
                admin.setLanguage("mr");
                admin.setTheme("dark");
                admin.setVoiceAlerts(true);

                userRepository.save(admin);
                System.out.println("✅ Default admin user created:");
                System.out.println("   Email: admin@panchmukhi.com");
                System.out.println("   Password: admin123");
            } else {
                System.out.println("ℹ️  Admin user already exists");
            }
        };
    }
}
