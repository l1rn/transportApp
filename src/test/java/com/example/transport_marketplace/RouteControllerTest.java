package com.example.transport_marketplace;

import com.example.transport_marketplace.entity.routes.Route;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RouteControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testSearchRoutes(){
    }
}
