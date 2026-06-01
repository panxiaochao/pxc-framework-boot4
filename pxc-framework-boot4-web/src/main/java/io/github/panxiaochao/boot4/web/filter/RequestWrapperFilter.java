package io.github.panxiaochao.boot4.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * <p>
 * RequestWrapper过滤器
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-26
 */
public class RequestWrapperFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String contentType = request.getContentType();
        // 判断请求类型
        if (!StringUtils.hasText(contentType)) {
            filterChain.doFilter(request, response);
        }
        // fix: 请求类型是表单提交的放过
        else if (StringUtils.hasText(contentType) && contentType.contains("multipart/form-data")) {
            filterChain.doFilter(request, response);
        }
        else {
            // 重新包装 Request Wrapper
            request = new RequestWrapper(request);
            if (null == request) {
                filterChain.doFilter(servletRequest, response);
            }
            else {
                filterChain.doFilter(request, response);
            }
        }
    }

}
