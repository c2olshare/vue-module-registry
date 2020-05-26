package com.c2olshare.registry.common.util;

import java.util.HashMap;

/**
 * @author MaJing
 */
public class ContentTypeUtils {

    private static final HashMap<String, String> MAP = new HashMap<>(64);

    private static final String DEFAULT_TYPE = "application/octet-stream";

    static {
        MAP.put("*", DEFAULT_TYPE);

        // 音视频
        MAP.put("wav", "audio/wav");
        MAP.put("mp3", "audio/mp3");
        MAP.put("avi", "video/avi");
        MAP.put("mp4", "video/mpeg4");

        // 文本
        MAP.put("css", "text/css; charset=utf-8");
        MAP.put("htm", "text/html; charset=utf-8");
        MAP.put("html", "text/html; charset=utf-8");
        MAP.put("md", "text/plain; charset=utf-8");
        MAP.put("txt", "text/plain; charset=utf-8");
        MAP.put("xhtml", "text/html; charset=utf-8");
        MAP.put("xml", "text/xml; charset=utf-8");
        MAP.put("svg", "text/xml; charset=utf-8");


        // 图片
        MAP.put("gif", "image/gif");
        MAP.put("ico", "image/x-icon");
        MAP.put("jpeg", "image/jpeg");
        MAP.put("jpg", "image/jpeg");
        MAP.put("png", "image/png");
        MAP.put("tif", "image/tiff");
        MAP.put("tiff", "image/tiff");

        // application
        MAP.put("js", "application/javascript; charset=utf-8");
        MAP.put("json", "application/json; charset=utf-8");
    }

    public static String valueOf(String fileType) {
        return MAP.getOrDefault(fileType, DEFAULT_TYPE);
    }
}
