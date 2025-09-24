package com.example.transport_marketplace.controllers;

import com.example.transport_marketplace.dto.account.AccountUserDTO;
import com.example.transport_marketplace.dto.account.WithdrawDTO;
import com.example.transport_marketplace.service.AccountService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class AccountController {
    @Autowired
    private final AccountService accountService;

    @GetMapping("/account/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<AccountUserDTO>> getAllAccounts(){
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @PostMapping("/account/top-up")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> withdraw(
            HttpServletRequest request,
            @RequestBody WithdrawDTO dto
            ){
        try{
            Cookie[] cookies = request.getCookies();
            String accessToken = Arrays.stream(cookies)
                    .filter(c -> "accessToken".equals(c.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElseThrow(() -> new RuntimeException("Нету куков"));
            double amount = dto.getAmount();
            accountService.withdraw(accessToken, amount);
            return ResponseEntity.ok("Пополнение на " + amount);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Не удалось пополнить");
        }
    }
}