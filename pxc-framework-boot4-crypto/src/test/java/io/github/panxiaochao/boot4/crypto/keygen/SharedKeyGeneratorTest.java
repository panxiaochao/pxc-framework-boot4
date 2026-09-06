package io.github.panxiaochao.boot4.crypto.keygen;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * SharedKeyGenerator 测试
 * </p>
 *
 * @author Lypxc
 * @version 1.0
 */
public class SharedKeyGeneratorTest {

    @Test
    void generateKeyReturnsDefensiveCopy() {
        final SharedKeyGenerator generator = new SharedKeyGenerator(16);
        final byte[] first = generator.generateKey();
        final byte[] second = generator.generateKey();
        // 多次调用返回不同数组实例，但内容一致
        assertNotSame(first, second);
        assertTrue(Arrays.equals(first, second));
        // 修改返回副本不应影响内部密钥
        Arrays.fill(first, (byte) 0);
        final byte[] third = generator.generateKey();
        assertFalse(Arrays.equals(first, third));
    }

}