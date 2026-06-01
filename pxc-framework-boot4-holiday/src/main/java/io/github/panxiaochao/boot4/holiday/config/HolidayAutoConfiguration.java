package io.github.panxiaochao.boot4.holiday.config;

import io.github.panxiaochao.boot4.holiday.config.properties.HolidayProperties;
import io.github.panxiaochao.boot4.holiday.core.HolidayClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * Holiday 自动配置类
 * </p>
 *
 * @author Lypxc
 * @since 2024-04-02
 * @version 1.0
 */
@AutoConfiguration
@EnableConfigurationProperties(HolidayProperties.class)
public class HolidayAutoConfiguration {

    /**
     * LOGGER HolidayAutoConfiguration.class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HolidayAutoConfiguration.class);

    /**
     * Holiday client
     * @return HolidayClient
     */
    @Bean
    public HolidayClient holidayClient() {
        LOGGER.info("配置[HolidayClient]成功！");
        return new HolidayClient();
    }

}
