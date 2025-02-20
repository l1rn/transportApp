package com.example.transport_marketplace.jwt;

import io.jsonwebtoken.Jwts;

public class JwtUtil {
    private static final String SECRET_KEY = "key";

    public static String generateToken(String username){
        return Jwts.builder()
                .setSubject()
                .setIssuedAt()
    }
}
