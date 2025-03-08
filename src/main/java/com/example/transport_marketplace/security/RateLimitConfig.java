package com.example.transport_marketplace.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {

    @Bean
    public Bucket rateLimitBucket() {
        Bandwidth limit = Bandwidth.simple(20, Duration.ofMinutes(1));
        return Bucket.builder().addLimit(limit).build();
    }

}
