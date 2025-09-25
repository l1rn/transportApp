package com.example.transport_marketplace.config;

import java.security.SecureRandom;

public class CodeGenerator {
    private static final SecureRandom random = new SecureRandom();

    public static String generateCode(){
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
