package com.share1024.redisson.support;

import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

public interface DistributeLock {

    void lock(String key,LockOperator operator);

    boolean tryLock(String key, LockOperator operator, long waitTime, TimeUnit timeUnit);

    RedissonClient getRedissonClient();
}
