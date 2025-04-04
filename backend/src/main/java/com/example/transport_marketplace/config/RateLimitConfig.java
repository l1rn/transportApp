package com.example.transport_marketplace.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {

    @Bean
    public Bucket anonymousBucket(){
        return Bucket.builder()
                .addLimit(limit -> limit.capacity(50).refillGreedy(10, Duration.ofMinutes(1)))
                .build();
    }
    @Bean
    public Bucket userBucket(){
        return Bucket.builder()
                .addLimit(limit -> limit.capacity(200).refillGreedy(50, Duration.ofMinutes(1)))
                .build();
    }

    @Bean Bucket adminBucket(){
        return Bucket.builder()
                .addLimit(limit -> limit.capacity(500).refillGreedy(100, Duration.ofMinutes(1)))
                .build();
    }
}