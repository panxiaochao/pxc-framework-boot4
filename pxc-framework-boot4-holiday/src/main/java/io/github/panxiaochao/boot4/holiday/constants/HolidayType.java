package io.github.panxiaochao.boot4.holiday.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 日期类型，工作日-0, 工休日（休息日）-1, 节假日-2
 * </p>
 *
 * @author Lypxc
 * @since 2024-04-02
 * @version 1.0
 */
@AllArgsConstructor
@Getter
public enum HolidayType {

    /**
     * 工作日
     */
    WEEKDAY(0, "工作日"),
    /**
     * 公休日（休息日）
     */
    PUBLIC_HOLIDAY(1, "公休日"),
    /**
     * 节假日
     */
    HOLIDAY(2, "节假日");

    public final int type;

    public final String name;

    /**
     * 根据type返回日期类型
     * @param type 值
     * @return 日期类型
     */
    public HolidayType ofType(int type) {
        for (HolidayType holidayType : values()) {
            if (holidayType.type == type) {
                return holidayType;
            }
        }
        throw new IllegalArgumentException("Invalid type: " + type);
    }

}
