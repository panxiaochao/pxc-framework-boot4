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
package io.github.panxiaochao.boot4.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * <p>
 * 获取国际化语言 i18n 文件
 * </p>
 *
 * @author lypxc
 * @since 2026-02-24
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageSourceUtil {

    private static final MessageSource MESSAGE_SOURCE = SpringContextUtil.getBean(MessageSource.class);

    /**
     * 根据消息键和参数获取翻译值
     * @param code 消息键
     * @param args 参数
     * @return 获取国际化翻译值
     */
    public static String message(String code, Object... args) {
        try {
            if (ObjectUtil.isNotEmpty(MESSAGE_SOURCE)) {
                return MESSAGE_SOURCE.getMessage(code, args, LocaleContextHolder.getLocale());
            }
            return StrUtil.EMPTY;
        }
        catch (NoSuchMessageException e) {
            return code;
        }
    }

}
