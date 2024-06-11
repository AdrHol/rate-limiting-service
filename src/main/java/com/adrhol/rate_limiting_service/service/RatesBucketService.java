package com.adrhol.rate_limiting_service.service;

import com.adrhol.rate_limiting_service.model.RateBucketDTO;
import com.adrhol.rate_limiting_service.exceptions.RateLimitExceededException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class RatesBucketService {

    @Value("${spring.redis.hashBucket}")
    private String HASH_NAME;
    @Value("${redirect.bucket.request-limit}")
    private int RATES_LIMIT;
    @Value("${redirect.bucket.duration}")
    private int SECONDS;
    private RedisTemplate<String, RateBucketDTO> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    private HashOperations<String, String, RateBucketDTO> hashOperations;



    @Autowired
    public RatesBucketService (RedisTemplate<String, RateBucketDTO> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init(){
        this.hashOperations = redisTemplate.opsForHash();
    }
    public boolean validateRequestBucket(String ip) throws JsonProcessingException {
        RateBucketDTO receivedBucket = hashOperations.get(HASH_NAME, ip);
        return tokenToBeCreated(receivedBucket) ? insertNewBucketToHash(ip) : decrementBucket(receivedBucket);
    }
    private boolean tokenToBeCreated(RateBucketDTO rateBucket){
        return rateBucket == null || rateBucket.getDuration().isBefore(LocalDateTime.now());
    }
    private boolean insertNewBucketToHash(String ip) throws JsonProcessingException {
        LocalDateTime duration = LocalDateTime.now().plus(Duration.ofSeconds(SECONDS));
        RateBucketDTO bucketDTO = new RateBucketDTO(ip, RATES_LIMIT, duration);
        hashOperations.put(HASH_NAME, ip, bucketDTO);
        return true;
    }
    private boolean decrementBucket(RateBucketDTO retrievedBucket) throws RateLimitExceededException, JsonProcessingException {
        retrievedBucket.decrement();
        hashOperations.put(HASH_NAME, retrievedBucket.getIp() , retrievedBucket);
        return true;
    }
}
