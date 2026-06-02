package io.github.panxiaochao.boot4.web.filter;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 重新包装HttpServletRequest，解决是让其输入流可重复读.
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-26
 */
public class RequestWrapper extends HttpServletRequestWrapper {

    private static final Logger log = LoggerFactory.getLogger(RequestWrapper.class);

    /**
     * 存储body数据的容器
     */
    private final byte[] bodyBytes;

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * Constructs a request object wrapping the given request.
     * @param request The request to wrap
     * @param response The response to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public RequestWrapper(HttpServletRequest request, HttpServletResponse response) {
        super(request);
        request.setCharacterEncoding(DEFAULT_CHARSET);
        response.setCharacterEncoding(DEFAULT_CHARSET);

        bodyBytes = getBodyBytes(request);
    }

    /**
     * 获取请求Body
     * @param request request
     * @return String
     */
    public String getBodyString(final ServletRequest request) {
        try {
            return inputStream2String(request.getInputStream());
        }
        catch (IOException e) {
            log.error("getBodyString is error", e);
        }
        return null;
    }

    /**
     * 获取请求BodyBytes
     * @param request request
     * @return String
     */
    public byte[] getBodyBytes(final ServletRequest request) {
        try {
            return inputStream2String(request.getInputStream()).getBytes(DEFAULT_CHARSET);
        }
        catch (IOException e) {
            log.error("getBodyBytes is error", e);
        }
        return null;
    }

    /**
     * 获取请求Body
     * @return String
     */
    public String getBodyString() {
        return inputStream2String(new ByteArrayInputStream(bodyBytes));
    }

    /**
     * 将inputStream里的数据读取出来并转换成字符串
     * @param inputStream inputStream
     * @return String
     */
    private String inputStream2String(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, DEFAULT_CHARSET));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        catch (IOException e) {
            log.error("inputStream2String is error", e);
            throw new RuntimeException(e);
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    log.error("reader close is error", e);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 基于缓存的请求体构造字符读取器。
     * @return 可重复读取的字符流
     * @throws IOException IO 异常
     */
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), DEFAULT_CHARSET));
    }

    /**
     * 返回基于缓存请求体重新生成的输入流。
     * @return 可重复读取的输入流
     * @throws IOException IO 异常
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bodyBytes);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public int available() throws IOException {
                return bodyBytes.length;
            }

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }

}
