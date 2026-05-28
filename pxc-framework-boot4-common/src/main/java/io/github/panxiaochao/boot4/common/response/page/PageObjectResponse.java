package io.github.panxiaochao.boot4.common.response.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * 分页对象响应实体.
 * </p>
 *
 * @author Lypxc
 * @since 2023-01-03
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分页对象响应")
public class PageObjectResponse<T> {

    /**
     * 分页信息
     */
    @Schema(description = "分页信息")
    private Pagination pagination;

    /**
     * 对象数据
     */
    @Schema(description = "对象数据")
    private T data;

}