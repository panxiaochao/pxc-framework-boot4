package io.github.panxiaochao.boot4.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.panxiaochao.boot4.utils.date.DatePattern;
import io.github.panxiaochao.boot4.utils.jackson.CustomizeJavaTimeModule;
import io.github.panxiaochao.boot4.utils.jackson.serializer.NullValueJacksonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.NamedType;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.databind.ser.BeanSerializerFactory;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * <p>
 * Jackson 工具类
 * </p>
 *
 * @author Lypxc
 * @since 2023-12-25
 */
public class JacksonUtil {

    /**
     * not init
     */
    private JacksonUtil() {
        throw new RuntimeException("can't be construct");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonUtil.class);

    private static final JsonMapper JSON_MAPPER;

    static {
        JSON_MAPPER = JsonMapper.builder()
            // 设置默认区域为中文
            .defaultLocale(Locale.CHINA)
            // 设置默认时区为上海时区
            .defaultTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))
            // 对象的所有字段全部列入，还是其他的选项，可以忽略null等
            .changeDefaultPropertyInclusion(value -> JsonInclude.Value.ALL_ALWAYS)
            // .changeDefaultVisibility(vc -> vc.withVisibility(PropertyAccessor.ALL,
            // JsonAutoDetect.Visibility.NONE)
            // .withVisibility(PropertyAccessor.GETTER,
            // JsonAutoDetect.Visibility.PUBLIC_ONLY)
            // .withVisibility(PropertyAccessor.IS_GETTER,
            // JsonAutoDetect.Visibility.PUBLIC_ONLY)
            // .withVisibility(PropertyAccessor.SETTER,
            // JsonAutoDetect.Visibility.PUBLIC_ONLY)
            // .withVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            // .withVisibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.ANY))
            // 设置 Date 类型的序列化及反序列化格式
            .defaultDateFormat(new SimpleDateFormat(DatePattern.NORMAL_DATE_TIME_PATTERN))
            // 忽略空Bean转json的错误
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            // 忽略未知属性，防止json字符串中存在，java对象中不存在对应属性的情况出现错误
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            // 注册一个时间序列化及反序列化的处理模块,用于解决jdk8中localDateTime等的序列化问题
            .addModule(new CustomizeJavaTimeModule())
            // 开启严格模式，用于在反序列化 JSON 时，默认true
            // 若解析完目标对象后输入流中仍有额外字符（如多余逗号、括号、文本等），则抛出JsonParseException
            .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
            // NULL 值处理
            .serializerFactory(
                    BeanSerializerFactory.instance.withNullValueSerializer(NullValueJacksonSerializer.INSTANCE))
            .build();
    }

    /**
     * @return ObjectMapper
     */
    public static JsonMapper jsonMapper() {
        return JSON_MAPPER;
    }

    /**
     * Object to Json string.
     * @param obj obj
     * @return Json String
     */
    public static String toString(Object obj) {
        if (obj != null) {
            if (obj.getClass() == String.class) {
                return (String) obj;
            }
            try {
                return JSON_MAPPER.writeValueAsString(obj);
            }
            catch (JacksonException e) {
                LOGGER.error("json序列化出错：{}", obj, e);
                return null;
            }
        }
        return null;
    }

    /**
     * JSON 转 Bean.
     * @param json json
     * @param tClass class
     * @param <T> T类型
     * @return T类型
     */
    public static <T> T toBean(String json, Class<T> tClass) {
        try {
            return JSON_MAPPER.readValue(json, tClass);
        }
        catch (JacksonException e) {
            LOGGER.error("json解析出错：{}", json, e);
            return null;
        }
    }

    /**
     * InputStream 转 Bean
     * @param inputStream 流
     * @param tClass class
     * @param <T> T类型
     * @return T类型
     */
    public static <T> T toBean(InputStream inputStream, Class<T> tClass) {
        try {
            return JSON_MAPPER.readValue(inputStream, tClass);
        }
        catch (JacksonException e) {
            LOGGER.error("json解析出错：{}", inputStream.toString(), e);
            return null;
        }
    }

    /**
     * Object to JSON string byte array.
     * @param obj obj
     * @return JSON string byte array
     */
    public static byte[] toJsonBytes(Object obj) {
        try {
            return JSON_MAPPER.writeValueAsBytes(obj);
        }
        catch (JacksonException e) {
            LOGGER.error("json解析出错：{}", obj, e);
            return null;
        }
    }

    /**
     * JSON string deserialize to Object.
     * @param bytes JSON string byte array
     * @param tClass class of obj
     * @param <T> General type
     * @return T类型
     */
    public static <T> T toBean(byte[] bytes, Class<T> tClass) {
        try {
            return JSON_MAPPER.readValue(bytes, tClass);
        }
        catch (JacksonException e) {
            LOGGER.error("JSON解析出错：{}", new String(bytes), e);
            return null;
        }
    }

    /**
     * JSON string deserialize to Object.
     * @param json JSON string byte array
     * @param typeReference {@link TypeReference} of object
     * @param <T> General type
     * @return object
     */
    public static <T> T toBean(byte[] json, TypeReference<T> typeReference) {
        try {
            return JSON_MAPPER.readValue(json, typeReference);
        }
        catch (Exception e) {
            LOGGER.error("JSON解析出错：{}", new String(json), e);
            return null;
        }
    }

    /**
     * JSON string deserialize to Object.
     * @param json JSON string byte array
     * @param cls {@link Type} of object
     * @param <T> General type
     * @return object
     */
    public static <T> T toBean(byte[] json, Type cls) {
        try {
            return JSON_MAPPER.readValue(json, JSON_MAPPER.constructType(cls));
        }
        catch (Exception e) {
            LOGGER.error("JSON解析出错：{}", new String(json), e);
            return null;
        }
    }

    /**
     * JSON string deserialize to Object.
     * @param fromValue object
     * @param <T> General type
     * @return object
     */
    public static <T> T toBean(Object fromValue) {
        try {
            return JSON_MAPPER.convertValue(fromValue, new TypeReference<T>() {
            });
        }
        catch (Exception e) {
            LOGGER.error("JSON解析出错：{}", toString(fromValue), e);
            return null;
        }
    }

    /**
     * JSON string deserialize to Object.
     * @param fromValue object
     * @param cls {@link Type} of object
     * @param <T> General type
     * @return object
     */
    public static <T> T toBean(Object fromValue, Class<T> cls) {
        try {
            return JSON_MAPPER.convertValue(fromValue, cls);
        }
        catch (Exception e) {
            LOGGER.error("JSON解析出错：{}", toString(fromValue), e);
            return null;
        }
    }

    /**
     * JSON String deserialize to Object.
     * @param json json string
     * @param typeReference {@link TypeReference} of object
     * @param <T> General type
     * @return object
     */
    public static <T> T toObj(String json, TypeReference<T> typeReference) {
        try {
            return toObj(JSON_MAPPER.readTree(json), typeReference);
        }
        catch (JacksonException e) {
            LOGGER.error("json解析出错：{}", json, e);
            return null;
        }
    }

    /**
     * JSON node deserialize to Object.
     * @param jsonNode json node
     * @param typeReference {@link TypeReference} of object
     * @param <T> General type
     * @return object
     */
    public static <T> T toObj(JsonNode jsonNode, TypeReference<T> typeReference) {
        try {

            return JSON_MAPPER.readValue(jsonNode.traverse(JSON_MAPPER._deserializationContext()), typeReference);
        }
        catch (JacksonException e) {
            LOGGER.error("json解析出错：{}", jsonNode, e);
            return null;
        }
    }

    /**
     * JSON string deserialize to List.
     * @param json JSON string
     * @param eClass class
     * @param <E> E
     * @return E
     */
    public static <E> List<E> toList(String json, Class<E> eClass) {
        try {
            return JSON_MAPPER.readValue(json,
                    JSON_MAPPER.getTypeFactory().constructCollectionType(List.class, eClass));
        }
        catch (JacksonException e) {
            LOGGER.error("json解析出错：{}", json, e);
            return null;
        }
    }

    /**
     * JSON string deserialize to Map.
     * @param json JSON string
     * @param <T> T类型
     * @return T类型
     */
    public static <T> T toMap(String json) {
        try {
            return JSON_MAPPER.readValue(json, new TypeReference<>() {
            });
        }
        catch (JacksonException e) {
            LOGGER.error("json解析出错：{}", json, e);
            return null;
        }
    }

    /**
     * JSON string deserialize to Map.
     * @param json JSON string
     * @param kClass class
     * @param vClass class
     * @param <K> K
     * @param <V> V
     * @return Map
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return JSON_MAPPER.readValue(json,
                    JSON_MAPPER.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        }
        catch (JacksonException e) {
            LOGGER.error("json解析出错：{}", json, e);
            return null;
        }
    }

    /**
     * Register subtype for child class.
     * @param clz child class
     * @param type type name of child class
     */
    public static void registerSubtype(Class<?> clz, String type) {
        JSON_MAPPER.rebuild().registerSubtypes(new NamedType(clz, type)).build();
    }

    /**
     * Create a new empty Jackson {@link ObjectNode}.
     * @return {@link ObjectNode}
     */
    public static ObjectNode createEmptyJsonNode() {
        return new ObjectNode(JSON_MAPPER.getNodeFactory());
    }

    /**
     * Create a new empty Jackson {@link ArrayNode}.
     * @return {@link ArrayNode}
     */
    public static ArrayNode createEmptyArrayNode() {
        return new ArrayNode(JSON_MAPPER.getNodeFactory());
    }

    /**
     * Parse object to Jackson {@link JsonNode}.
     * @param obj object
     * @return {@link JsonNode}
     */
    public static JsonNode transferToJsonNode(Object obj) {
        return JSON_MAPPER.valueToTree(obj);
    }

    /**
     * construct java type -> Jackson Java Type.
     * @param type java type
     * @return JavaType {@link JavaType}
     */
    public static JavaType constructJavaType(Type type) {
        return JSON_MAPPER.constructType(type);
    }

    public static String pretty(Object o) {
        try {
            return JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        }
        catch (Exception e) {
            LOGGER.error("json解析出错：{}", o, e);
            return "";
        }
    }

    public static void prettyPrint(Object o) {
        try {
            String json = pretty(o);
            if (StrUtil.isNotBlank(json)) {
                System.out.println(json.replace("\r", ""));
            }
            else {
                LOGGER.error("json 为空：{}", o);
            }
        }
        catch (Exception e) {
            LOGGER.error("json解析出错：{}", o, e);
        }

    }

}
