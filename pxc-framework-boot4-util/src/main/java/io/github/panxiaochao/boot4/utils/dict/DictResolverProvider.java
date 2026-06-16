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
package io.github.panxiaochao.boot4.utils.dict;

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 字典服务提供类
 * </p>
 *
 * @author lypxc
 * @since 2026-01-16
 * @version 1.0
 */
public class DictResolverProvider {

    /**
     * LOGGER DictResolverProvider.class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DictResolverProvider.class);

    /**
     * 设置字典服务实现，volatile 确保线程安全
     */
    @Setter
    private static volatile IDictResolver dictResolver;

    /**
     * 获取当前字典服务
     * @return 字典服务
     */
    public static IDictResolver getDictResolver() {
        if (dictResolver == null) {
            synchronized (DictResolverProvider.class) {
                if (dictResolver == null) {
                    // 默认返回空实现，防止NPE
                    dictResolver = new DefaultDictResolver();
                    LOGGER.info("配置[Dict -> Default]成功！");
                }
            }
        }
        return dictResolver;
    }

}
