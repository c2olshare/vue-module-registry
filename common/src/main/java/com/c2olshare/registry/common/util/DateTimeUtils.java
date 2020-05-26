package com.c2olshare.registry.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author MaJing
 */
public class DateTimeUtils {

    public static final ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.of("+8");


    /**
     * 获取当前时间
     *
     * @return 当前时间戳
     */
    public static LocalDateTime nowTime() {
        return Instant.now().atZone(DEFAULT_ZONE_OFFSET).toLocalDateTime();
    }

    /**
     * 获取当前时间戳
     *
     * @return 当前时间戳
     */
    public static long nowTimestamp() {
        return LocalDateTime.now().toInstant(DEFAULT_ZONE_OFFSET).toEpochMilli();
    }

    /**
     * 转换时间
     *
     * @param dateTime dateTime
     * @return date
     */
    public static Date transferToDate(LocalDateTime dateTime) {
        Instant instant = dateTime.toInstant(DEFAULT_ZONE_OFFSET);
        return Date.from(instant);
    }

    /**
     * 转换时间
     *
     * @param date date
     * @return dateTime
     */
    public static LocalDateTime transferToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        return instant.atZone(DEFAULT_ZONE_OFFSET).toLocalDateTime();
    }

}
