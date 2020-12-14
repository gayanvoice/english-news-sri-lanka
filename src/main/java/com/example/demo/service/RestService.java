package com.example.demo.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestService {

    private final RestTemplate restTemplate;

    public RestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public boolean setAlive(String url) {
        try {
            ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);
            return responseEntity.getStatusCode() == HttpStatus.OK;
        } catch(Exception e) {
            return false;
        }
    }
}