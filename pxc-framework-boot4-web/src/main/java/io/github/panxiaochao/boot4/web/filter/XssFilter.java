package io.github.panxiaochao.boot4.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * Xss 过滤器
 * </p>
 *
 * @author Lypxc
 * @since 2024-07-03
 * @version 1.0
 */
public class XssFilter implements Filter {

    /**
     * LOGGER XssFilter.class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(XssFilter.class);

    private final static PathMatcher PATHMATCHER = new AntPathMatcher();

    private static final String[] WHITE_SUFFIXES = new String[] { "js", "css", "ico", "png", "jpg", "jpeg", "gif",
            "svg", "ttf", "fon", "ttc" };

    private final List<String> excludeUrls;

    public XssFilter(List<String> whiteList) {
        this.excludeUrls = whiteList;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestUrl = request.getRequestURI();
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())
                || Strings.CS.endsWithAny(requestUrl, WHITE_SUFFIXES)
                || excludeUrls.stream().anyMatch(excludeUrl -> PATHMATCHER.match(excludeUrl, requestUrl))) {
            filterChain.doFilter(request, response);
        }
        else {
            LOGGER.info("XssFilter request url: {}, method: {}", requestUrl, request.getMethod());
            filterChain.doFilter(new XssWrapper(request), response);
        }
    }

}
