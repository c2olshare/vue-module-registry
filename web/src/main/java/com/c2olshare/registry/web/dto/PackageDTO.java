package com.c2olshare.registry.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import java.util.StringJoiner;

/**
 * @author MaJing
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PackageDTO {
    /**
     * 版本
     */
    @NotBlank(message = "版本不能为空")
    private String version;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空")
    private String description;

    /**
     * 模块编码
     */
    @NotBlank(message = "模块不能为空")
    private String module;

    /**
     * 命名空间编码
     */
    private String namespace;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PackageDTO.class.getSimpleName() + "[", "]")
                .add("version='" + version + "'")
                .add("description='" + description + "'")
                .add("module='" + module + "'")
                .add("namespace='" + namespace + "'")
                .toString();
    }
}
