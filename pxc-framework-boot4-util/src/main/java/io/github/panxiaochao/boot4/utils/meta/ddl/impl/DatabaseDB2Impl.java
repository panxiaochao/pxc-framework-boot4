package io.github.panxiaochao.boot4.utils.meta.ddl.impl;

import io.github.panxiaochao.boot4.utils.meta.constants.DatabaseType;
import io.github.panxiaochao.boot4.utils.meta.db.ColumnMeta;
import io.github.panxiaochao.boot4.utils.meta.ddl.IDatabase;

import java.sql.Connection;
import java.util.List;

/**
 * <p>
 * 达梦 数据库实现类
 * </p>
 *
 * @author lypxc
 * @since 2025-06-13
 * @version 1.0
 */
public class DatabaseDB2Impl implements IDatabase {

	@Override
	public DatabaseType getDatabaseType() {
		return DatabaseType.DB2;
	}

	@Override
	public String getTableDdl(Connection connection, String schemaName, String tableName) {
		return "";
	}

	@Override
	public String getViewDdl(Connection connection, String schemaName, String tableName) {
		return "";
	}

	@Override
	public String generateCreateTableSql(String schemaName, String tableName, String tableComment,
			List<ColumnMeta> columnMetas) {
		return "";
	}

	@Override
	public String getQuotedSchemaTableCombination(String schemaName, String tableName) {
		return "";
	}

	@Override
	public List<String> getTableColumnCommentDefinition(String schemaName, String tableName, String tableComment,
			List<ColumnMeta> columnMetas) {
		return List.of();
	}

}
