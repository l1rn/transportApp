package com.example.transport_marketplace.service;
import com.example.transport_marketplace.config.CodeGenerator;
import com.example.transport_marketplace.dto.users.UserSettingsResponse;
import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.jwt.JwtService;
import com.example.transport_marketplace.model.*;
import com.example.transport_marketplace.repo.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final BookingService bookingService;
    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private final BookingRepository bookingRepository;
    @Autowired
    private final RouteRepository routeRepository;
    @Autowired
    private final DeviceRepository deviceRepository;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final EmailService emailService;

    private final Map<String, String> confirmationCodes = new ConcurrentHashMap<>();
    private final Map<String, Double> pendingAmounts = new ConcurrentHashMap<>();
    private final Map<String, String> pendingEmails = new ConcurrentHashMap<>();


    public User create(User user){
        if(userRepository.existsByUsername(user.getUsername())){
            throw new RuntimeException("Имя пользователя занято!");
        }
        return userRepository.save(user);
    }

    @CacheEvict(value = "users")
    @Transactional
    public void delete(int id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь c ID " + id + " не найден"));
        refreshTokenRepository.deleteByUser(user);
        bookingRepository.deleteByUserId(id);

        List<Booking> bookings = bookingService.getBookingByUserId(id);
        bookings.forEach(booking -> {
            Route route = booking.getRoute();
            route.setAvailableSeats(route.getAvailableSeats() + 1);
            routeRepository.save(route);
        });
        userRepository.delete(user);
    }
    @Cacheable(value = "users", key = "#username")
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public UserDetailsService userDetailsService(){
        return this::getByUsername;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Transactional
    public Role getUserRole(User user) {
        return user.getRole();
    }

    @Cacheable(value = "users", key = "#id")
    public User getById(int id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь c ID" + id + " не найден"));
    }

    public UserSettingsResponse getInfoByUsername(String accessToken){
        User user = userRepository.findByUsername(jwtService.getUsernameFromToken(accessToken))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Device> devices = deviceRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Не найдено ни одно устройсто"));

        return UserSettingsResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .balance(user.getBalance())
                .devices(devices)
                .build();
    }

    public void setAdmin(User user){
        user.setRole(Role.ROLE_ADMIN);
        userRepository.save(user);
    }

    /// ACCOUNT FUNCTIONS ///

    public String setUserEmail(String accessToken, String newEmail){
        User user = userRepository.findByUsername(jwtService.getUsernameFromToken(accessToken))
                .orElseThrow(() -> new RuntimeException("Не удалось найти юзера по токену"));

        String code = CodeGenerator.generateCode();
        confirmationCodes.put(user.getUsername(), code);
        pendingEmails.put(user.getUsername(), newEmail);

        emailService.sendConfirmationCodeNewEmail(newEmail, code);

        return "Код подтверждения отправлен на: " + newEmail;
    }

    public String confirmEmail(String accessToken, String code){
        User user = userRepository.findByUsername(jwtService.getUsernameFromToken(accessToken))
                .orElseThrow(() -> new RuntimeException("Не удалось найти юзера по токену"));

        String savedCode = confirmationCodes.get(user.getUsername());
        String newEmail = pendingEmails.get(user.getUsername());

        if(!code.equals(savedCode)){
            throw new RuntimeException("Неверный код подтверждения!");
        }

        user.setEmail(newEmail);
        userRepository.save(user);

        confirmationCodes.remove(user.getUsername());
        pendingEmails.remove(user.getUsername());

        return user.getEmail();
    }

    public String requestTopUp(String accessToken, double amount){
        User user = userRepository.findByUsername(jwtService.getUsernameFromToken(accessToken))
                .orElseThrow(() -> new RuntimeException("Не удалось найти юзера по токену"));

        String code = CodeGenerator.generateCode();
        confirmationCodes.put(user.getEmail(), code);
        pendingAmounts.put(user.getEmail(), amount);

        emailService.sendConfirmationCodeTopUp(user.getEmail(), code, amount);
        return "Код подтверждения отправлен на " + user.getEmail();
    }

    public double confirmTopUp(String accessToken, String code) {
        User user = userRepository.findByUsername(jwtService.getUsernameFromToken(accessToken))
                .orElseThrow(() -> new RuntimeException("Не удалось найти юзера по токену"));

        String savedCode = confirmationCodes.get(user.getEmail());
        Double amount = pendingAmounts.get(user.getEmail());

        if(savedCode == null || amount == null) {
            throw new RuntimeException("Сначала запросите пополнение");
        }

        if(!savedCode.equals(code)) {
            throw new RuntimeException("Неверный код подтверждения!");
        }

        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);

        confirmationCodes.remove(user.getEmail());
        pendingAmounts.remove(user.getEmail());
        return amount;
    }
}
