package com.adrhol.edge_service.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.messages")
public class EdgeServiceMessages {

    private String ipNotFound;

    public String getIpNotFound() {
        return ipNotFound;
    }

    public void setIpNotFound(String ipNotFound) {
        this.ipNotFound = ipNotFound;
    }
}
