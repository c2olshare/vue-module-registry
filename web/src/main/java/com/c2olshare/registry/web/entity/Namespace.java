package com.c2olshare.registry.web.entity;

import com.c2olshare.registry.common.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * @author MaJing
 */
@Entity
@Table(name = "t_vmr_namespace")
public class Namespace extends BaseEntity {

    private static final long serialVersionUID = 7801455426998454230L;

    @NotBlank(message = "名称不能为空")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "代码不能为空")
    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private Boolean preset;

    public String getName() {
        return name;
    }

    public Namespace setName(String name) {
        this.name = name;
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getPreset() {
        return preset;
    }

    public void setPreset(Boolean preset) {
        this.preset = preset;
    }
}
