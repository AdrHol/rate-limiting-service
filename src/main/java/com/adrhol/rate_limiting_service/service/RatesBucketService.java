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
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    private HashOperations<String, String, String> hashOperations;



    @Autowired
    public RatesBucketService (RedisTemplate<String, String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init(){
        this.hashOperations = redisTemplate.opsForHash();
    }
    public boolean validateRequestBucket(String ip) throws JsonProcessingException {
        String receivedJson = hashOperations.get(HASH_NAME, ip);
        RateBucketDTO retrievedBucket = objectMapper.readValue(receivedJson, RateBucketDTO.class);
        return tokenToBeCreated(retrievedBucket) ? insertNewBucketToHash(ip) : decrementBucket(retrievedBucket);
    }
    private boolean tokenToBeCreated(RateBucketDTO rateBucket){
        return rateBucket == null || rateBucket.getDuration().isBefore(LocalDateTime.now());
    }
    private boolean insertNewBucketToHash(String ip) throws JsonProcessingException {
        LocalDateTime duration = LocalDateTime.now().plus(Duration.ofSeconds(SECONDS));
        RateBucketDTO bucketDTO = new RateBucketDTO(ip, RATES_LIMIT, duration);
        hashOperations.put(HASH_NAME, ip, bucketDTO.toJson());
        return true;
    }
    private boolean decrementBucket(RateBucketDTO retrievedBucket) throws RateLimitExceededException, JsonProcessingException {
        retrievedBucket.decrement();
        hashOperations.put(HASH_NAME, retrievedBucket.getIp() , retrievedBucket.toJson());
        return true;
    }
}
