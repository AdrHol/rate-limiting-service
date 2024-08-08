package com.adrhol.edge_service.filters;

import com.adrhol.edge_service.configuration.EdgeServiceMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    @Autowired
    EdgeServiceMessages edgeServiceMessages;

    public LoggingFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String address = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");

            if(address != null){
                address = address.split(",")[0];
            } else {
                InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
                address = remoteAddress == null ? edgeServiceMessages.getIpNotFound() : remoteAddress.getAddress().getHostName();
            }

            logger.info("{} REQUESTED PATH: {}", address, exchange.getRequest().getPath());
            return chain.filter(exchange);
        };
    }

    public static class Config{

    }
}
