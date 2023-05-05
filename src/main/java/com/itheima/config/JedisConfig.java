package com.itheima.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.text.MessageFormat;
import java.time.Duration;

@Configuration
public class JedisConfig extends CachingConfigurerSupport {
    private Logger logger = LoggerFactory.getLogger(JedisConfig.class);

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.jedis.pool.max-active}")
    private int maxActive;

    @Value("${spring.data.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.data.redis.jedis.pool.min-idle}")
    private int minIdle;

    @Value("${spring.data.redis.jedis.pool.max-wait}")
    private long maxWaitMillis;

    @Value("${spring.data.redis.timeout}")
    private int timeout;
    @Bean
    public JedisPool redisPoolFactory() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(this.maxIdle);
        config.setMaxWait(Duration.ofMillis(this.maxWaitMillis));
        config.setMaxTotal(this.maxActive);
        config.setMinIdle(this.minIdle);
        JedisPool pool = new JedisPool(config, host, port, timeout, null);
        logger.info("JedisPool Ingestion Success!");
        logger.info(MessageFormat.format("Redis 地址: {0}:{1}", host, port));
        return pool;
    }
}
