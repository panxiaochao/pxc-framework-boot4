package io.github.panxiaochao.boot4.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * Encoding过滤器
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-26
 */
public class EncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        // 编码
        // fix(doFilter)[2026-01-21 16:50:24]: Tomcat 11以上才有
        // setCharacterEncoding(Charset encoding) 方法
        servletRequest.setCharacterEncoding(StandardCharsets.UTF_8.name());
        servletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        // 放行
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
