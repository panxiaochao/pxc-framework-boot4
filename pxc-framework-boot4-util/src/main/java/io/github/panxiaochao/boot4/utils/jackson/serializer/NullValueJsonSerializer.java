package io.github.panxiaochao.boot4.utils.jackson.serializer;

import org.springframework.util.ReflectionUtils;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.annotation.JacksonStdImpl;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 针对空对象的处理
 * </p>
 *
 * @author Lypxc
 * @since 2022/8/30
 */
@JacksonStdImpl
public class NullValueJsonSerializer extends ValueSerializer<Object> {

    private static final String EMPTY_STRING = "";

    public static final NullValueJsonSerializer INSTANCE = new NullValueJsonSerializer();

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializationContext ctxt) {
        String fieldName = gen.streamWriteContext().currentName();
        // 反射获取字段
        Field field = ReflectionUtils.findField(gen.currentValue().getClass(), fieldName);
        if (Objects.nonNull(field)) {
            // 数字类型Integer、Double、Long等返回""
            if (Number.class.isAssignableFrom(field.getType())) {
                gen.writeString(EMPTY_STRING);
                return;
            }
            // String类型返回""
            if (Objects.equals(field.getType(), String.class)) {
                gen.writeString(EMPTY_STRING);
                return;
            }
            // Boolean类型返回false
            if (Objects.equals(field.getType(), Boolean.class)) {
                gen.writeBoolean(false);
                return;
            }
            // List类型返回[]
            if (isArrayType(field.getType())) {
                gen.writeStartArray();
                gen.writeEndArray();
                return;
            }
            // Map类型返回{}
            if (isMapType(field.getType())) {
                gen.writeStartObject();
                gen.writeEndObject();
                return;
            }
        }
        // 其他Object默认返回""
        gen.writeString(EMPTY_STRING);
    }

    /**
     * 是否是数组
     * @param rawClass rawClass
     * @return boolean
     */
    private boolean isArrayType(Class<?> rawClass) {
        return rawClass.isArray() || Collection.class.isAssignableFrom(rawClass);
    }

    /**
     * 是否是map
     * @param rawClass rawClass
     * @return boolean
     */
    private boolean isMapType(Class<?> rawClass) {
        return Map.class.isAssignableFrom(rawClass);

    }

}
