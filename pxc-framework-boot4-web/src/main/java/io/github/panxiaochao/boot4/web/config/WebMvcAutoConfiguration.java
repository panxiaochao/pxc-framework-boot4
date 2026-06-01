package io.github.panxiaochao.boot4.web.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.http.converter.HttpMessageConverters;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * WebMvcAutoConfiguration is a AutoConfiguration.
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-26
 */
@AutoConfiguration
public class WebMvcAutoConfiguration implements WebMvcConfigurer {

    /**
     * 配置 StringHttpMessageConverter 编码 UTF-8 Spring 7.x 推荐使用
     * HttpMessageConverters.ServerBuilder 来配置消息转换器
     * @param builder the HttpMessageConverters.ServerBuilder to configure
     */
    @Override
    public void configureMessageConverters(HttpMessageConverters.ServerBuilder builder) {
        builder.withStringConverter(new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

}
