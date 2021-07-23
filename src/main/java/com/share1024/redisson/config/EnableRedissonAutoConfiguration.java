package com.share1024.redisson.config;

import com.share1024.redisson.condition.SingleConditional;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * \* @Author: yesheng
 * \* Date: 2021/7/23 16:49
 * \* Description:
 * \
 */
@ConditionalOnClass(RedissonClient.class)
@EnableConfigurationProperties({SingleServerConfigProperties.class,ConfigProperties.class})
@Configuration
public class EnableRedissonAutoConfiguration {

    @Bean
    public Config config(ConfigProperties configProperties){
        Config config = new Config();
        BeanUtils.copyProperties(configProperties,config);
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
}