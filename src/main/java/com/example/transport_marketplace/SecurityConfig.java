//package com.example.transport_marketplace;
//
//import com.example.transport_marketplace.Authorization.Account;
//import com.example.transport_marketplace.Authorization.AccountService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//    private final AccountService accountService;
//    private final PasswordEncoder passwordEncoder;
//
//    public SecurityConfig(AccountService accountService, PasswordEncoder passwordEncoder) {
//        this.accountService = accountService;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//        http
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers(
//                                "/accounts", "/accounts/login").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginProcessingUrl("/login")
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .permitAll()
//                )
//                .csrf(csrf -> csrf.disable());
//        return http.build();
//    }
//    @Bean
//    public UserDetailsService userDetailsService (){
//        return username ->{
//            Account account = accountService.getAccountByUsername(username);
//            if(account == null){
//                throw new UsernameNotFoundException("Пользователь не найден: " + username);
//            }
//            return org.springframework.security.core.userdetails.User
//                    .withUsername(account.getUsername())
//                    .password(account.getPassword())
//                    .roles("USER")
//                    .build();
//        };
//    }
//}
