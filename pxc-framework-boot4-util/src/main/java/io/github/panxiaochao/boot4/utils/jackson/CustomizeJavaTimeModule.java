package io.github.panxiaochao.boot4.utils.jackson;

import io.github.panxiaochao.boot4.utils.date.DatePattern;
import io.github.panxiaochao.boot4.utils.jackson.jsonserializer.BigNumberSerializer;
import tools.jackson.core.json.PackageVersion;
import tools.jackson.databind.ext.javatime.deser.DurationDeserializer;
import tools.jackson.databind.ext.javatime.deser.InstantDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalDateDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalTimeDeserializer;
import tools.jackson.databind.ext.javatime.deser.YearDeserializer;
import tools.jackson.databind.ext.javatime.deser.YearMonthDeserializer;
import tools.jackson.databind.ext.javatime.ser.DurationSerializer;
import tools.jackson.databind.ext.javatime.ser.InstantSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalTimeSerializer;
import tools.jackson.databind.ext.javatime.ser.YearMonthSerializer;
import tools.jackson.databind.ext.javatime.ser.YearSerializer;
import tools.jackson.databind.module.SimpleModule;

import java.io.Serial;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;

/**
 * <p>
 * 自定义 Jackson TimeModule.
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-06
 */
public class CustomizeJavaTimeModule extends SimpleModule {

	@Serial
	private static final long serialVersionUID = 1L;

	public CustomizeJavaTimeModule() {
		super(PackageVersion.VERSION);
		// ====== Serialize ======
		// yyyy
		this.addSerializer(Year.class, new YearSerializer(DatePattern.NORMAL_YEAR_FORMATTER));
		// yyyy-MM
		this.addSerializer(YearMonth.class, new YearMonthSerializer(DatePattern.NORMAL_YEAR_MONTH_FORMATTER));
		// yyyy-MM-dd HH:mm:ss
		this.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DatePattern.NORMAL_DATE_TIME_FORMATTER));
		// yyyy-MM-dd
		this.addSerializer(LocalDate.class, new LocalDateSerializer(DatePattern.NORMAL_DATE_FORMATTER));
		// HH:mm:ss
		this.addSerializer(LocalTime.class, new LocalTimeSerializer(DatePattern.NORMAL_TIME_FORMATTER));
		// Instant 时间戳
		this.addSerializer(Instant.class, InstantSerializer.INSTANCE);
		// Duration 类型
		this.addSerializer(Duration.class, DurationSerializer.INSTANCE);
		// 数值型
		this.addSerializer(Long.class, BigNumberSerializer.INSTANCE);
		this.addSerializer(Long.TYPE, BigNumberSerializer.INSTANCE);
		this.addSerializer(BigInteger.class, BigNumberSerializer.INSTANCE);
		this.addSerializer(BigDecimal.class, BigNumberSerializer.INSTANCE);

		// ====== Deserialize ======
		// yyyy
		this.addDeserializer(Year.class, new YearDeserializer(DatePattern.NORMAL_YEAR_FORMATTER));
		// yyyy-MM
		this.addDeserializer(YearMonth.class, new YearMonthDeserializer(DatePattern.NORMAL_YEAR_MONTH_FORMATTER));
		// yyyy-MM-dd HH:mm:ss
		this.addDeserializer(LocalDateTime.class,
				new LocalDateTimeDeserializer(DatePattern.NORMAL_DATE_TIME_FORMATTER));
		// yyyy-MM-dd
		this.addDeserializer(LocalDate.class, new LocalDateDeserializer(DatePattern.NORMAL_DATE_FORMATTER));
		// HH:mm:ss
		this.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DatePattern.NORMAL_TIME_FORMATTER));
		// Instant 时间戳
		this.addDeserializer(Instant.class, InstantDeserializer.INSTANT);
		// Duration 类型
		this.addDeserializer(Duration.class, DurationDeserializer.INSTANCE);
	}

}
