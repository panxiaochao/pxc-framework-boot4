package io.github.panxiaochao.boot4.redis.config;

import io.github.panxiaochao.boot4.redis.ratelimiter.aspect.RateLimiterAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConfiguration;

/**
 * <p>
 * RateLimiter 自动配置类.
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-28
 */
@AutoConfiguration(after = RedisConfiguration.class)
public class RateLimiterAutoConfiguration {

    @Bean
    public RateLimiterAspect rateLimiterAspect() {
        return new RateLimiterAspect();
    }

}
