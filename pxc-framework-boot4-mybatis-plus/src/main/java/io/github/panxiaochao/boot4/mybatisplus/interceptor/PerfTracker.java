package io.github.panxiaochao.boot4.mybatisplus.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 慢查询性能追踪器
 * </p>
 *
 * @author lypxc
 * @since 2025-06-05
 * @version 1.0
 */
public class PerfTracker {

    /**
     * LOGGER PerfTracker.class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PerfTracker.class);

    private final long startTime;

    private final String methodName;

    private final Map<String, String> resultMap = new HashMap<>();

    private PerfTracker() {
        this.startTime = System.currentTimeMillis();
        this.methodName = null;
    }

    private PerfTracker(String methodName) {
        this.startTime = System.currentTimeMillis();
        this.methodName = methodName;
    }

    private PerfTracker(String methodName, Map<String, String> resultMap) {
        this.startTime = System.currentTimeMillis();
        this.methodName = methodName;
        this.resultMap.putAll(resultMap);
    }

    public static TimerContext start() {
        return new TimerContext(Thread.currentThread().getStackTrace()[2].getMethodName(), null);
    }

    public static TimerContext start(Map<String, String> resultMap) {
        return new TimerContext(Thread.currentThread().getStackTrace()[2].getMethodName(), resultMap);
    }

    /**
     * 时间追踪器上下文，try-with-resources 即使发生异常，close() 方法也会被调用
     */
    public static class TimerContext implements AutoCloseable {

        private final PerfTracker tracker;

        private TimerContext() {
            this.tracker = new PerfTracker();
        }

        private TimerContext(String methodName) {
            this.tracker = new PerfTracker(methodName);
        }

        private TimerContext(String methodName, Map<String, String> resultMap) {
            this.tracker = new PerfTracker(methodName, resultMap);
        }

        @Override
        public void close() {
            long executeTime = System.currentTimeMillis() - tracker.startTime;
            Map<String, String> resultMap = tracker.resultMap;
            if (executeTime > 0 && executeTime < 500) {
                LOGGER.info("\n==>	耗时：{}ms\n==> Execute ID：{}\n==> Execute Type：{}\n==> Execute Sql：{}", executeTime,
                        resultMap.get("id"), resultMap.get("type"), resultMap.get("sql"));
            }
            else if (executeTime >= 500 && executeTime < 3000) {
                LOGGER.warn("\n==>	【慢查询告警】耗时{} ms\n==> Execute ID：{}\n==> Execute Type：{}\n==> Execute Sql：{}",
                        executeTime, resultMap.get("id"), resultMap.get("type"), resultMap.get("sql"));
            }
            else if (executeTime >= 3000) {
                LOGGER.error("\n==>	【慢查询警告】耗时{} ms\n==> Execute ID：{}\n==> Execute Type：{}\n==> Execute Sql：{}",
                        executeTime, resultMap.get("id"), resultMap.get("type"), resultMap.get("sql"));
            }
        }

    }

}
