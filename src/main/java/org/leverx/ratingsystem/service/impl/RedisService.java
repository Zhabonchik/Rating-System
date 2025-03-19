package org.leverx.ratingsystem.service.impl;

import org.leverx.ratingsystem.exception.VerificationCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);
    private final RedisTemplate<String, String> redisTemplate;
    private final static long EXPIRATION_TIME = 24 * 60 * 60; // 24 часа

    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Сохранить код в Redis
    public void saveVerificationCode(String email, String code) {
        logger.info("Saving verification code for email: {} in Redis", email);
        redisTemplate.opsForValue().set(email, code, EXPIRATION_TIME, TimeUnit.SECONDS);
    }

    // Получить код из Redis
    public String getVerificationCode(String email) {
        logger.info("Returning verification code for email: {}", email);
        return redisTemplate.opsForValue().get(email);
    }

    // Удалить код из Redis
    public void deleteVerificationCode(String email) {
        logger.info("Deleting verification code for email: {}", email);
        redisTemplate.delete(email);
    }

    public void checkVerificationCode(String verificationCode, String userCode) {

        if (verificationCode == null) {
            logger.warn("Verification code is null");
            throw new VerificationCodeException("Verification code expired or does not exist.");
        }

        if (!verificationCode.equals(userCode)) {
            logger.warn("Verification and user code don't coincide");
            throw new VerificationCodeException("Invalid verification code.");
        }
    }

}
