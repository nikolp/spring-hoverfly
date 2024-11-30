package com.example.serviceA;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class Controller {
    private final ServiceBClient serviceBClient;
    @GetMapping("/")
    HelloDto hello() {
        final String name = serviceBClient.getName();
        return new HelloDto("Hello " + name);
    }
}
