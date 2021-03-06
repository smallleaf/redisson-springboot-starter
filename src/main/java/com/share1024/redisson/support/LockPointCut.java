package com.share1024.redisson.support;

import com.share1024.redisson.annotation.DistributeMethodLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;


@Slf4j
public class LockPointCut extends StaticMethodMatcherPointcut {

    @Override
    public boolean matches(Method method, Class<?> aClass) {
        DistributeMethodLock lock = AnnotationUtils.findAnnotation(method, DistributeMethodLock.class);
        if(lock != null){
            if(!StringUtils.hasText(lock.value())){
                throw new RuntimeException("the DistributeMethodLock name cannot be empty "+aClass.getName());
            }
            return  true;
        }
        return false;
    }
}
