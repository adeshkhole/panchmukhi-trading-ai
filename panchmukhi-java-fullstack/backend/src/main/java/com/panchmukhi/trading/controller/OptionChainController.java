package com.panchmukhi.trading.controller;

import com.panchmukhi.trading.service.MLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/options")
@CrossOrigin(origins = "*")
public class OptionChainController {

    private final MLService mlService;

    @Autowired
    public OptionChainController(MLService mlService) {
        this.mlService = mlService;
    }

    @GetMapping("/chain")
    public Mono<ResponseEntity<Map<String, Object>>> getOptionChain() {
        return mlService.getOptionChainData()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
