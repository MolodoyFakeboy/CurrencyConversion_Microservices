package com.rabbit.study.config;

import com.rabbit.study.util.LockKeyUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisLock {

    private final RedisTemplate<String,Long> redisTemplate;

    public boolean acquireLock(long expireMillis, String taskKey) {
        var lockKey = LockKeyUtil.getLockKey(taskKey);
        var expireAt = redisTemplate.opsForValue().get(lockKey);
        var currentTime = System.currentTimeMillis();

        if (Objects.nonNull(expireAt)) {
            if (expireAt <= currentTime) {
                redisTemplate.delete(lockKey);
            } else {
                return false;
            }
        }

        var expire = currentTime + expireMillis;

        return Optional
                .ofNullable(redisTemplate.opsForValue().setIfAbsent(lockKey,expire))
                .orElse(false);

    }

    public void releaseLock(String taskKey){
        String lockKey = LockKeyUtil.getLockKey(taskKey);
        redisTemplate.delete(lockKey);
    }

}
