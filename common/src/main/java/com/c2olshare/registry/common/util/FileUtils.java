package com.c2olshare.registry.common.util;

import java.io.File;

/**
 * @author MaJing
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

    public static String concat(String root, String... paths) {
        StringBuilder builder = new StringBuilder(root);

        for (String path : paths) {
            builder.append(File.separator).append(path);
        }

        return builder.toString();
    }

}
