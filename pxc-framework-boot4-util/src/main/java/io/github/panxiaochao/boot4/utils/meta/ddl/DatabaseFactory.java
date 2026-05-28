// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package io.github.panxiaochao.boot4.utils.meta.ddl;

import io.github.panxiaochao.boot4.utils.meta.constants.DatabaseType;
import io.github.panxiaochao.boot4.utils.meta.ddl.impl.DatabaseDMImpl;
import io.github.panxiaochao.boot4.utils.meta.ddl.impl.DatabaseMySqlImpl;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * <p>
 * 数据库实例构建工厂类
 * </p>
 *
 * @author lypxc
 * @since 2025-06-17
 * @version 1.0
 */
public final class DatabaseFactory {

    private static final Map<DatabaseType, Callable<AbstractDatabase>> DATABASE_MAPPER = new HashMap<>() {
        @Serial
        private static final long serialVersionUID = 1L;

        {
            put(DatabaseType.MYSQL, DatabaseMySqlImpl::new);
            put(DatabaseType.DM, DatabaseDMImpl::new);
        }
    };

    public static AbstractDatabase getDatabaseInstance(DatabaseType type) {
        Callable<AbstractDatabase> callable = DATABASE_MAPPER.get(type);
        if (null != callable) {
            try {
                return callable.call();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        throw new UnsupportedOperationException(String.format("Unknown database type (%s)", type.name()));
    }

    private DatabaseFactory() {
        throw new IllegalStateException("Illegal access to DatabaseFactory constructor");
    }

}
