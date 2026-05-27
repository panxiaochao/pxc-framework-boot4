package io.github.panxiaochao.boot4.utils.sysinfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * DiskInfo Entity
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-07
 */
@Getter
@Setter
@ToString
public class DiskInfo {

	/**
	 * 盘符路径
	 */
	private String dirName;

	/**
	 * 盘符类型
	 */
	private String sysTypeName;

	/**
	 * 文件类型
	 */
	private String typeName;

	/**
	 * 总大小
	 */
	private double total;

	/**
	 * 剩余大小
	 */
	private double free;

	/**
	 * 已经使用量
	 */
	private double used;

	/**
	 * 资源的使用率
	 */
	private double usage;

}
