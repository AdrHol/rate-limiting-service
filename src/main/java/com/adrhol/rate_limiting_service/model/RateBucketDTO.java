package com.adrhol.rate_limiting_service.model;

import com.adrhol.rate_limiting_service.exceptions.RateLimitExceededException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RateBucketDTO implements Serializable {
    private String ip;
    private int requestsLeft;
    private LocalDateTime duration;

    public RateBucketDTO(){};
    @JsonCreator
    public RateBucketDTO(@JsonProperty("ip") String ip,
                         @JsonProperty("requestsLeft") int requestsLeft,
                         @JsonProperty("duration") LocalDateTime duration) {
        this.ip = ip;
        this.requestsLeft = requestsLeft;
        this.duration = duration;
    }

    public boolean decrement() {
        if(this.requestsLeft > 0){
            this.requestsLeft--;
            return true;
        } else {
            throw new RateLimitExceededException(this.ip);
        }
    }

    public String getIp() {
        return ip;
    }

    public int getRequestsLeft() {
        return requestsLeft;
    }
    public LocalDateTime getDuration(){
        return duration;
    }
    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.writeValueAsString(this);
    }
}
