package com.example.transport_marketplace.security;

import jakarta.servlet.http.HttpServletRequest;

public class SecurityUtils {
    public static String getThirdApiPartyAppKey(HttpServletRequest request){
        return request.getHeader("X-API-Key");
    }
}
