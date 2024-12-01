package com.example.serviceA;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class IntegrationTestWithMocks {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    ServiceBClient serviceBClient;

    @Test
    void testBasic() {
        when(serviceBClient.getName()).thenReturn("My Friend");
        ResponseEntity<HelloDto> responseEntity = testRestTemplate.getForEntity("/", HelloDto.class);
        assertEquals(HttpStatusCode.valueOf(202), responseEntity.getStatusCode());
        assertEquals("Hello My Friend", responseEntity.getBody().getMessage());
    }
}