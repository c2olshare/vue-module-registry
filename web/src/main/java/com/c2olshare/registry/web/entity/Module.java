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
@Table(name = "t_vmr_module", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "namespaceCode"}),
        @UniqueConstraint(columnNames = {"code", "namespaceCode"})
})
public class Module extends BaseEntity {

    private static final long serialVersionUID = 2112341239252918140L;

    /**
     * 模块名称
     */
    @NotBlank(message = "模块名称不能为空")
    @Column(nullable = false)
    private String name;

    /**
     * 模块代码
     */
    @NotBlank(message = "模块代码不能为空")
    @Column(nullable = false)
    private String code;

    /**
     * 当前版本
     */
    @Column(nullable = false)
    private String current;

    /**
     * 描述
     */
    @NotBlank(message = "模块描述不能为空")
    @Column(nullable = false)
    private String description;

    /**
     * 命名空间代码
     */
    @NotBlank(message = "命名空间代码不能为空")
    @Column(nullable = false)
    private String namespaceCode;

    public String getName() {
        return name;
    }

    public Module setName(String name) {
        this.name = name;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Module setCode(String code) {
        this.code = code;
        return this;
    }

    public String getCurrent() {
        return current;
    }

    public Module setCurrent(String current) {
        this.current = current;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Module setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getNamespaceCode() {
        return namespaceCode;
    }

    public Module setNamespaceCode(String namespaceCode) {
        this.namespaceCode = namespaceCode;
        return this;
    }
}
