package io.github.panxiaochao.boot4.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import io.github.panxiaochao.boot4.redis.config.properties.Boot4RedissonProperties;
import io.github.panxiaochao.boot4.redis.mapper.KeyPrefixNameMapper;
import io.github.panxiaochao.boot4.utils.date.DatePattern;
import io.github.panxiaochao.boot4.utils.jackson.CustomizeJavaTimeModule;
import io.github.panxiaochao.boot4.utils.jackson.serializer.NullValueJacksonSerializer;
import lombok.RequiredArgsConstructor;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.CompositeCodec;
import org.redisson.codec.JsonJackson3Codec;
import org.redisson.codec.TypedJsonJackson3Codec;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.ser.BeanSerializerFactory;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <p>
 * Redisson 自动配置类 3表示大版本号
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-27
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties({ Boot4RedissonProperties.class })
@ConditionalOnWebApplication
public class Boot4RedissonAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(Boot4RedissonAutoConfiguration.class);

    private final Boot4RedissonProperties redissonProperties;

    /**
     * 自定义 Redisson 配置
     * @return RedissonAutoConfigurationCustomizer
     */
    @Bean
    public RedissonAutoConfigurationCustomizer redissonAutoConfigurationCustomizers() {
        return config -> {
            // 序列化模式
            Codec codec = buildCodec();
            // 组合序列化 key 使用 String 内容使用通用 json 格式
            config.setCodec(new CompositeCodec(StringCodec.INSTANCE, codec, codec));
            config.setThreads(redissonProperties.getThreads());
            config.setNettyThreads(redissonProperties.getNettyThreads());
            // 缓存 Lua 脚本 减少网络传输(redisson 大部分的功能都是基于 Lua 脚本实现)
            config.setUseScriptCache(true);
            // 自定义 Key 前缀
            config.setNameMapper(new KeyPrefixNameMapper(redissonProperties.getKeyPrefix()));
            LOGGER.info("配置[Redis -> Redisson]成功！");
        };
    }

    /**
     * 构建 Redisson 编码器, 根据配置是否开启类编码器
     * @return Redisson 编码器
     */
    private Codec buildCodec() {
        JsonMapper.Builder builder = objectMapper();
        if (redissonProperties.isClassCodecEnable()) {
            // DefaultTyping.NON_FINAL 针对非 final 类型的数据会添加 @class 字段，比如：Date、List、Map 等
            builder.activateDefaultTyping(
                    BasicPolymorphicTypeValidator.builder().allowIfSubType((ctxt, clazz) -> true).build(),
                    DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
            return new JsonJackson3Codec(builder.build());
        }
        else {
            // 自定义 NULL 值处理，不适合序列化 @class 方式，[]通过不了序列化
            builder.serializerFactory(
                    BeanSerializerFactory.instance.withNullValueSerializer(NullValueJacksonSerializer.INSTANCE));
        }
        return new TypedJsonJackson3Codec(Object.class, builder.build());
    }

    /**
     * 创建并配置RedisTemplate模板 用于Redis操作，支持泛型
     * @param redisConnectionFactory Redis连接工厂，用于创建Redis连接
     * @return 返回RedisTemplate模板实例
     */
    @Bean(name = "redisTemplate")
    public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, T> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        // 配置 JsonMapper
        JsonMapper jsonMapper = objectMapper().build();
        JacksonJsonRedisSerializer<Object> jacksonJsonRedisSerializer = new JacksonJsonRedisSerializer<>(jsonMapper,
                Object.class);
        // 使用 StringRedisSerializer 来序列化和反序列化redis的key值
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        // 使用 JacksonJsonRedisSerializer 序列化VALUE
        template.setValueSerializer(jacksonJsonRedisSerializer);
        template.setHashValueSerializer(jacksonJsonRedisSerializer);
        // afterPropertiesSet
        template.afterPropertiesSet();
        LOGGER.info("配置[Redis -> RedisTemplate]成功！");
        return template;
    }

    private JsonMapper.Builder objectMapper() {
        return JsonMapper.builder()
            // 设置默认区域为中文
            .defaultLocale(Locale.CHINA)
            // 设置默认时区为上海时区
            .defaultTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))
            // 全局值全部可见，即便值为null也会被序列化
            .changeDefaultPropertyInclusion(value -> JsonInclude.Value.ALL_ALWAYS)
            // 全局控制字段可见性规则，ANY表示所有字段都可见
            .changeDefaultVisibility(value -> value.withVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY))
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
        // 校验对象：子类型（即 JSON 中 @class 指定的实际类型）
        // 含义：对所有子类型都放行，不做任何限制
        // 效果：反序列化时，无论 JSON 中声明的实际类型是什么，都会被接受
        // 安全风险：等同于完全放开多态反序列化，存在反序列化漏洞风险（恶意构造的 JSON 可能实例化危险类）
        // allowIfBaseType(Object.class) 只有当代码中声明的类型是 Object.class 时，才允许使用多态类型信息
        // 因为安全策略，LaissezFaireSubTypeValidator 在 Jackson 3.X 中被禁止外部引用
        // --- ⭐️ 关键新增：激活默认类型信息 ---
        // NON_FINAL: 对非 final 类添加 @class 字段（最常用）
        // WRAPPER_ARRAY: 将类型信息包装为数组 ["com.example.User", {...}]
        // WRAPPER_OBJECT: 将类型信息包装为对象 {"com.example.User": {...}}
        // .activateDefaultTyping(
        // BasicPolymorphicTypeValidator.builder().allowIfSubType((ctxt, clazz) ->
        // true).build(),
        // DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
        ;
    }

}
