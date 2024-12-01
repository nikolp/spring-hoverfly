package com.example.serviceA;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

// Exploring stuff ... not a full integration test
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
// Usually we like to inject things using constructor
// However @LocalServerPort is a bit funky and is handled differently by the framework
// So do everything by field injection.
// Do not mark fields as final, do @Autowire them, do not include @AllArgsConstructor
public class IntegrationExplorationTest {
    @LocalServerPort
    private int serviceAPort;

    // This approach already knows on which port app was started.
    // Also, it is pre-configured to handle exceptions by still returning
    // a response instead of raising an exception
    @Autowired
    private TestRestTemplate testRestTemplate;

    // This approach does not know the port yet but we configure in @BeforeEach
    // Need additional configs in order not to raise exceptions
    private RestTemplate restTemplate;
    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplateBuilder()
                .rootUri("http://localhost:" + serviceAPort).build();
    }

    // Uncomment to try with RestTemplate
    // @Test
    void withRestTemplate() {
        ResponseEntity<HelloDto> responseEntity = restTemplate.getForEntity("/", HelloDto.class);
    }
    @Test
    @SneakyThrows
    void testBasic() {
        log.info("Access app at: " + "http://localhost:" + serviceAPort);
        ResponseEntity<HelloDto> responseEntity = testRestTemplate.getForEntity("/", HelloDto.class);
        // The call fails because we don't have serviceB up yet
        // But the test passes because we don't have any assertions
        //Thread.sleep(60000);
    }

}
