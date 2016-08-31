package se.jaitco.queueticketapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

/**
 * Created by Johan Aschan on 2016-08-31.
 */
@Configuration
public class RedisConfig {

    @Value("${redis.url}")
    private String redisUrl;

    @Bean
    public JedisPool jedisPool(){
        return new JedisPool(redisUrl);
    }
}
