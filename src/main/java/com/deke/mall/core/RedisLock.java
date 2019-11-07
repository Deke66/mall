package com.deke.mall.core;

public interface RedisLock {
    boolean tryLock(String resourceId,long timeOut,RedisLockCondition redisLockCondition);
    void unLock(String resourceId);
}
