package io.github.panxiaochao.boot4.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 静态工具类
 * </p>
 *
 * @author Lypxc
 * @since 2022/8/25
 */
public enum Singleton {

	/**
	 * Inst singleton.
	 */
	INST;

	/**
	 * The Singles.
	 */
	private static final Map<String, Object> SINGLES = new ConcurrentHashMap<>();

	/**
	 * Single.
	 * @param key the String key
	 * @param o the o
	 */
	public void single(final String key, final Object o) {
		SINGLES.put(key, o);
	}

	/**
	 * Get t.
	 * @param <T> the type parameter
	 * @param key String key
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(final String key) {
		return (T) SINGLES.get(key);
	}

	/**
	 * 存储数量
	 * @return size
	 */
	public int count() {
		return SINGLES.size();
	}

}
