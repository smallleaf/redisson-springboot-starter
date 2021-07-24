package com.share1024.redisson.support;


import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * yesheng
 */
public class RedissonDistributeLock implements Lock{

    private Logger logger  = LoggerFactory.getLogger(RedissonDistributeLock.class);

    private final RedissonClient redissonClient;

    public RedissonDistributeLock(RedissonClient redissonClient){
        this.redissonClient = redissonClient;
    }

    @Override
    public void lock(String key,LockOperator operator) {
        RLock lock = redissonClient.getLock(key);
        lock.lock();
        try {
            operator.doOperator();
        }catch (Exception e){
            logger.error("lock error key {} message {}",key,e.getMessage(),e);
        }finally {
            lock.unlock();
        }

    }

    @Override
    public boolean tryLock(String key, LockOperator operator, long waitTime, TimeUnit timeUnit) {
        RLock lock = redissonClient.getLock(key);
        boolean res = false;
        try {
            res = lock.tryLock(waitTime,timeUnit);
        } catch (InterruptedException e) {
            logger.error("tryLock error key {} message {}",key,e.getMessage(),e);
        }
        if(res){
            try {
                operator.doOperator();
            }catch (Exception e){
                logger.error("tryLock error key {} message {}",key,e.getMessage(),e);
            }
        }
        return res;
    }

    @Override
    public RedissonClient getRedissonClient() {
        return redissonClient;
    }
}
