package io.github.panxiaochao.boot4.utils.meta.ddl.impl;

import io.github.panxiaochao.boot4.utils.meta.db.ColumnMeta;
import org.junit.jupiter.api.Test;

import java.sql.Types;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseOracleImplTest {

    @Test
    void generateCreateTableSql() {
        ColumnMeta id = new ColumnMeta();
        id.setColumnName("id");
        id.setJdbcType(Types.NUMERIC);
        id.setJdbcTypeName("NUMBER");
        id.setColumnLength(10);
        id.setNullable(false);
        id.setPrimaryKey(true);

        ColumnMeta name = new ColumnMeta();
        name.setColumnName("name");
        name.setJdbcType(Types.VARCHAR);
        name.setJdbcTypeName("VARCHAR2");
        name.setColumnLength(50);
        name.setNullable(true);
        name.setColumnDefault("guest");
        name.setColumnComment("用户's名称");

        DatabaseOracleImpl database = new DatabaseOracleImpl();
        String sql = database.generateCreateTableSql("APP", "users", "用户's表", List.of(id, name));

        assertTrue(sql.contains("CREATE TABLE \"APP\".\"users\""));
        assertTrue(sql.contains("\"id\" NUMBER NOT NULL PRIMARY KEY"));
        assertTrue(sql.contains("\"name\" VARCHAR2(50) DEFAULT 'guest'"));
        assertEquals(
                List.of("COMMENT ON TABLE \"APP\".\"users\" IS '用户''s表';",
                        "COMMENT ON COLUMN \"APP\".\"users\".\"name\" IS '用户''s名称';"),
                database.getTableColumnCommentDefinition("APP", "users", "用户's表", List.of(id, name)));
    }

    @Test
    void quoteIdentifier() {
        DatabaseOracleImpl database = new DatabaseOracleImpl();
        assertEquals("\"APP\".\"user\"\"data\"", database.getQuotedSchemaTableCombination("APP", "user\"data"));
        assertEquals("\"users\"", database.getQuotedSchemaTableCombination(null, "users"));
    }

}
