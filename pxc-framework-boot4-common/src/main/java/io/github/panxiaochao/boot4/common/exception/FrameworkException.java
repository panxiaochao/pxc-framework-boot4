package io.github.panxiaochao.boot4.common.exception;

import io.github.panxiaochao.boot4.common.enums.IEnum;
import lombok.Getter;

import java.io.Serial;

/**
 * <p>
 * Framework exception.
 * </p>
 *
 * @author Lypxc
 * @since 2022-11-28
 */
@Getter
public class FrameworkException extends Exception {

    @Serial
    private static final long serialVersionUID = -4367714276298639594L;

    /**
     * 错误码
     */
    private final int code;

    public FrameworkException(IEnum<Integer> responseEnum) {
        super(responseEnum.getMessage());
        this.code = responseEnum.getCode();
    }

    public FrameworkException(IEnum<Integer> responseEnum, String message) {
        super(message);
        this.code = responseEnum.getCode();
    }

    public FrameworkException(IEnum<Integer> responseEnum, Throwable cause) {
        super(responseEnum.getMessage(), cause);
        this.code = responseEnum.getCode();
    }

    public FrameworkException(IEnum<Integer> responseEnum, String message, Throwable cause) {
        super(message, cause);
        this.code = responseEnum.getCode();
    }

}