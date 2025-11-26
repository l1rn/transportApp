package com.example.transport_marketplace.config;

import java.security.SecureRandom;
import java.time.LocalDateTime;


public class CodeGenerator {
    private static final SecureRandom random = new SecureRandom();

    public static String generateCode(){
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public static LocalDateTime generateExpiryTime(){
        return LocalDateTime.now().plusMinutes(1);
    }

    public static boolean isCodeValid(String inputCode, String storedCode, LocalDateTime expiryTime){
        if(!inputCode.equals(storedCode)){
            return false;
        }
        return LocalDateTime.now().isBefore(expiryTime);
    }
}
