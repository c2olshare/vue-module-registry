package com.c2olshare.registry.common.exception;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

/**
 * @author MaJing
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionMessage implements Serializable {

    private static final long serialVersionUID = -1673861618618717468L;

    private String origin;

    private String path;

    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timestamp;

    public ExceptionMessage() {
    }

    public String getOrigin() {
        return origin;
    }

    public ExceptionMessage setOrigin(String origin) {
        this.origin = origin;
        return this;
    }

    public String getPath() {
        return path;
    }

    public ExceptionMessage setPath(String path) {
        this.path = path;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ExceptionMessage setMessage(String message) {
        this.message = message;
        return this;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public ExceptionMessage setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
