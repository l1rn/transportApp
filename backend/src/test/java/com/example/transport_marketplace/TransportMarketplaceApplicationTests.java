package com.example.transport_marketplace;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"spring.mail.host=localhost",
		"spring.mail.port=587",
		"spring.mail.username=test",
		"spring.mail.password=test"
})
@ActiveProfiles("test")
class TransportMarketplaceApplicationTests {

	@Test
	void contextLoads() {
	}
}
