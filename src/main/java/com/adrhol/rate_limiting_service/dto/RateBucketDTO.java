package com.adrhol.rate_limiting_service.dto;

import com.adrhol.rate_limiting_service.exceptions.RateLimitExceededException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class RateBucketDTO implements Serializable {
    private String ip;
    private int requestsLeft;

    public RateBucketDTO(){};
    @JsonCreator
    public RateBucketDTO(@JsonProperty("ip") String ip,
                         @JsonProperty("requestsLeft") int requestsLeft) {
        this.ip = ip;
        this.requestsLeft = requestsLeft;
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
}
