package com.example.transport_marketplace.service;

import com.example.transport_marketplace.config.CodeGenerator;
import com.example.transport_marketplace.dto.account.AccountUserDTO;
import com.example.transport_marketplace.jwt.JwtService;
import com.example.transport_marketplace.model.Account;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.repo.AccountRepository;
import com.example.transport_marketplace.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    @Autowired
    private final AccountRepository accountRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final JwtService jwtService;
    private final JavaMailSender mailSender;

    public boolean verifyCode(String actualCode, String enteredCode){
        return enteredCode.equals(actualCode);
    }

    public String sendConfirmationCode(String userEmail){
        String code = CodeGenerator.generateCode();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@ololotravel.com");
        message.setTo(userEmail);
        message.setSubject("Payment Confirmation Code");
        message.setText("Your confirmation code is: " + code);

        mailSender.send(message);

        return code;
    }

    public Account getAccountByUserId(int userId){
        return accountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Не удалось найти кошелен по User ID"));
    }

    public void withdraw(String accessToken, double amount) {
        User user = userRepository.findByUsername(jwtService.getUsernameFromToken(accessToken))
                .orElseThrow(() -> new RuntimeException("Не удалось найти юзера по токену"));

        if(accountRepository.findByUserId(user.getId()).isPresent()){
            Account acc = accountRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Счет не найден"));
            acc.setBalance(acc.getBalance() + amount);
            accountRepository.save(acc);
        }

        Account account = Account.builder()
                .user(user)
                .balance(amount)
                .build();

        accountRepository.save(account);
    }

    public List<AccountUserDTO> getAllAccounts(){
        List<Account> accounts = accountRepository.findAll();

        return accounts.stream()
                .map(account -> {
                    AccountUserDTO dto = new AccountUserDTO();
                    dto.setBalance(account.getBalance());

                    String username = userRepository.findById(
                            account.getUser().getId()
                    ).orElseThrow(() -> new RuntimeException("Не удалось найти пользователя"))
                            .getUsername();
                    dto.setUsername(username);

                    return dto;
                })
                    .collect(Collectors.toList());
    }
}
