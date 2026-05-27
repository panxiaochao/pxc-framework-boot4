package io.github.panxiaochao.boot4.crypto.encrypt;

/**
 * <p>
 * 对称数据加密服务接口.
 * </p>
 *
 * @author Lypxc
 * @since 2024-07-24
 * @version 1.0
 */
public interface BytesEncryptor {

	/**
	 * Encrypt the byte array.
	 */
	byte[] encrypt(byte[] byteArray);

	/**
	 * Decrypt the byte array.
	 */
	byte[] decrypt(byte[] encryptedBytes);

}
