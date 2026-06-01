package io.github.panxiaochao.boot4.holiday.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Holiday 实体
 * </p>
 *
 * @author Lypxc
 * @since 2024-04-02
 * @version 1.0
 */
@Getter
@Setter
public class Holiday {

    /**
     * 年份
     */
    private String year;

    /**
     * 节假日期集合
     */
    private List<Days> days = new ArrayList<>();

    /**
     * 工作补班日期集合
     */
    private List<WorkDays> workdays = new ArrayList<>();

    /**
     * 节假日期详情
     */
    @Setter
    @Getter
    public static class Days {

        /**
         * 节日名称
         */
        private String name;

        /**
         * 日期
         */
        private String date;

    }

    /**
     * 工作补班日期详情
     */
    @Setter
    @Getter
    public static class WorkDays {

        /**
         * 日期
         */
        private String date;

    }

}
