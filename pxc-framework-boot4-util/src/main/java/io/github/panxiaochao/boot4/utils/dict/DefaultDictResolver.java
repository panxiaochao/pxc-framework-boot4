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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 默认字典服务实现类，使用 ConcurrentHashMap 内存模式，实现了{@link IDictResolver}接口，提供字典操作的基本方法
 * </p>
 *
 * @author lypxc
 * @since 2026-01-16
 * @version 1.0
 */
public class DefaultDictResolver extends AbstractDictResolver {

    private final ConcurrentHashMap<String, Map<String, String>> CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();

    @Override
    public Map<String, String> getAllDictByDictCode(String dictCode) {
        String dictCacheKey = CACHE_KEY_PREFIX + dictCode;
        Map<String, String> dictMap = CONCURRENT_HASH_MAP.getOrDefault(dictCacheKey, Map.of());
        return dictMap.isEmpty() ? Map.of() : dictMap;
    }

    @Override
    public void loadAllDict(Map<String, Map<String, String>> dictAllMap) {
        dictAllMap.forEach((dictCode, map) -> CONCURRENT_HASH_MAP.put(CACHE_KEY_PREFIX + dictCode, map));
    }

    @Override
    public void loadDict(String dictCode, Map<String, String> dictMap) {
        CONCURRENT_HASH_MAP.put(CACHE_KEY_PREFIX + dictCode, dictMap);
    }

    @Override
    public void clearAllDict() {
        CONCURRENT_HASH_MAP.clear();
    }

}
