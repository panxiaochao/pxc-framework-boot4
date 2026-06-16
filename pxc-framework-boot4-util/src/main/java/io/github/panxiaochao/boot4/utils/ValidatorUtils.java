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

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * <p>
 * Validator 检验工具类.
 * </p>
 *
 * @author lypxc
 * @since 2026-02-25
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorUtils {

    private static final Validator VALID = SpringContextUtil.getBean(Validator.class);

    /**
     * 对给定对象进行参数校验，并根据指定的校验组进行校验
     * @param object 要进行校验的对象
     * @param groups 校验组
     * @throws ConstraintViolationException 如果校验不通过，则抛出参数校验异常
     */
    public static <T> void validate(T object, Class<?>... groups) {
        if (VALID != null) {
            Set<ConstraintViolation<T>> validate = VALID.validate(object, groups);
            if (!validate.isEmpty()) {
                throw new ConstraintViolationException("参数校验异常", validate);
            }
        }
        else {
            throw new ConstraintViolationException("Validator 没有初始化", null);
        }
    }

}
