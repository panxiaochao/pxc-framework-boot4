package io.github.panxiaochao.boot4.web.handler;

import io.github.panxiaochao.boot4.common.constants.CommonResponseEnum;
import io.github.panxiaochao.boot4.common.constants.ServletResponseEnum;
import io.github.panxiaochao.boot4.common.response.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * <p>
 * 默认情况下，标准的 Spring MVC 异常会通过 DefaultHandlerExceptionResolver 来处理 继承自定义处理一些特殊错误.
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-26
 */
@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers,
            HttpStatusCode statusCode, WebRequest request) {
        ServletResponseEnum servletExceptionEnum;
        try {
            servletExceptionEnum = ServletResponseEnum.valueOf(e.getClass().getSimpleName());
        }
        catch (IllegalArgumentException e1) {
            LOGGER.error("class [{}] not defined in enum {}", e.getClass().getName(),
                    ServletResponseEnum.class.getName());
            return new ResponseEntity<>(R.fail(CommonResponseEnum.INTERNAL_SERVER_ERROR.getCode(),
                    CommonResponseEnum.INTERNAL_SERVER_ERROR.getMessage(), body), headers, statusCode);
        }

        return new ResponseEntity<>(R.fail(servletExceptionEnum.getCode(), servletExceptionEnum.getMessage(), body),
                headers, statusCode);
    }

}
