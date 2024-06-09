package com.adrhol.rate_limiting_service.filter;


import com.adrhol.rate_limiting_service.dto.RateBucketDTO;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.filters.RateLimitFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;

public class RedirectFilter implements Filter {

    private String destination;
    private static final Logger logger = LoggerFactory.getLogger(RedirectFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String HASH = "rates";


//        This implementation works fine at this point, but in the future additional layer like API Gateway will
//        cause IP to be almost constant. So final implementation will take a look at header ip value

        String ip = request.getRemoteAddr();

        String query = request.getQueryString();
        String newUrl = destination + request.getRequestURI() + (query == null ? "" : query);
        logger.info("Redirected to: " + newUrl);
        response.sendRedirect(newUrl);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    public void setDestination(String destination){
        this.destination = destination;
    }
}
