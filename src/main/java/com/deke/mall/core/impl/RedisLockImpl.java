package com.deke.mall.core.impl;

import com.deke.mall.core.CommonService;
import com.deke.mall.core.RedisLock;
import com.deke.mall.core.RedisLockCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class RedisLockImpl implements RedisLock {

    @Autowired
    private CommonService commonService;

    @Autowired
    private RedisTemplate redisTemplate;

    private ThreadLocal<Long> valueId = new ThreadLocal<>();

    @Override
    public boolean tryLock(String resourceId, long timeOut, RedisLockCondition redisLockCondition) {
        ValueOperations<String, Long> valueOperations= redisTemplate.opsForValue();
        long id = commonService.nextId();
        long endTime = System.currentTimeMillis()+TimeUnit.SECONDS.toMillis(timeOut);
        while (redisLockCondition.shouldNotExit()&&System.currentTimeMillis() <= endTime){
            if (valueOperations.setIfAbsent(resourceId,id,60, TimeUnit.SECONDS)){
                this.valueId.set(id);
                return true;
            }
        }
        return false;
    }

    @Override
    public void unLock(String resourceId) {
        if (valueId.get() == null) return;
        DefaultRedisScript redisScript = new DefaultRedisScript<>();
        redisScript.setLocation(new ClassPathResource("lua/redisUnLock.lua"));
        redisScript.setResultType(Long.class);
        List<String> keys = new ArrayList<>();
        keys.add(resourceId);
        redisTemplate.execute(redisScript,keys,valueId.get());
    }
}
