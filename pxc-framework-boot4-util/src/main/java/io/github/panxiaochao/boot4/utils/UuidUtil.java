package io.github.panxiaochao.boot4.utils;

import java.util.UUID;

/**
 * <p>
 * UUID 生成.
 * </p>
 *
 * @author Lypxc
 * @since 2023-03-16
 */
public class UuidUtil {

	/**
	 * 获取原生UUID
	 * @return return UUID
	 */
	public static String getUuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 获取原生UUID，去除-的简化UUID.
	 * @return return simple UUID
	 */
	public static String getSimpleUuid() {
		return getUuid().replaceAll("-", "");
	}

}
