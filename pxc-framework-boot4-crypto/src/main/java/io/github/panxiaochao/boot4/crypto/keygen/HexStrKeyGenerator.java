package io.github.panxiaochao.boot4.crypto.keygen;

import io.github.panxiaochao.boot4.crypto.utils.HexUtil;

/**
 * <p>
 * 生成十六进制密钥，委托{@link BytesKeyGenerator}进行实际密钥生成.
 * </p>
 *
 * @author Lypxc
 * @since 2024-07-24
 * @version 1.0
 */
public class HexStrKeyGenerator implements StrKeyGenerator {

    private final BytesKeyGenerator keyGenerator;

    HexStrKeyGenerator(BytesKeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    /**
     * Generate a new key.
     */
    @Override
    public String generateKey() {
        return HexUtil.encode(this.keyGenerator.generateKey());
    }

}
