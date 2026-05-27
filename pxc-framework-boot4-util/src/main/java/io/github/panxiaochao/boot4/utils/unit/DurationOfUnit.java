package io.github.panxiaochao.boot4.utils.unit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;

/**
 * <p>
 * A standard set of {@link DurationOfUnit} units.
 * </p>
 *
 * @author Lypxc
 * @since 2023-08-01
 */
public enum DurationOfUnit {

	/**
	 * Nanoseconds.
	 */
	NANOS(ChronoUnit.NANOS, "ns", Duration::toNanos),

	/**
	 * Microseconds.
	 */
	MICROS(ChronoUnit.MICROS, "us", (duration) -> duration.toNanos() / 1000L),

	/**
	 * Milliseconds.
	 */
	MILLIS(ChronoUnit.MILLIS, "ms", Duration::toMillis),

	/**
	 * Seconds.
	 */
	SECONDS(ChronoUnit.SECONDS, "s", Duration::getSeconds),

	/**
	 * Minutes.
	 */
	MINUTES(ChronoUnit.MINUTES, "m", Duration::toMinutes),

	/**
	 * Hours.
	 */
	HOURS(ChronoUnit.HOURS, "h", Duration::toHours),

	/**
	 * Days.
	 */
	DAYS(ChronoUnit.DAYS, "d", Duration::toDays);

	private final ChronoUnit chronoUnit;

	private final String suffix;

	private final Function<Duration, Long> longValue;

	DurationOfUnit(ChronoUnit chronoUnit, String suffix, Function<Duration, Long> toUnit) {
		this.chronoUnit = chronoUnit;
		this.suffix = suffix;
		this.longValue = toUnit;
	}

	public Duration parse(String value) {
		return Duration.of(Long.parseLong(value), this.chronoUnit);
	}

	public String print(Duration value) {
		return longValue(value) + this.suffix;
	}

	public long longValue(Duration value) {
		return this.longValue.apply(value);
	}

	public static DurationOfUnit fromChronoUnit(ChronoUnit chronoUnit) {
		if (chronoUnit == null) {
			return DurationOfUnit.MILLIS;
		}
		for (DurationOfUnit candidate : values()) {
			if (candidate.chronoUnit == chronoUnit) {
				return candidate;
			}
		}
		throw new IllegalArgumentException("Unknown unit " + chronoUnit);
	}

	public static DurationOfUnit fromSuffix(String suffix) {
		for (DurationOfUnit candidate : values()) {
			if (candidate.suffix.equalsIgnoreCase(suffix)) {
				return candidate;
			}
		}
		throw new IllegalArgumentException("Unknown unit '" + suffix + "'");
	}

}
