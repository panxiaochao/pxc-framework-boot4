package io.github.panxiaochao.boot4.jackson.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.panxiaochao.boot4.utils.date.DatePattern;
import io.github.panxiaochao.boot4.utils.jackson.CustomizeJavaTimeModule;
import io.github.panxiaochao.boot4.utils.jackson.serializer.NullValueJacksonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <p>
 * Jackson3 自动化配置.
 * </p>
 * <pre>
 *     注册顺序：
 *     JacksonAutoConfiguration
 *     ObjectMapper
 *     JsonMapperBuilder
 *     JsonMapperBuilderCustomizer
 * </pre>
 *
 * <p>
 * doc: <a href=
 * "https://github.com/FasterXML/jackson/blob/main/jackson3/MIGRATING_TO_JACKSON_3.md">MIGRATING_TO_JACKSON_3.md</a>
 * </p>
 *
 * @author Lypxc
 * @since 2026-05-27
 */
@AutoConfiguration(before = JacksonAutoConfiguration.class)
public class Jackson3AutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(Jackson3AutoConfiguration.class);

    /**
     * <p>
     * To override the default ObjectMapper (and XmlMapper).
     * </p>
     * <pre>
     *     1.JsonMapperBuilderCustomizer 注册Bean
     *     2.生成Bean JsonMapperBuilder
     *     3.通过 JsonMapperBuilder 生成 ObjectMapper
     * </pre>
     * @return custom JsonMapperBuilderCustomizer
     */
    @Bean
    public JsonMapperBuilderCustomizer jsonMapperBuilderCustomizer() {
        LOGGER.info("配置[JsonMapper]成功！");
        //@formatter:off
        return jsonMapperBuilder ->
                jsonMapperBuilder.defaultLocale(Locale.CHINA)
                    // 所有字段全部展现
                    .changeDefaultPropertyInclusion(value -> JsonInclude.Value.ALL_ALWAYS)
                    .defaultTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))
                    .defaultDateFormat(new SimpleDateFormat(DatePattern.NORMAL_DATE_TIME_PATTERN))
                    // 忽略空Bean转json的错误
                    .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                    // 忽略未知属性
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    // 时间格式化处理
                    .addModule(new CustomizeJavaTimeModule())
                    // 空值处理
                    .serializerFactory(
                            jsonMapperBuilder.serializerFactory()
                                    .withNullValueSerializer(NullValueJacksonSerializer.INSTANCE)
                    );
        //@formatter:on
    }

}
