package com.example.serviceA;

import io.specto.hoverfly.junit.core.Hoverfly;
import io.specto.hoverfly.junit.core.HoverflyConfig;
import io.specto.hoverfly.junit.core.SimulationSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static io.specto.hoverfly.junit.core.HoverflyConfig.localConfigs;
import static io.specto.hoverfly.junit.core.HoverflyMode.SIMULATE;
import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                // it is important for below property not to be localhost
                // since Hoverfly is not configured to intercept localhost
                // and we do want Hoverfly to intercept calls to serviceB and simulate a response
                properties = {"serviceB.base_url=http://serviceb"})
@Import(RestTemplateRoutedThruHoverflyConfig.class)
@Slf4j
public class IntegrationTestWithHoverfly {

    // This approach already knows on which localhost port app was started.
    // Also, it is pre-configured to handle exceptions by still returning
    // a response (with error statuscode) instead of raising an exception
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private RestTemplateBuilder restTemplateBuilderWithHoverfly;
    private static Hoverfly hoverfly;

    @BeforeAll
    static void setUpHoverfly() {
        SimulationSource source = dsl(
                service("http://serviceb")
                        .get("/name")
                        .willReturn(success().body("my friend")));
        HoverflyConfig hoverflyConfig = localConfigs();
        // Turn this ON for hoverfly to intercept calls to localhost
        // However that's a problem since our system under test is running on localhost
        // hoverflyConfig.proxyLocalHost();
        hoverfly = new Hoverfly(hoverflyConfig, SIMULATE);
        hoverfly.start();
        hoverfly.simulate(source);
    }
    @Test
    @SneakyThrows
    void testBasic() {
        // Confirm hoverfly wiring works
        // RestTemplate restTemplateWithHoverfly = restTemplateBuilderWithHoverfly.build();
        // This works
        // ResponseEntity<String> resp0 = restTemplateWithHoverfly.getForEntity("http://www.baeldung.com/api/courses/1", String.class);
        // This also works but to turn it on you have to do localConfigs().proxyLocalHost();
        // But then your calls to system under test will be intercepted making you unable to test it
        // ResponseEntity<String> resp1 = restTemplateWithHoverfly.getForEntity("http://localhost:8082/foo", String.class);

        // Connect to our real app under test
        ResponseEntity<HelloDto> responseEntity = testRestTemplate.getForEntity("/", HelloDto.class);
        assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
        assertEquals("Hello my friend", responseEntity.getBody().getMessage());
    }

}
