package io.github.panxiaochao.boot4.utils.meta.ddl.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * <p>
 * 校验尚未实现的数据库 DDL 生成在调用时显式抛出异常，避免静默返回空导致数据丢失
 * </p>
 *
 * @author Lypxc
 * @version 1.0
 */
public class DatabaseUnsupportedImplTest {

    @Test
    void postgresUnsupported() {
        final DatabasePostgresSqlImpl impl = new DatabasePostgresSqlImpl();
        assertThrows(UnsupportedOperationException.class, () -> impl.getTableDdl(null, "s", "t"));
        assertThrows(UnsupportedOperationException.class, () -> impl.getViewDdl(null, "s", "t"));
        assertThrows(UnsupportedOperationException.class, () -> impl.generateCreateTableSql("s", "t", "c", null));
        assertThrows(UnsupportedOperationException.class, () -> impl.getQuotedSchemaTableCombination("s", "t"));
        assertThrows(UnsupportedOperationException.class,
                () -> impl.getTableColumnCommentDefinition("s", "t", "c", null));
    }

    @Test
    void db2Unsupported() {
        final DatabaseDB2Impl impl = new DatabaseDB2Impl();
        assertThrows(UnsupportedOperationException.class, () -> impl.getTableDdl(null, "s", "t"));
        assertThrows(UnsupportedOperationException.class, () -> impl.getViewDdl(null, "s", "t"));
        assertThrows(UnsupportedOperationException.class, () -> impl.generateCreateTableSql("s", "t", "c", null));
        assertThrows(UnsupportedOperationException.class, () -> impl.getQuotedSchemaTableCombination("s", "t"));
        assertThrows(UnsupportedOperationException.class,
                () -> impl.getTableColumnCommentDefinition("s", "t", "c", null));
    }

}