package io.github.panxiaochao.boot4.ip2region.config;

import io.github.panxiaochao.boot4.ip2region.core.Ip2regionClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * IP转换地址模块自动配置类
 * </p>
 *
 * @author lypxc
 * @since 2025-10-09
 * @version 1.0
 */
@AutoConfiguration
public class Ip2regionAutoConfiguration {

    /**
     * LOGGER Ip2regionAutoConfiguration.class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Ip2regionAutoConfiguration.class);

    /**
     * 创建 Ip2regionClient 对象
     * @return Ip2regionClient
     */
    @Bean
    public Ip2regionClient ip2regionClient() {
        LOGGER.info("配置[Ip2regionClient]成功");
        return new Ip2regionClient();
    }

}
