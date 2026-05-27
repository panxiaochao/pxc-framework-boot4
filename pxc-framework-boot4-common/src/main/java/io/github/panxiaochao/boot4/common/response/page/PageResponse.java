package io.github.panxiaochao.boot4.common.response.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * 分页数组响应实体.
 * </p>
 *
 * @author Lypxc
 * @since 2023-01-03
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分页响应")
public class PageResponse<T> {

	/**
	 * 分页信息
	 */
	@Schema(description = "分页信息")
	private Pagination pagination;

	/**
	 * 数组数据
	 */
	@Schema(description = "数组数据")
	private List<T> list;

}