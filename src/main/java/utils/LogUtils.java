package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtils {
    // 确保使用正确的 Logger 实例
    private static final Logger Log = LogManager.getLogger(LogUtils.class);

    public static void info(String message) {
        Log.info(message);
    }

    public static void warn(String message) {
        Log.warn(message);
    }

    // 增强版：除了消息，还能记录具体的异常堆栈信息
    public static void error(String message) {
        Log.error(message);
    }

    public static void error(String message, Throwable throwable) {
        Log.error(message, throwable);
    }

    public static void debug(String message) {
        Log.debug(message);
    }
}