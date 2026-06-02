package io.github.panxiaochao.boot4.web.handler;

import io.github.panxiaochao.boot4.common.constants.CommonResponseEnum;
import io.github.panxiaochao.boot4.common.exception.FrameworkException;
import io.github.panxiaochao.boot4.common.exception.FrameworkRuntimeException;
import io.github.panxiaochao.boot4.common.response.R;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParseException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>
 * 统一异常处理器类增强, 默认不拦截4xx错误, 比如(400,405,404)等
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-26
 */
@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * 生产环境
     */
    private final static String ENV_PROD = "prod";

    /**
     * 当前环境
     */
    @Value("${spring.profiles.active:}")
    private String profile;

    /**
     * 常规兜底报错
     * @param e Exception
     * @return R
     */
    @ExceptionHandler(value = Exception.class)
    public R<String> exception(Exception e) {
        LOG.error(e.getMessage(), e);
        if (ENV_PROD.equals(profile)) {
            return R.fail(CommonResponseEnum.INTERNAL_SERVER_ERROR.getMessage());
        }
        return R.fail(e.getMessage());
    }

    /**
     * IllegalArgumentException 报错拦截
     * @param e Exception
     * @return R
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public R<String> illegalArgumentException(IllegalArgumentException e) {
        LOG.error(e.getMessage(), e);
        if (ENV_PROD.equals(profile)) {
            return R.fail(CommonResponseEnum.INTERNAL_SERVER_ERROR.getMessage());
        }
        return R.fail(e.getMessage());
    }

    /**
     * 常规框架业务异常
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = FrameworkException.class)
    public R<String> handleBusinessException(FrameworkException e) {
        LOG.error(e.getMessage(), e);
        return R.fail(e.getMessage());
    }

    /**
     * 常规框架业务运行时异常
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = FrameworkRuntimeException.class)
    public R<String> handleBaseException(FrameworkRuntimeException e) {
        LOG.error(e.getMessage(), e);
        return R.fail(e.getCode(), e.getMessage());
    }

    /**
     * 参数绑定异常
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = BindException.class)
    public R<String> handleBindException(BindException e) {
        LOG.error(e.getMessage(), e);
        return wrapperBindingResult(e.getBindingResult());
    }

    /**
     * 参数校验异常，将校验失败的所有异常组合成一条错误信息
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R<String> handleValidException(MethodArgumentNotValidException e) {
        LOG.error(e.getMessage(), e);
        return wrapperBindingResult(e.getBindingResult());
    }

    /**
     * 包装绑定异常结果
     * @param bindingResult 绑定结果
     * @return 异常结果
     */
    private R<String> wrapperBindingResult(BindingResult bindingResult) {
        StringBuilder msg = new StringBuilder();
        for (ObjectError error : bindingResult.getAllErrors()) {
            msg.append(", ");
            if (error instanceof FieldError) {
                msg.append(((FieldError) error).getField()).append(": ");
            }
            msg.append(error.getDefaultMessage() == null ? "" : error.getDefaultMessage());
        }
        return R.fail(CommonResponseEnum.INTERNAL_SERVER_ERROR.getCode(), msg.substring(2));
    }

    /**
     * JSON 解析异常（Jackson 在处理 JSON 格式出错时抛出） 可能是请求体格式非法，也可能是服务端反序列化失败
     */
    @ExceptionHandler(JsonParseException.class)
    public R<Void> handleJsonParseException(JsonParseException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        LOG.error("请求地址'{}' 发生 JSON 解析异常: {}", requestURI, e.getMessage());
        return R.fail("请求数据格式错误");
    }

}
