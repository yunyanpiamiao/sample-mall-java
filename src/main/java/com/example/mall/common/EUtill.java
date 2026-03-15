package com.example.mall.common;

import java.util.function.Supplier;

/**
 * 异常处理工具类 (Exception Util)
 */
public class EUtill {

    /**
     * 如果条件为true，则抛出业务异常
     */
    public static void throwIf(boolean condition, String message) {
        if (condition) {
            throw new BusinessException(message);
        }
    }

    /**
     * 如果条件为true，则抛出业务异常（带错误码）
     */
    public static void throwIf(boolean condition, int code, String message) {
        if (condition) {
            throw new BusinessException(code, message);
        }
    }

    /**
     * 如果对象为空，则抛出业务异常
     */
    public static void throwIfNull(Object obj, String message) {
        if (obj == null) {
            throw new BusinessException(message);
        }
    }

    /**
     * 如果字符串为空或空白，则抛出业务异常
     */
    public static void throwIfBlank(String str, String message) {
        if (str == null || str.trim().isEmpty()) {
            throw new BusinessException(message);
        }
    }

    /**
     * 如果对象不存在，则抛出资源未找到异常
     */
    public static void throwIfNotFound(Object obj, String resourceName) {
        if (obj == null) {
            throw new ResourceNotFoundException(resourceName + "不存在");
        }
    }

    /**
     * 执行操作，捕获异常并转换为业务异常
     */
    public static <T> T execute(Supplier<T> supplier, String errorMessage) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new BusinessException(errorMessage + ": " + e.getMessage());
        }
    }

    /**
     * 执行操作，捕获异常并转换为业务异常（带错误码）
     */
    public static <T> T execute(Supplier<T> supplier, int code, String errorMessage) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new BusinessException(code, errorMessage + ": " + e.getMessage());
        }
    }

    /**
     * 断言对象不为空
     */
    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null) {
            throw new BusinessException(message);
        }
        return obj;
    }

    /**
     * 断言条件为真
     */
    public static void requireTrue(boolean condition, String message) {
        if (!condition) {
            throw new BusinessException(message);
        }
    }

    /**
     * 断言条件为假
     */
    public static void requireFalse(boolean condition, String message) {
        if (condition) {
            throw new BusinessException(message);
        }
    }
}
