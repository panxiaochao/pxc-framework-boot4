package io.github.panxiaochao.boot4.utils.test;

import io.github.panxiaochao.boot4.utils.QRCodeUtil;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * QRCodeUtil 读取原始字节测试
 * </p>
 *
 * @author Lypxc
 * @version 1.0
 */
public class QRCodeUtilTest {

    @Test
    void readRawBytesFromBufferedImage() {
        final BufferedImage image = QRCodeUtil.build("hello-qrcode").toImage();
        final byte[] raw = QRCodeUtil.readRawBytes(image);
        assertNotNull(raw);
        assertTrue(raw.length > 0);
    }

    @Test
    void readRawBytesFromFile() throws Exception {
        final Path tempDir = Files.createTempDirectory("qrcode-test");
        final File qrCodeFile = new File(tempDir.toFile(), "test.png");
        QRCodeUtil.build("hello-qrcode").toFile(qrCodeFile);
        final byte[] raw = QRCodeUtil.readRawBytes(qrCodeFile);
        assertNotNull(raw);
        assertTrue(raw.length > 0);
    }

}