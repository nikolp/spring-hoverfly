package com.example.serviceA;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ProjectConfig {

    @Bean
    public RestTemplate restTemplate(@Value("${serviceB.base_url}") String apiUrl,
                                     RestTemplateBuilder builder) {
        RestTemplate result = builder.rootUri(apiUrl).build();
        return result;
    }
}
