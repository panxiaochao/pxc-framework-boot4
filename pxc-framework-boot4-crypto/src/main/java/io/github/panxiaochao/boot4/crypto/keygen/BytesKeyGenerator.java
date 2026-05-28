package io.github.panxiaochao.boot4.crypto.keygen;

/**
 * <p>
 * 基于唯一字节数组的密钥生成器.
 * </p>
 *
 * @author Lypxc
 * @since 2024-07-23
 * @version 1.0
 */
public interface BytesKeyGenerator {

    /**
     * Get the length, in bytes, of keys created by this generator. Most unique keys are
     * at least 16 bytes in length.
     */
    int getKeySize();

    /**
     * Generate a new key.
     */
    byte[] generateKey();

}
