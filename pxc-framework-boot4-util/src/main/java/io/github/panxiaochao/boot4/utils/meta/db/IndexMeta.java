package io.github.panxiaochao.boot4.utils.meta.db;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * 数据库-索引元数据
 * </p>
 *
 * @author Lypxc
 * @since 2024-05-07
 * @version 1.0
 */
@Getter
@Setter
public class IndexMeta implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 数据库 表名
	 */
	private String tableName;

	/**
	 * 索引值 是否可以不唯一
	 */
	private boolean nonUnique;

	/**
	 * 索引 名称
	 */
	private String indexName;

	/**
	 * 索引 字段
	 */
	private String columnName;

	@Override
	public int hashCode() {
		return Objects.hash(indexName, tableName);
	}

}
