package io.github.panxiaochao.boot4.crypto.keygen;

import java.util.Base64;

/**
 * <p>
 * 基于唯一字符串的Base64密钥生成器.
 * </p>
 *
 * @author Lypxc
 * @since 2024-07-19
 * @version 1.0
 */
public class StrKeyBase64Generator implements StrKeyGenerator {

    private static final int DEFAULT_KEY_SIZE = 32;

    private final Base64.Encoder encoder;

    private final BytesKeyGenerator keyGenerator;

    /**
     * Creates an instance with keySize of 32 bytes and standard Base64 encoding.
     */
    public StrKeyBase64Generator() {
        this(DEFAULT_KEY_SIZE);
    }

    /**
     * Creates an instance with the provided key length in bytes and standard Base64
     * encoding.
     * @param keySize the key length in bytes
     */
    public StrKeyBase64Generator(int keySize) {
        this(Base64.getEncoder(), keySize);
    }

    /**
     * Creates an instance with keySize of 32 bytes and the provided encoder.
     * @param encoder the encoder to use
     */
    public StrKeyBase64Generator(Base64.Encoder encoder) {
        this(encoder, DEFAULT_KEY_SIZE);
    }

    /**
     * Creates an instance with the provided key length and encoder.
     * @param encoder the encoder to use
     * @param keySize the key length to use
     */
    public StrKeyBase64Generator(Base64.Encoder encoder, int keySize) {
        if (encoder == null) {
            throw new IllegalArgumentException("encode cannot be null");
        }
        if (keySize < DEFAULT_KEY_SIZE) {
            throw new IllegalArgumentException("keySize must be greater than or equal to" + DEFAULT_KEY_SIZE);
        }
        this.encoder = encoder;
        this.keyGenerator = new BytesKeySecureRandomGenerator(keySize);
    }

    @Override
    public String generateKey() {
        byte[] key = this.keyGenerator.generateKey();
        return this.encoder.encodeToString(key);
    }

}
