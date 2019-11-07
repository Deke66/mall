package com.deke.mall.core;

@FunctionalInterface
public interface RedisLockCondition {
    boolean shouldNotExit();
}
