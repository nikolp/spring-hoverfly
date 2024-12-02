package com.example.serviceA;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.net.InetSocketAddress;
import java.net.Proxy;

@TestConfiguration
public class RestTemplateRoutedThruHoverflyConfig {

    // Wanted to create a builder that is hooked into Hoverfly but things work well
    // even without any explicit hooking up.
    @Bean
    RestTemplateBuilder restTemplateBuilder() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
//        builder.requestFactory(() -> {
//            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 8500)); // Default Hoverfly proxy port
//            requestFactory.setProxy(proxy);
//            return requestFactory;
//        });
        return builder;
    }
}
