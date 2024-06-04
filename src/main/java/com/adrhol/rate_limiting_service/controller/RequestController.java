package com.adrhol.rate_limiting_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {

    @GetMapping("/**")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Intial commit");
    }
}
