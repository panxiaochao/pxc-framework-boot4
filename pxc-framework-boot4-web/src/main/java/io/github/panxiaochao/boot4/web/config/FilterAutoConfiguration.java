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

import io.github.panxiaochao.boot4.web.config.properties.GlobalWebProperties;
import io.github.panxiaochao.boot4.web.filter.CorsFilter;
import io.github.panxiaochao.boot4.web.filter.EncodingFilter;
import io.github.panxiaochao.boot4.web.filter.RequestWrapperFilter;
import io.github.panxiaochao.boot4.web.filter.XssFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * Filter过滤器自动装配
 * </p>
 * <p>
 * order的数值越小, 则优先级越高
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-26
 */
@AutoConfiguration
@EnableConfigurationProperties({ GlobalWebProperties.class })
public class FilterAutoConfiguration {

    /**
     * EncodingFilter 过滤器
     * @return FilterRegistrationBean
     */
    @Bean
    @FilterRegistration(name = "encodingFilter", urlPatterns = "/*", order = 0)
    public EncodingFilter encodingFilter() {
        return new EncodingFilter();
    }

    /**
     * CorsFilter 过滤器
     * @return FilterRegistrationBean
     */
    @Bean
    @ConditionalOnProperty(name = "spring.pxc-framework-boot4.cors.enabled", havingValue = "true")
    @FilterRegistration(name = "corsFilter", urlPatterns = "/*", order = FilterRegistrationBean.HIGHEST_PRECEDENCE)
    public CorsFilter corsFilter(GlobalWebProperties globalWebProperties) {
        return new CorsFilter(globalWebProperties);
    }

    /**
     * RequestWrapperFilter 过滤器
     * @return FilterRegistrationBean
     */
    @Bean
    @FilterRegistration(name = "requestWrapperFilter", urlPatterns = "/*", order = 1)
    public RequestWrapperFilter requestWrapperFilter() {
        return new RequestWrapperFilter();
    }

    /**
     * XssFilter 过滤器
     * @return FilterRegistrationBean
     */
    @Bean
    @ConditionalOnProperty(name = "spring.pxc-framework-boot4.xss.enabled", havingValue = "true")
    @FilterRegistration(name = "xssFilter", urlPatterns = "/*", order = 2)
    public XssFilter xssFilter(GlobalWebProperties globalWebProperties) {
        return new XssFilter(globalWebProperties.getXss().getExcludeUrls());
    }

}
