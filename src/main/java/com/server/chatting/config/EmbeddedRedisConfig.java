package com.server.chatting.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
@Slf4j
@Configuration
public class EmbeddedRedisConfig implements InitializingBean, DisposableBean {

    @Value("${spring.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @Override
    public void afterPropertiesSet() throws Exception {
        redisServer = RedisServer.builder()
                .port(redisPort)
                .setting("maxmemory 128M")
                .build();
        redisServer.start();
    }

    @Override
    public void destroy() throws Exception {
        log.info("stopRedis");
        if (redisServer != null) {
            log.info("stopRedis--stop");
            redisServer.stop();
        }
    }
}
