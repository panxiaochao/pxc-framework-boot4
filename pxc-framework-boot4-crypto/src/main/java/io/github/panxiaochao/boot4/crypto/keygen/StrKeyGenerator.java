package io.github.panxiaochao.boot4.crypto.keygen;

/**
 * <p>
 * 基于唯一字符串的密钥生成器.
 * </p>
 *
 * @author Lypxc
 * @since 2024-07-19
 * @version 1.0
 */
public interface StrKeyGenerator {

    /**
     * Generate a new key.
     */
    String generateKey();

}
