package com.example.serviceA;

import static io.specto.hoverfly.junit.core.HoverflyMode.SIMULATE;
import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.HttpBodyConverter.jsonWithSingleQuotes;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;
import static io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.any;
import static io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.equalsTo;
import static io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.equalsToJson;
import static io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.equalsToXml;
import static io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.matches;
import static io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.matchesJsonPath;
import static io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.matchesXPath;
import static io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URISyntaxException;

import io.specto.hoverfly.junit.core.Hoverfly;
import io.specto.hoverfly.junit.core.HoverflyConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import io.specto.hoverfly.junit.core.SimulationSource;
import io.specto.hoverfly.junit.rule.HoverflyRule;

public class HoverflyExampleTest {

    private static Hoverfly hoverfly;


    @BeforeAll
    static void setUpHoverfly() {
        SimulationSource source = dsl(
                service("http://www.baeldung.com")
                        .get("/api/courses/1").willReturn(success().body(jsonWithSingleQuotes("{'id':'1','name':'HCI'}")))
                        .post("/api/courses").willReturn(success()),
                service(matches("www.*dung.com"))
                        .get(startsWith("/api/student")).queryParam("page", any()).willReturn(success())
                        .post(equalsTo("/api/student")).body(equalsToJson(jsonWithSingleQuotes("{'id':'1','name':'Joe'}"))).willReturn(success())
                        .put("/api/student/1").body(matchesJsonPath("$.name")).willReturn(success())
                        .post("/api/student").body(equalsToXml("<student><id>2</id><name>John</name></student>")).willReturn(success())
                        .put("/api/student/2").body(matchesXPath("/student/name")).willReturn(success()));

        hoverfly = new Hoverfly(SIMULATE);
        hoverfly.start();
        hoverfly.simulate(source);
    }

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    public void givenGetCourseById_whenRequestSimulated_thenAPICalledSuccessfully() throws URISyntaxException {
        final ResponseEntity<String> courseResponse = restTemplate.getForEntity("http://www.baeldung.com/api/courses/1", String.class);

        assertEquals(HttpStatus.OK, courseResponse.getStatusCode());
        assertEquals("{\"id\":\"1\",\"name\":\"HCI\"}", courseResponse.getBody());
    }
}
