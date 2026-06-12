package io.github.panxiaochao.boot4.redis.repeatsubmit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 限制重复提交注解
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-29
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatSubmitLimiter {

    /**
     * 提交间隔时间, 小于此时间间隔属于重复提交, 默认毫秒
     */
    long interval() default 5000;

    /**
     * 时间单位格式, 默认毫秒
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 自定义提示消息
     */
    String message() default "";

}
