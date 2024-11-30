package com.example.serviceB;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class Controller {
    @GetMapping("/name")
    String name() {
        return generateRandomString(5);
    }
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        char[] characters = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length);
            char randomChar = characters[randomIndex];
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
