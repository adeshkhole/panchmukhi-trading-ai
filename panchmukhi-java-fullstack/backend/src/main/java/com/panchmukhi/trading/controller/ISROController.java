package com.panchmukhi.trading.controller;

import com.panchmukhi.trading.service.MLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/isro")
@CrossOrigin(origins = "*")
public class ISROController {

    private final MLService mlService;

    @Autowired
    public ISROController(MLService mlService) {
        this.mlService = mlService;
    }

    @GetMapping("/missions")
    public Mono<ResponseEntity<List<Map<String, Object>>>> getMissions() {
        return mlService.getISROData()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
