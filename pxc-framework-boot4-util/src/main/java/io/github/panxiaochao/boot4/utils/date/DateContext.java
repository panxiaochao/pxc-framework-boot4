package io.github.panxiaochao.boot4.utils.date;

import java.text.SimpleDateFormat;

/**
 * <p>
 * new Date() 上下文，解决SimpleDateFormat多线程安全问题.
 * </p>
 *
 * @author Lypxc
 * @since 2022/4/12
 */
public class DateContext {

	private static final ThreadLocal<SimpleDateFormat> THREAD_LOCAL = new ThreadLocal<>();

	/**
	 * 设置数据
	 * @param simpleDateFormat 参数
	 */
	public static void set(SimpleDateFormat simpleDateFormat) {
		// set之前先remove，以免内存泄露，重复数据
		remove();
		THREAD_LOCAL.set(simpleDateFormat);
	}

	/**
	 * 获取当前数据
	 * @return String
	 */
	public static SimpleDateFormat get() {
		return THREAD_LOCAL.get();
	}

	/**
	 * 移除当前线程数据
	 */
	public static void remove() {
		THREAD_LOCAL.remove();
	}

}
