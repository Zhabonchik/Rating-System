package org.leverx.ratingsystem.service.impl;

import org.leverx.ratingsystem.exception.VerificationCodeException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final static long EXPIRATION_TIME = 24 * 60 * 60; // 24 часа

    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Сохранить код в Redis
    public void saveVerificationCode(String email, String code) {
        redisTemplate.opsForValue().set(email, code, EXPIRATION_TIME, TimeUnit.SECONDS);
    }

    // Получить код из Redis
    public String getVerificationCode(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    // Удалить код из Redis
    public void deleteVerificationCode(String email) {
        redisTemplate.delete(email);
    }

    public void checkVerificationCode(String verificationCode, String userCode) {

        if (verificationCode == null) {
            throw new VerificationCodeException("Verification code expired or does not exist.");
        }

        if (!verificationCode.equals(userCode)) {
            throw new VerificationCodeException("Invalid verification code.");
        }
    }

}
