/*
 * Copyright © 2026-2027 Lypxc (545685602@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
