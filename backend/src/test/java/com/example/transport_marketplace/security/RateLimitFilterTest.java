//package com.example.transport_marketplace.security;
//
//
//import com.example.transport_marketplace.controllers.AuthController;
//import com.example.transport_marketplace.controllers.RouteController;
//import com.example.transport_marketplace.enums.Role;
//import com.example.transport_marketplace.jwt.JwtAuthenticationFilter;
//import com.example.transport_marketplace.jwt.JwtService;
//import com.example.transport_marketplace.jwt.TokenBlacklist;
//import com.example.transport_marketplace.model.Route;
//import com.example.transport_marketplace.model.User;
//import com.example.transport_marketplace.service.RouteService;
//import com.example.transport_marketplace.service.UserService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.github.bucket4j.Bucket;
//import io.github.bucket4j.BucketConfiguration;
//import io.github.bucket4j.ConsumptionProbe;
//import io.github.bucket4j.distributed.BucketProxy;
//import io.github.bucket4j.distributed.proxy.ProxyManager;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//import java.util.function.Function;
//import java.util.function.Supplier;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class RateLimitFilterTest {
//    @Mock
//    private ProxyManager<String> proxyManager;
//
//    @Mock
//    private Supplier<BucketConfiguration> bucketConfiguration;
//
//    @Mock
//    private HttpServletRequest request;
//
//    @Mock
//    private HttpServletResponse response;
//
//    @Mock
//    private FilterChain filterChain;
//
//    @Mock
//    private Bucket bucket;
//
//    @Mock
//    private ConsumptionProbe probe;
//
//    @Mock
//    private PrintWriter printWriter;
//
//    @InjectMocks
//    private RateLimitFilter rateLimitFilter;
//    @BeforeEach
//    void setupUsers() throws IOException {
//        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
//        when(proxyManager.builder().build(anyString(), (Supplier<BucketConfiguration>) any())).thenReturn((BucketProxy) bucket);
//        when(response.getWriter()).thenReturn(printWriter);
//    }
//
//    @Test
//    void whenTokenNotConsumed_shouldReturn429() {
//
//    }
//
//}
