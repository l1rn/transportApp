package com.example.transport_marketplace.jwt;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RateLimitingFilter implements Filter {
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    private Bucket createNewBucket() {
        return Bucket.builder()
                .addLimit(limit -> limit.capacity(50).refillGreedy(10, Duration.ofSeconds(1)))
                .build();
    }

    private final Cache<String, Bucket> cache = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(10))
            .build();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        String clientIp = request.getRemoteAddr();
        Bucket bucket = cache.get(clientIp, k -> createNewBucket());

        if(bucket.tryConsume(1)){
            chain.doFilter(request, response);
        }else {
            ((HttpServletResponse) response).sendError(429, "Слишком много запросов");
        }
    }
}
