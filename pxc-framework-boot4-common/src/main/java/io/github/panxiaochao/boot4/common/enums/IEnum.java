package io.github.panxiaochao.boot4.common.enums;

/**
 * <p>
 * 枚举基类.
 * </p>
 *
 * @author Lypxc
 * @since 2023-03-13
 */
public interface IEnum<T> {

    /**
     * 码值
     * @return T类型
     */
    T getCode();

    /**
     * 码值对应描述
     * @return 返回信息
     */
    String getMessage();

}