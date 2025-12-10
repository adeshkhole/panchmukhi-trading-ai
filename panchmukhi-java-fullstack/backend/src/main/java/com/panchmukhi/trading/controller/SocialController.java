package com.panchmukhi.trading.controller;

import com.panchmukhi.trading.service.MLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/social")
@CrossOrigin(origins = "*")
public class SocialController {

    private final MLService mlService;

    @Autowired
    public SocialController(MLService mlService) {
        this.mlService = mlService;
    }

    @GetMapping("/feed")
    public Mono<ResponseEntity<List<Map<String, Object>>>> getSocialFeed() {
        return mlService.getSocialData()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
