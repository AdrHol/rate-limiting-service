package com.adrhol.rate_limiting_service.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RedirectFilter implements Filter {

    private String destination;

    private static final Logger logger = LoggerFactory.getLogger(RedirectFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
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
