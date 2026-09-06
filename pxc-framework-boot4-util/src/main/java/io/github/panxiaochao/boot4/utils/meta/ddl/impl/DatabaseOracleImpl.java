/*
 * Copyright © 2026-2027 Lypxc (545685602@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.panxiaochao.boot4.utils.meta.ddl.impl;

import io.github.panxiaochao.boot4.utils.StrUtil;
import io.github.panxiaochao.boot4.utils.meta.constants.DatabaseType;
import io.github.panxiaochao.boot4.utils.meta.db.ColumnMeta;
import io.github.panxiaochao.boot4.utils.meta.ddl.AbstractDatabase;
import io.github.panxiaochao.boot4.utils.meta.ddl.IDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Oracle 数据库实现类
 * </p>
 *
 * @author lypxc
 * @since 2025-06-13
 * @version 1.0
 */
public class DatabaseOracleImpl extends AbstractDatabase implements IDatabase {

    private static final String GET_DDL_TABLE_SQL = "SELECT DBMS_METADATA.GET_DDL('TABLE','%s','%s') FROM DUAL";

    private static final String GET_DDL_VIEW_SQL = "SELECT DBMS_METADATA.GET_DDL('VIEW','%s','%s') FROM DUAL";

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.ORACLE;
    }

    @Override
    public String getTableDdl(Connection connection, String schemaName, String tableName) {
        return getMetadataDdl(connection, schemaName, tableName, GET_DDL_TABLE_SQL);
    }

    @Override
    public String getViewDdl(Connection connection, String schemaName, String tableName) {
        return getMetadataDdl(connection, schemaName, tableName, GET_DDL_VIEW_SQL);
    }

    private String getMetadataDdl(Connection connection, String schemaName, String objectName, String sqlTemplate) {
        String sql = String.format(sqlTemplate, objectName, schemaName);
        try (Statement statement = connection.createStatement()) {
            if (statement.execute(sql)) {
                try (ResultSet resultSet = statement.getResultSet()) {
                    if (resultSet != null && resultSet.next()) {
                        return resultSet.getString(1);
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public String generateCreateTableSql(String schemaName, String tableName, String tableComment,
            List<ColumnMeta> columnMetas) {
        if (columnMetas == null || columnMetas.isEmpty()) {
            return "";
        }
        StringBuilder sql = new StringBuilder("CREATE TABLE ");
        sql.append(getQuotedSchemaTableCombination(schemaName, tableName)).append(" (\n");
        for (int i = 0; i < columnMetas.size(); i++) {
            if (i > 0) {
                sql.append(",\n");
            }
            sql.append("  ").append(reflectionFieldSqlFromColumnMeta(columnMetas.get(i)));
        }
        sql.append("\n)");
        return sql.toString();
    }

    public String reflectionFieldSqlFromColumnMeta(ColumnMeta column) {
        int type = column.getJdbcType();
        List<String> columnDdl = new ArrayList<>();
        columnDdl.add(quoteIdentifier(column.getColumnName()));
        columnDdl.add(super.getFieldDefineSqlFormJdbcType(column, type));
        if (!column.isNullable()) {
            columnDdl.add("NOT NULL");
        }
        if (column.isPrimaryKey()) {
            columnDdl.add("PRIMARY KEY");
        }
        appendDefaultValue(column, type, columnDdl);
        return String.join(" ", columnDdl);
    }

    private void appendDefaultValue(ColumnMeta column, int type, List<String> columnDdl) {
        String defaultValue = column.getColumnDefault();
        if (StrUtil.isBlank(defaultValue) || "null".equalsIgnoreCase(defaultValue)) {
            return;
        }
        if (type == Types.TIMESTAMP || type == Types.TIMESTAMP_WITH_TIMEZONE || type == Types.TIME
                || type == Types.DATE) {
            columnDdl.add("DEFAULT CURRENT_TIMESTAMP");
        }
        else if (defaultValue.startsWith("'")) {
            columnDdl.add("DEFAULT " + defaultValue);
        }
        else {
            columnDdl.add("DEFAULT '" + defaultValue.replace("'", "''") + "'");
        }
    }

    @Override
    public String getQuotedSchemaTableCombination(String schemaName, String tableName) {
        if (StrUtil.isBlank(schemaName)) {
            return quoteIdentifier(tableName);
        }
        return quoteIdentifier(schemaName) + "." + quoteIdentifier(tableName);
    }

    @Override
    public List<String> getTableColumnCommentDefinition(String schemaName, String tableName, String tableComment,
            List<ColumnMeta> columnMetas) {
        List<String> results = new ArrayList<>();
        String quotedTable = getQuotedSchemaTableCombination(schemaName, tableName);
        if (StrUtil.isNotBlank(tableComment)) {
            results.add("COMMENT ON TABLE " + quotedTable + " IS '" + escapeLiteral(tableComment) + "';");
        }
        if (columnMetas != null) {
            for (ColumnMeta column : columnMetas) {
                if (StrUtil.isNotBlank(column.getColumnComment())) {
                    results.add("COMMENT ON COLUMN " + quotedTable + "." + quoteIdentifier(column.getColumnName())
                            + " IS '" + escapeLiteral(column.getColumnComment()) + "';");
                }
            }
        }
        return results;
    }

    private String quoteIdentifier(String identifier) {
        return "\"" + identifier.replace("\"", "\"\"") + "\"";
    }

    private String escapeLiteral(String value) {
        return value.replace("'", "''");
    }

}
