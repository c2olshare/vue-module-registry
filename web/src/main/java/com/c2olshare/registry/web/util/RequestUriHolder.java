package com.c2olshare.registry.web.util;

/**
 * @author MaJing
 */
public class RequestUriHolder {

    private static final ThreadLocal<String> HOLDER = new ThreadLocal<>();

    /**
     * 获取请求
     * @return String
     */
    public static String getRequestUri() {
        return HOLDER.get();
    }

    /**
     * 存储请求
     * @param requestId 请求ID
     */
    public static void putRequestUri(String requestId) {
        HOLDER.set(requestId);
    }

    /**
     * 清除请求
     */
    public static void clearRequestUri() {
        HOLDER.remove();
    }

}
