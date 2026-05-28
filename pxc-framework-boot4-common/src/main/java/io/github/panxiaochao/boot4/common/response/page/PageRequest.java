package io.github.panxiaochao.boot4.common.response.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * 请求分页参数.
 * </p>
 *
 * @author Lypxc
 * @since 2021/12/3 17:51
 */
@Getter
@Setter
@Schema(description = "请求分页参数")
public class PageRequest {

    /**
     * 页号
     */
    @Schema(description = "页码，不小于1")
    private long pageNo = 1;

    /**
     * 页数
     */
    @Schema(description = "页数")
    private long pageSize = 10;

    /**
     * 是否查询总数
     */
    @Schema(description = "页码")
    private boolean searchCount = true;

    /**
     * 排序字段
     */
    @Schema(description = "排序字段")
    private List<OrderItems> orders;

    /**
     * 转换为分页参数
     * @return Pagination
     */
    @Schema(description = "转化分页对象", hidden = true)
    public Pagination toPagination() {
        return new Pagination(this.pageNo, this.pageSize);
    }

}