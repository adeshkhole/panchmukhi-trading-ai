package com.panchmukhi.trading.controller;

import com.panchmukhi.trading.dto.AuthResponse;
import com.panchmukhi.trading.dto.LoginRequest;
import com.panchmukhi.trading.dto.SignupRequest;
import com.panchmukhi.trading.model.User;
import com.panchmukhi.trading.repository.UserRepository;
import com.panchmukhi.trading.security.JwtTokenProvider;
import com.panchmukhi.trading.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateToken(authentication);
            String refreshToken = tokenProvider.generateRefreshToken(loginRequest.getEmail());

            User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);

            return ResponseEntity.ok(new AuthResponse(jwt, refreshToken, user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Error: Invalid credentials! " + e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body("Error: Email is already in use!");
        }

        if (userRepository.existsByPhone(signUpRequest.getPhone())) {
            return ResponseEntity.badRequest()
                    .body("Error: Phone number is already in use!");
        }

        // Create new user's account
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPhone(signUpRequest.getPhone());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setLanguage(signUpRequest.getLanguage() != null ? signUpRequest.getLanguage() : "en");
        user.setTheme(signUpRequest.getTheme() != null ? signUpRequest.getTheme() : "dark");
        user.setVoiceAlerts(signUpRequest.getVoiceAlerts() != null ? signUpRequest.getVoiceAlerts() : true);

        if (signUpRequest.getPlan() != null) {
            try {
                user.setPlan(User.PlanType.valueOf(signUpRequest.getPlan().toUpperCase()));
            } catch (IllegalArgumentException e) {
                user.setPlan(User.PlanType.FREE);
            }
        } else {
            user.setPlan(User.PlanType.FREE);
        }

        userRepository.save(user);

        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signUpRequest.getEmail(),
                        signUpRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(signUpRequest.getEmail());

        return ResponseEntity.ok(new AuthResponse(jwt, refreshToken, user));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        try {
            String username = tokenProvider.getUsernameFromToken(refreshToken);

            if (username != null && tokenProvider.validateToken(refreshToken,
                    userDetailsService.loadUserByUsername(username))) {

                String newJwt = tokenProvider.generateTokenFromUsername(username);
                return ResponseEntity.ok(new AuthResponse(newJwt, refreshToken, null));
            }
        } catch (Exception e) {
            System.err.println("Token refresh failed: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Error: Invalid refresh token!");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            User user = userRepository.findByEmail(username).orElse(null);
            if (user != null) {
                return ResponseEntity.ok(user);
            }
        } catch (Exception e) {
            System.err.println("Get current user failed: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Error: User not found!");
    }
}