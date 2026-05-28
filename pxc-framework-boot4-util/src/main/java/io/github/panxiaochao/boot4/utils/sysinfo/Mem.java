package io.github.panxiaochao.boot4.utils.sysinfo;

import io.github.panxiaochao.boot4.utils.ArithmeticUtil;
import io.github.panxiaochao.boot4.utils.unit.DataOfSize;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * Mem Entity
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-07
 */
@Setter
@ToString
public class Mem {

    /**
     * 内存总量, 单位MB
     */
    private long total;

    /**
     * 已用内存, 单位MB
     */
    private long used;

    /**
     * 剩余内存, 单位MB
     */
    private long free;

    public long getTotal() {
        return DataOfSize.ofBytes(total).toMegabytes();
    }

    public long getUsed() {
        return DataOfSize.ofBytes(used).toMegabytes();
    }

    public long getFree() {
        return DataOfSize.ofBytes(free).toMegabytes();
    }

    public double getUsage() {
        return ArithmeticUtil.mul(ArithmeticUtil.div(String.valueOf(used), String.valueOf(total), 4), "100")
            .doubleValue();
    }

}
