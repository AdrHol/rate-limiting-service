package com.adrhol.rate_limiting_service.filter;


import com.adrhol.rate_limiting_service.service.RatesBucketService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class RedirectFilter implements Filter {
    @Value("${redirect.destination}")
    private String destination;

    private final RatesBucketService ratesBucketService;
    @Autowired
    public RedirectFilter (RatesBucketService ratesBucketService){
        this.ratesBucketService = ratesBucketService;
    }

    private static final Logger logger = LoggerFactory.getLogger(RedirectFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

//        This implementation works fine at this point, but in the future additional layer like API Gateway will
//        cause IP to be almost constant. So final implementation will take a look at header ip value

        String ip = request.getRemoteAddr();

        if(ratesBucketService.validateRequestBucket(ip)){
            String query = request.getQueryString();
            String newUrl = destination + request.getRequestURI() + (query == null ? "" : query);
            logger.info("Redirected to: " + newUrl);
            response.sendRedirect(newUrl);
        }

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
