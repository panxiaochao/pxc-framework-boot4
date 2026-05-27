package io.github.panxiaochao.boot4.common.response.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 请求字段排序.
 * </p>
 *
 * @author Lypxc
 * @since 2022/4/7
 */
@Getter
@Setter
@Schema(description = "请求排序")
public class OrderItems implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 排序字段
	 */
	@Schema(description = "排序字段")
	private String column;

	/**
	 * 是否正序排列，默认 true
	 */
	@Schema(description = "是否正序排列，默认true")
	private boolean asc = true;

	private OrderItems() {
	}

	public OrderItems(String column, boolean asc) {
		this.column = column;
		this.asc = asc;
	}

	/**
	 * 以某个字段升序
	 * @param column 字段
	 * @return OrderItems
	 */
	public static OrderItems asc(String column) {
		return build(column, true);
	}

	/**
	 * 以某个字段降序
	 * @param column 字段
	 * @return OrderItems
	 */
	public static OrderItems desc(String column) {
		return build(column, false);
	}

	/**
	 * 以多个字段升序
	 * @param columns 字段
	 * @return OrderItems
	 */
	public static List<OrderItems> ascList(String... columns) {
		return Arrays.stream(columns).map(OrderItems::asc).collect(Collectors.toList());
	}

	/**
	 * 以多个字段降序
	 * @param columns 字段
	 * @return OrderItems
	 */
	public static List<OrderItems> descList(String... columns) {
		return Arrays.stream(columns).map(OrderItems::desc).collect(Collectors.toList());
	}

	/**
	 * 自主构建字段是否升序或降序
	 * @param column 字段
	 * @param asc 排序
	 * @return OrderItems
	 */
	private static OrderItems build(String column, boolean asc) {
		return new OrderItems(column, asc);
	}

}