package io.github.panxiaochao.boot4.common.exception;

import io.github.panxiaochao.boot4.common.enums.IEnum;
import lombok.Getter;

import java.io.Serial;

/**
 * <p>
 * Framework runtime exception.
 * </p>
 *
 * @author Lypxc
 * @since 2022/4/19
 */
@Getter
public class FrameworkRuntimeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2307670685197783604L;

    /**
     * 错误码
     */
    private final int code;

    public FrameworkRuntimeException(IEnum<Integer> responseEnum) {
        super(responseEnum.getMessage());
        this.code = responseEnum.getCode();
    }

    public FrameworkRuntimeException(IEnum<Integer> responseEnum, String message) {
        super(message);
        this.code = responseEnum.getCode();
    }

    public FrameworkRuntimeException(IEnum<Integer> responseEnum, Throwable cause) {
        super(responseEnum.getMessage(), cause);
        this.code = responseEnum.getCode();
    }

    public FrameworkRuntimeException(IEnum<Integer> responseEnum, String message, Throwable cause) {
        super(message, cause);
        this.code = responseEnum.getCode();
    }

}