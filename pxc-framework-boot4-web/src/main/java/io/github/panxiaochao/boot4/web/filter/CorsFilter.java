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
package io.github.panxiaochao.boot4.web.filter;

import io.github.panxiaochao.boot4.utils.BooleanUtil;
import io.github.panxiaochao.boot4.utils.StrUtil;
import io.github.panxiaochao.boot4.web.config.properties.GlobalWebProperties;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

/**
 * <p>
 * CorsFilter过滤器.
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-26
 */
@RequiredArgsConstructor
public class CorsFilter implements Filter {

    private final GlobalWebProperties globalWebProperties;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 解决跨域的问题
        cors(request, response);
        // 预请求，直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        // 放行
        filterChain.doFilter(request, response);
    }

    private void cors(HttpServletRequest request, HttpServletResponse response) {
        String allowCredentials = globalWebProperties.getCors().getAllowCredentials();
        response.setHeader("Access-Control-Allow-Credentials", allowCredentials);
        // 判断 allowCredentials 是否为true, 如果为true, 则设置 Allow-Origin 为请求头中的Origin
        // !!!:当 Allow-Credentials 设置为 true 时，Access-Control-Allow-Origin 不能使用通配符 *
        if (Boolean.TRUE.equals(BooleanUtil.toBoolean(allowCredentials))) {
            String origin = request.getHeader("Origin");
            if (StrUtil.isNotBlank(origin)) {
                response.setHeader("Access-Control-Allow-Origin", origin);
            }
        }
        else {
            response.setHeader("Access-Control-Allow-Origin", globalWebProperties.getCors().getAllowAllOrigin());
        }
        response.setHeader("Access-Control-Allow-Methods", globalWebProperties.getCors().getAllowedMethods());
        response.setHeader("Access-Control-Allow-Headers", globalWebProperties.getCors().getAllowedHeaders());
        response.setHeader("Access-Control-Expose-Headers", globalWebProperties.getCors().getExposeHeaders());
        response.setHeader("Access-Control-Max-Age", globalWebProperties.getCors().getMaxAge());
    }

}
