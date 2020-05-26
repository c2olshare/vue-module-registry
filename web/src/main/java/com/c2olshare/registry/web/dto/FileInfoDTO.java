package com.c2olshare.registry.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.StringJoiner;

/**
 * @author MaJing
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileInfoDTO {

    private String name;

    private Long length;

    private Boolean directory;

    private String extension;

    private Long modifyTime;

    private String url;

    public String getName() {
        return name;
    }

    public FileInfoDTO setName(String name) {
        this.name = name;
        return this;
    }

    public Long getLength() {
        return length;
    }

    public FileInfoDTO setLength(Long length) {
        this.length = length;
        return this;
    }

    public Boolean getDirectory() {
        return directory;
    }

    public FileInfoDTO setDirectory(Boolean directory) {
        this.directory = directory;
        return this;
    }

    public String getExtension() {
        return extension;
    }

    public FileInfoDTO setExtension(String extension) {
        this.extension = extension;
        return this;
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public FileInfoDTO setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public FileInfoDTO setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FileInfoDTO.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("length=" + length)
                .add("directory=" + directory)
                .add("modifyTime=" + modifyTime)
                .toString();
    }
}
