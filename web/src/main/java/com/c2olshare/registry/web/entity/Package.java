package com.c2olshare.registry.web.entity;

import com.c2olshare.registry.common.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

/**
 * @author MaJing
 */
@Entity
@Table(name = "t_vmr_package", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"version", "moduleCode", "namespaceCode"})
})
public class Package extends BaseEntity {

    private static final long serialVersionUID = -1186490154443674977L;

    /**
     * 版本
     */
    @NotBlank(message = "版本不能为空")
    @Column(nullable = false)
    private String version;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空")
    @Column(nullable = false)
    private String description;

    /**
     * 模块代码
     */
    @NotBlank(message = "模块代码不能为空")
    @Column(nullable = false)
    private String moduleCode;

    /**
     * 命名空间代码
     */
    @NotBlank(message = "命名空间代码不能为空")
    @Column(nullable = false)
    private String namespaceCode;

    public String getVersion() {
        return version;
    }

    public Package setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Package setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public Package setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
        return this;
    }

    public String getNamespaceCode() {
        return namespaceCode;
    }

    public Package setNamespaceCode(String namespaceCode) {
        this.namespaceCode = namespaceCode;
        return this;
    }
}
