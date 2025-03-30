package com.example.transport_marketplace.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {

    @Qualifier(value = "defaultBucket")
    @Bean
    public Bucket defaultBucket(){
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofSeconds(30)));
        return Bucket.builder().addLimit(limit).build();
    }

    @Qualifier(value = "bookingBucket")
    @Bean
    public Bucket bookingBucket(){
        Bandwidth limit = Bandwidth.classic(15, Refill.greedy(15, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    @Qualifier(value = "authBucket")
    @Bean
    public Bucket authBucket(){
        Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

}
