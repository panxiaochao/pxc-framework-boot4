package io.github.panxiaochao.boot4.crypto.keygen;

/**
 * <p>
 * 密钥生成器，每次只返回相同的密钥.
 * </p>
 *
 * @author Lypxc
 * @since 2024-07-25
 * @version 1.0
 */
public class SharedKeyGenerator implements BytesKeyGenerator {

	private final byte[] sharedKey;

	SharedKeyGenerator(int keySize) {
		this.sharedKey = new BytesKeySecureRandomGenerator(keySize).generateKey();
	}

	/**
	 * Get the length, in bytes, of keys created by this generator. Most unique keys are
	 * at least 16 bytes in length.
	 */
	@Override
	public int getKeySize() {
		return this.sharedKey.length;
	}

	/**
	 * Generate a new key.
	 */
	@Override
	public byte[] generateKey() {
		return this.sharedKey;
	}

}
