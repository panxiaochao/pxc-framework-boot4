package io.github.panxiaochao.boot4.utils.jackson.serializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.annotation.JacksonStdImpl;

import java.lang.reflect.Field;
import java.time.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * 针对空对象的处理
 * </p>
 *
 * @author Lypxc
 * @since 2022/8/30
 */
@JacksonStdImpl
public class NullValueJacksonSerializer extends ValueSerializer<Object> {

    /**
     * LOGGER NullValueJacksonSerializer.class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(NullValueJacksonSerializer.class);

    private static final String EMPTY_STRING = "";

    public static final NullValueJacksonSerializer INSTANCE = new NullValueJacksonSerializer();

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializationContext ctxt) {
        // 1. 安全检查：如果当前的流上下文中没有获取到父级对象，直接退回到默认处理
        Object currentValue = gen.currentValue();
        if (Objects.isNull(currentValue)) {
            gen.writeString(EMPTY_STRING);
            return;
        }
        // 2. 获取当前序列化 null 值对应的属性名
        String fieldName = gen.streamWriteContext().currentName();
        if (Objects.nonNull(fieldName)) {
            // 反射获取字段信息
            try {
                Field field = ReflectionUtils.findField(currentValue.getClass(), fieldName);
                if (Objects.nonNull(field)) {
                    // 数字类型Integer、Double、Long等返回""
                    if (Number.class.isAssignableFrom(field.getType())) {
                        gen.writeNull();
                        return;
                    }
                    // String类型返回""
                    if (Objects.equals(field.getType(), String.class)) {
                        gen.writeString(EMPTY_STRING);
                        return;
                    }
                    // Boolean类型返回false
                    if (Objects.equals(field.getType(), Boolean.class)
                            || Objects.equals(field.getType(), Boolean.TYPE)) {
                        gen.writeBoolean(false);
                        return;
                    }
                    // Optional类型返回null
                    if (Objects.equals(field.getType(), Optional.class)) {
                        gen.writeNull();
                        return;
                    }
                    // 日期时间类型返回""
                    if (isDateTimeType(field.getType())) {
                        gen.writeString(EMPTY_STRING);
                        return;
                    }
                    // 数组或集合类型 (List, Set 等) 返回 []
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
            }
            catch (Exception e) {
                LOGGER.error("NullValueJacksonSerializer serialize error, fieldName: {}", fieldName, e);
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

    /**
     * 是否是日期时间类型
     * @param fieldType fieldType
     * @return boolean
     */
    private boolean isDateTimeType(Class<?> fieldType) {
        //@formatter:off
        // Java 8+ java.time 新类型
        if (fieldType == LocalDate.class
                || fieldType == LocalDateTime.class
                || fieldType == LocalTime.class
                || fieldType == ZonedDateTime.class
                || fieldType == OffsetDateTime.class
                || fieldType == OffsetTime.class
                || fieldType == Instant.class
                || fieldType == Year.class
                || fieldType == YearMonth.class
                || fieldType == MonthDay.class
                || fieldType == Duration.class
                || fieldType == Period.class) {
            return true;
        }
        // Java 8 之前的旧类型
        if (Date.class.isAssignableFrom(fieldType)
                || Calendar.class.isAssignableFrom(fieldType)) {
            return true;
        }
        // java.sql 包下的日期时间类型
        if (fieldType.getName().startsWith("java.sql.Date")
                || fieldType.getName().startsWith("java.sql.Time")
                || fieldType.getName().startsWith("java.sql.Timestamp")) {
            return true;
        }
        //@formatter:on
        return false;
    }

}
