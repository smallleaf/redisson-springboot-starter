package com.share1024.redisson.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * \* @Author: yesheng
 * \* Date: 2021/7/23 18:12
 * \* Description:
 * \
 */
@ConfigurationProperties("spring.redisson.config")
@Data
public class ConfigProperties {

    private int threads = 16;

    private int nettyThreads = 32;

    private boolean referenceEnabled = true;

    private long lockWatchdogTimeout = 30 * 1000;

    private long reliableTopicWatchdogTimeout = TimeUnit.MINUTES.toMillis(10);

    private boolean keepPubSubOrder = true;

    private boolean useScriptCache = false;

    private int minCleanUpDelay = 5;

    private int maxCleanUpDelay = 30*60;

    private int cleanUpKeysAmount = 100;
}