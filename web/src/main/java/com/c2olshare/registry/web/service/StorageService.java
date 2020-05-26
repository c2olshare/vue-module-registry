package com.c2olshare.registry.web.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author MaJing
 */
public interface StorageService {

    /**
     * 保存文件
     *
     * @param file  文件
     * @param paths 路径
     * @return file
     */
    File save(MultipartFile file, String... paths);

    /**
     * 删除文件
     *
     * @param paths 路径
     */
    void delete(String... paths);

    /**
     * 获取文件
     *
     * @param paths 路径
     * @return file
     */
    File get(String... paths);

    /**
     * 获取文件
     *
     * @param paths 路径
     * @return file[]
     */
    File[] list(String... paths);
}
