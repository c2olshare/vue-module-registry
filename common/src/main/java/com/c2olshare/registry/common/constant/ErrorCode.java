package com.c2olshare.registry.common.constant;

/**
 * @author MaJing
 */
public enum ErrorCode {
    /**
     * 一切OK
     */
    SUCCESS("00000", "操作成功"),

    /**
     * 资源相关
     */
    CODE_ALREADY_EXIST("A0100", "资源代码已存在"),
    NAME_ALREADY_EXIST("A0101", "资源名称已存在"),
    VERSION_ALREADY_EXIST("A0102", "资源版本已存在"),
    RESOURCE_NOT_EXIST("A0200", "资源数据不存在"),

    /**
     * 认证相关
     */
    ACCESS_EXCEPTION("B0300", "访问权限异常"),

    /**
     * 服务相关
     */
    SERVICE_GLOBAL_ERROR("C0100", "全局错误代码"),
    ;

    private final String code;

    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}