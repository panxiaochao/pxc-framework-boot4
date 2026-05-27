package io.github.panxiaochao.boot4.utils.meta.ddl;

import io.github.panxiaochao.boot4.utils.meta.constants.DatabaseType;
import io.github.panxiaochao.boot4.utils.meta.db.ColumnMeta;

import java.sql.Connection;
import java.util.List;

/**
 * <p>
 * 数据库 元接口
 * </p>
 *
 * @author lypxc
 * @since 2025-06-13
 * @version 1.0
 */
public interface IDatabase {

	/**
	 * 获取数据库类型
	 * @return 数据库类型
	 */
	DatabaseType getDatabaseType();

	/**
	 * 获取指定物理表的DDL语句
	 * @param connection JDBC连接
	 * @param schemaName 模式名称
	 * @param tableName 表名称
	 * @return 字段元信息列表
	 */
	String getTableDdl(Connection connection, String schemaName, String tableName);

	/**
	 * 获取指定视图表的DDL语句
	 * @param connection JDBC连接
	 * @param schemaName 模式名称
	 * @param tableName 表或视图名称
	 * @return 字段元信息列表
	 */
	String getViewDdl(Connection connection, String schemaName, String tableName);

	/**
	 * 生成创建表 DDL
	 * @return 创建表 DDL-SQL
	 */
	String generateCreateTableSql(String schemaName, String tableName, String tableComment,
			List<ColumnMeta> columnMetas);

	/**
	 * 获取数据库的表全名
	 * @param schemaName 模式名称
	 * @param tableName 表名称
	 * @return 表全名
	 */
	String getQuotedSchemaTableCombination(String schemaName, String tableName);

	/**
	 * 获取表字段注释定义
	 * @param schemaName 模式名称
	 * @param tableName 表名称
	 * @param tableComment 表注释
	 * @param columnMetas 字段元信息列表
	 * @return 表字段注释定义
	 */
	List<String> getTableColumnCommentDefinition(String schemaName, String tableName, String tableComment,
			List<ColumnMeta> columnMetas);

}
