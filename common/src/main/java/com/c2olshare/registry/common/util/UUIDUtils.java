package com.c2olshare.registry.common.util;

import java.util.UUID;

/**
 * UUID工具类
 *
 * @author MaJing
 */
public class UUIDUtils {

    /**
     * 获取UUID
     *
     * @return UUID字符串
     */
    public static String getUUID() {
        return getRawUUID().replaceAll("-", "");
    }

    /**
     * 获取原始UUID
     *
     * @return 原始UUID字符串
     */
    public static String getRawUUID() {
        return UUID.randomUUID().toString();
    }
}
