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
package io.github.panxiaochao.boot4.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * Request相关请求工具类
 * </p>
 *
 * @author Lypxc
 * @since 2023-05-06
 */
public class RequestUtil {

    /**
     * DELETE
     */
    public static final String METHOD_DELETE = "DELETE";

    /**
     * HEAD
     */
    public static final String METHOD_HEAD = "HEAD";

    /**
     * GET
     */
    public static final String METHOD_GET = "GET";

    /**
     * OPTIONS
     */
    public static final String METHOD_OPTIONS = "OPTIONS";

    /**
     * POST
     */
    public static final String METHOD_POST = "POST";

    /**
     * PUT
     */
    public static final String METHOD_PUT = "PUT";

    /**
     * TRACE
     */
    public static final String METHOD_TRACE = "TRACE";

    /**
     * obtain HttpServletRequest
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        return null == getRequestAttributes() ? null : getRequestAttributes().getRequest();
    }

    /**
     * obtain HttpServletResponse
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        return null == getRequestAttributes() ? null : getRequestAttributes().getResponse();
    }

    /**
     * obtain ServerRequestAttributes
     * @return ServletRequestAttributes
     */
    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes;
        }
        return null;
    }

    /**
     * 将字符串渲染到客户端（以 JSON 格式返回）
     * @param response 渲染对象
     * @param content 待渲染的字符串
     */
    public static void renderString(HttpServletResponse response, String content) {
        try {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8);
            response.getWriter().print(content);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对内容进行 URL 编码
     * @param str 内容
     * @return 编码后的内容
     */
    public static String urlEncode(String str) {
        return URLEncoder.encode(str, StandardCharsets.UTF_8);
    }

    /**
     * 对内容进行 URL 解码
     * @param encodedStr 编码后的内容
     * @return 解码后的内容
     */
    public static String urlDecode(String encodedStr) {
        return URLDecoder.decode(encodedStr, StandardCharsets.UTF_8);
    }

}
