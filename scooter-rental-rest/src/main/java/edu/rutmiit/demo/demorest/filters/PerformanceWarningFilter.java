package edu.rutmiit.demo.demorest.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(2)
public class PerformanceWarningFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(PerformanceWarningFilter.class);
    private static final long SLOW_REQUEST_THRESHOLD_MS = 20; // Порог для медленных запросов

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, servletResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            // Логируем предупреждение для медленных запросов
            if (duration > SLOW_REQUEST_THRESHOLD_MS && request.getRequestURI().startsWith("/api/")) {
                log.warn("Slow request detected: {} {} took {}ms",
                        request.getMethod(), request.getRequestURI(), duration);
            }
        }
    }
}