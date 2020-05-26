package com.c2olshare.registry.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.StringJoiner;

/**
 * @author MaJing
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModuleDTO {

    private String id;

    private String code;

    private String name;

    private String current;

    private String namespace;

    private String url;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public String getId() {
        return id;
    }

    public ModuleDTO setId(String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ModuleDTO setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public ModuleDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getCurrent() {
        return current;
    }

    public ModuleDTO setCurrent(String current) {
        this.current = current;
        return this;
    }

    public String getNamespace() {
        return namespace;
    }

    public ModuleDTO setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public ModuleDTO setUrl(String url) {
        this.url = url;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public ModuleDTO setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public ModuleDTO setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ModuleDTO.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("code='" + code + "'")
                .add("name='" + name + "'")
                .add("current='" + current + "'")
                .add("namespace='" + namespace + "'")
                .add("url='" + url + "'")
                .add("createTime=" + createTime)
                .add("updateTime=" + updateTime)
                .toString();
    }
}
