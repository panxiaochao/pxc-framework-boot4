package io.github.panxiaochao.boot4.utils.jackson.serializer;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.annotation.JacksonStdImpl;
import tools.jackson.databind.ser.jdk.NumberSerializer;

import java.math.BigDecimal;

/**
 * <p>
 * 大精度转换String, 根据前端 JS Number.MAX_SAFE_INTEGER 与 Number.MIN_SAFE_INTEGER 百度得来.
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-26
 */
@JacksonStdImpl
public class BigNumberSerializer extends NumberSerializer {

    /**
     * 最大范围
     */
    private static final long MAX_SAFE_INTEGER = 9007199254740991L;

    /**
     * 最小范围
     */
    private static final long MIN_SAFE_INTEGER = -9007199254740991L;

    /**
     * 提供实例
     */
    public static final BigNumberSerializer INSTANCE = new BigNumberSerializer(Number.class);

    public BigNumberSerializer(Class<? extends Number> rawType) {
        super(rawType);
    }

    @Override
    public void serialize(Number value, JsonGenerator gen, SerializationContext ctxt) {
        if (value == null) {
            gen.writeNull();
            return;
        }

        if ((value instanceof BigDecimal) && value.longValue() > MAX_SAFE_INTEGER) {
            gen.writeString(((BigDecimal) value).toPlainString());
        }

        // MIN_SAFE_INTEGER < value < MAX_SAFE_INTEGER
        if (value.longValue() > MIN_SAFE_INTEGER && value.longValue() < MAX_SAFE_INTEGER) {
            super.serialize(value, gen, ctxt);
        }
        else {
            gen.writeString(value.toString());
        }
    }

}
