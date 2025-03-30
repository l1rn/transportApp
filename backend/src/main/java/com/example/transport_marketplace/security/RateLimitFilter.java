package com.example.transport_marketplace.security;

import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class RateLimitFilter implements Filter {
    @Autowired
    private final Bucket defaultBucket;
    @Autowired
    private final Bucket authBucket;
    @Autowired
    private final Bucket bookingBucket;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String path = ((HttpServletRequest) request).getRequestURI();
        if(path.startsWith("/api/routes")){
            if(defaultBucket.tryConsume(1)) {
                chain.doFilter(request, response);
            }
            else {
                ((HttpServletResponse) response).setStatus(429);
            }
        } 
        else if (path.startsWith("/api/auth")) {
            if(authBucket.tryConsume(1)){
                chain.doFilter(request, response);
            }else {
                ((HttpServletResponse) response).setStatus(429);
            }
        } else if (path.startsWith("/api/booking")) {
            if(bookingBucket.tryConsume(1)){
                chain.doFilter(request, response);
            }
            else{
                ((HttpServletResponse) response).setStatus(429);
            }
        }
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }
    @Override
    public void destroy() {
        // Cleanup code if needed
    }
}
