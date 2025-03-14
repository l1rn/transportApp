package com.example.transport_marketplace;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = {
				"spring.datasource.url=jdbc:h2:mem:testdb",
				"spring.datasource.driver-class-name=org.h2.Driver",
				"spring.h2.console.enabled=true"
		}
)
class TransportMarketplaceApplicationTests {

	@Test
	void contextLoads() {
	}
}
