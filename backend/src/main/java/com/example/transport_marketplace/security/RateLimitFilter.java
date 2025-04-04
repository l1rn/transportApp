package com.example.transport_marketplace.security;

import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitFilter implements Filter {
    private final Bucket anonymousBucket;
    private final Bucket userBucket;
    private final Bucket adminBucket;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        Bucket bucket = selectBucket(httpServletRequest);

        if(bucket.tryConsume(1)){
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setHeader("X-Rate-Limit-Remaining", String.valueOf(bucket.getAvailableTokens()));
            chain.doFilter(request, response);
        }
        else{
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(429);
            httpServletResponse.setHeader("X-Rate-Limit-Retry-After-Seconds", "60");
            response.getWriter().write("Слишком много запросов");
        }
    }

    private Bucket selectBucket(HttpServletRequest request){
        if(request.getRequestURI().startsWith("/api/auth/sign-in")) {
            return anonymousBucket;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()){
            if(authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
                return adminBucket;
            }
            return userBucket;
        }
        return anonymousBucket;
    }
}
