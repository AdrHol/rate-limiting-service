package com.adrhol.rate_limiting_service.service;

import com.adrhol.rate_limiting_service.dto.RateBucketDTO;
import com.adrhol.rate_limiting_service.exceptions.RateLimitExceededException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RatesBucketService {

    @Value("${spring.redis.hashBucket}")
    private String HASH_NAME;
    @Value("${spring.redis.request-limit}")
    private int RATES_LIMIT;
    private RedisTemplate<String, RateBucketDTO> redisTemplate;
    private HashOperations<String, String, RateBucketDTO> hashOperations;



    @Autowired
    public RatesBucketService (RedisTemplate<String, RateBucketDTO> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init(){
        this.hashOperations = redisTemplate.opsForHash();
    }
    public boolean validateRequestBucket(String ip){
        RateBucketDTO retrievedBucket = hashOperations.get(HASH_NAME, ip);
        return retrievedBucket == null ? insertNewBucketToHash(ip) : decrementBucket(retrievedBucket);
    }
    private boolean insertNewBucketToHash(String ip){
        RateBucketDTO bucketDTO = new RateBucketDTO(ip, RATES_LIMIT);
        hashOperations.put(HASH_NAME, ip, bucketDTO);
        return true;
    }
    private boolean decrementBucket(RateBucketDTO retrievedBucket) throws RateLimitExceededException {
        retrievedBucket.decrement();
        hashOperations.put(HASH_NAME, retrievedBucket.getIp() , retrievedBucket);
        return true;
    }
}
