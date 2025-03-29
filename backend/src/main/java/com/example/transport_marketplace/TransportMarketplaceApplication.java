package com.example.transport_marketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TransportMarketplaceApplication {
	public static void main(String[] args) {
		SpringApplication.run(TransportMarketplaceApplication.class, args);
	}
}


