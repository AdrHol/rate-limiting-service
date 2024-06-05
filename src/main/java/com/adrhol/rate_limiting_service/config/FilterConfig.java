package com.adrhol.rate_limiting_service.config;


import com.adrhol.rate_limiting_service.filter.RedirectFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Value("${redirect.destination}")
    private String destination;

    @Bean
    public FilterRegistrationBean<RedirectFilter> filterRegistration(RedirectFilter filter){
        FilterRegistrationBean<RedirectFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
    @Bean
    public RedirectFilter filter(){
        RedirectFilter filter = new RedirectFilter();
        filter.setDestination(destination);
        return filter;
    }
}
