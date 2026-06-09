package io.github.panxiaochao.boot4.redis.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * Redisson 自定义属性
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-27
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.pxc-framework-boot4.redisson", ignoreInvalidFields = true)
public class Boot4RedissonProperties {

    /**
     * redis 缓存 key 前缀
     */
    private String keyPrefix;

    /**
     * 线程池数量, 默认16
     */
    private int threads = 16;

    /**
     * Netty线程池数量, 默认32
     */
    private int nettyThreads = 32;

}
