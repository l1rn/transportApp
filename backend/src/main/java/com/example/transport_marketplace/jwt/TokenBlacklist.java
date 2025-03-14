package com.example.transport_marketplace.jwt;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBlacklist {
    private final Set<String> revokedTokens = ConcurrentHashMap.newKeySet();

    public void revoke(String token) {
        revokedTokens.add(token);
    }

    public boolean isRevoked(String token) {
        return revokedTokens.contains(token);
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void cleanUp() {
        revokedTokens.clear();
    }
}