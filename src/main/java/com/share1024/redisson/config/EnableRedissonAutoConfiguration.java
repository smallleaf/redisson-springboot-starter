package com.share1024.redisson.config;

import com.share1024.redisson.condition.SingleConditional;
import com.share1024.redisson.support.LockMethodInteceptor;
import com.share1024.redisson.support.LockPointCut;
import com.share1024.redisson.support.RedissonDistributeLock;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;

/**
 * \* @Author: yesheng
 * \* Date: 2021/7/23 16:49
 * \* Description:
 * \
 */
@ConditionalOnClass(RedissonClient.class)
@EnableConfigurationProperties({SingleServerConfigProperties.class,ConfigProperties.class})
@Configuration
@Import(AutoProxyRegistrar.class)
@Slf4j
public class EnableRedissonAutoConfiguration {

    @Bean
    public Config config(ConfigProperties configProperties){
        Config config = new Config();
        config.setLockWatchdogTimeout(configProperties.getLockWatchdogTimeout());
        return config;
    }

    @Conditional(SingleConditional.class)
    @ConditionalOnMissingBean(RedissonClient.class)
    @Bean
    public RedissonClient redissonClient(Config config,SingleServerConfigProperties singleServerConfigProperties){
        SingleServerConfig singleServerConfig = config.useSingleServer();
        BeanUtils.copyProperties(singleServerConfigProperties,singleServerConfig);
        return Redisson.create(config);
    }


    @ConditionalOnBean(RedissonClient.class)
    @Bean
    public RedissonDistributeLock lock(RedissonClient redissonClient){
        return new RedissonDistributeLock(redissonClient);
    }


    @ConditionalOnBean(RedissonClient.class)
    @Bean
    public DefaultPointcutAdvisor distributeLockAdvisor(RedissonClient redissonClient){
        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
        defaultPointcutAdvisor.setPointcut(new LockPointCut());
        defaultPointcutAdvisor.setAdvice(new LockMethodInteceptor(redissonClient));
        return defaultPointcutAdvisor;
    }

}