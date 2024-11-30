package com.example.serviceA;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class ServiceBClient {

    RestTemplate restTemplate;
    String getName() {
        ResponseEntity<String> response = restTemplate.getForEntity("/name", String.class);
        return response.getBody();
    }
}
