package se.jaitco.queueticketapi.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Johan Aschan on 2016-08-31.
 */
@Configuration
public class RedisConfig {

    @Value("${redis.url}")
    private String redisUrl;

    @Value("${redis.password}")
    private String redisPassword;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer()
                .setAddress(redisUrl);

        if(!redisPassword.isEmpty()){
            singleServerConfig.setPassword(redisPassword);
        }
        return Redisson.create(config);
    }

}
