package com.example.transport_marketplace.controllers;

import com.example.transport_marketplace.dto.account.AccountUserDTO;
import com.example.transport_marketplace.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}