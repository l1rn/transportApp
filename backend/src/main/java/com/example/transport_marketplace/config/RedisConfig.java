package com.example.transport_marketplace.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.function.Supplier;

@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private int redisPort;

    private RedisClient redisClient(){
        return RedisClient.create(RedisURI.builder()
                .withHost(redisHost)
                .withPort(redisPort)
                .withSsl(false)
                .build());
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new LettuceConnectionFactory(configuration);
    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate(){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public ProxyManager<String> lettuceBasedProxyManager(){
        RedisClient redisClient = redisClient();
        StatefulRedisConnection<String, byte[]> redisConnection = redisClient
                .connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));

        return LettuceBasedProxyManager.builderFor(redisConnection)
                .withExpirationStrategy(
                        ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofMinutes(1L)))
                .build();
    }

    @Bean
    public Supplier<BucketConfiguration> bucketConfiguration() {
        return () -> BucketConfiguration.builder()
                .addLimit(Bandwidth.simple(200L, Duration.ofMinutes(1L)))
                .build();
    }
}
