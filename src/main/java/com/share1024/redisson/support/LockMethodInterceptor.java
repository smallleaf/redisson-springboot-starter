package com.share1024.redisson.support;

import com.share1024.redisson.annotation.DistributeMethodLock;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.concurrent.TimeUnit;

public class LockMethodInterceptor implements MethodInterceptor {

    private final RedissonClient redissonClient;

    public LockMethodInterceptor(RedissonClient redissonClient){
        this.redissonClient = redissonClient;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        DistributeMethodLock distributeLock = AnnotationUtils.findAnnotation(methodInvocation.getMethod(), DistributeMethodLock.class);
        RLock lock = redissonClient.getLock(distributeLock.value());
        boolean res = lock.tryLock(distributeLock.lockMostMilTime(), TimeUnit.MILLISECONDS);
        Object result = null;
        if(res){
            try {
                result = methodInvocation.proceed();
            }finally {
                lock.unlock();
            }
        }
        return result;
    }
}
