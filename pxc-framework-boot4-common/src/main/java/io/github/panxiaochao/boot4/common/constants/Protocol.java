package io.github.panxiaochao.boot4.common.constants;

import lombok.Getter;

/**
 * <p>
 * 协议枚举.
 * </p>
 *
 * @author Lypxc
 * @since 2023-11-14
 */
@Getter
public enum Protocol {

	/**
	 * http 协议类型
	 */
	HTTP("http://", "http"),
	/**
	 * https 协议类型
	 */
	HTTPS("https://", "https");

	private final String format;

	private final String prefix;

	Protocol(String format, String prefix) {
		this.format = format;
		this.prefix = prefix;
	}

}
