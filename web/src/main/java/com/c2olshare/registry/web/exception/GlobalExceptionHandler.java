package com.c2olshare.registry.web.exception;


import com.c2olshare.registry.common.constant.ErrorCode;
import com.c2olshare.registry.common.dto.HttpResponse;
import com.c2olshare.registry.common.exception.BusinessException;
import com.c2olshare.registry.common.exception.ExceptionMessage;
import com.c2olshare.registry.common.util.HttpUtils;
import com.c2olshare.registry.web.util.RequestUriHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author MaJing
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String API_PREFIX = "api";

    private final GlobalErrorController defaultController;

    public GlobalExceptionHandler(GlobalErrorController defaultController) {
        this.defaultController = defaultController;
    }

    /**
     * 全局异常处理
     *
     * @param exception 异常信息
     * @return CloudResult
     */
    @ExceptionHandler
    public ResponseEntity<?> exceptionHandler(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        String requestUri = defaultController.getOriginRequestUri(request);
        ExceptionMessage error = new ExceptionMessage()
                .setOrigin(HttpUtils.getRequestIp(request))
                .setPath(requestUri)
                .setTimestamp(new Date());

        String message = resolveException(exception, request, response);
        if (Objects.nonNull(message)) {
            error.setMessage(message);
        } else {
            // 未知错误，调用默认错误处理器
            LOGGER.error("Unknown exception: {}", exception.getMessage());
            return defaultController.error(request, response);
        }

        HttpStatus status = response.getStatus() != HttpServletResponse.SC_OK ?
                HttpStatus.valueOf(response.getStatus()) : HttpStatus.valueOf(HttpServletResponse.SC_BAD_REQUEST);
        if (requestUri.contains(API_PREFIX)) {
            HttpResponse<ExceptionMessage> body = HttpResponse.build(ErrorCode.SERVICE_GLOBAL_ERROR, null, error);
            return new ResponseEntity<>(body, status);
        } else {
            return new ResponseEntity<>(error, status);
        }
    }

    private String resolveException(Throwable exception, HttpServletRequest request, HttpServletResponse response) {
        String message = null;
        // 业务异常
        if (exception instanceof BusinessException) {
            message = exception.getMessage();
        }
        // 乐观锁
        else if (exception instanceof ObjectOptimisticLockingFailureException) {
            message = "数据已更新，请刷新重试";
        }
        // 无数据
        else if (exception instanceof EmptyResultDataAccessException) {
            message = "请求数据不存在";
        }
        // 方法不支持
        else if (exception instanceof HttpRequestMethodNotSupportedException) {
            HttpRequestMethodNotSupportedException e = (HttpRequestMethodNotSupportedException) exception;
            message = String.format("请求不支持 %s 方法", e.getMethod());
        }
        // 参数不完整
        else if (exception instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException e = (MissingServletRequestParameterException) exception;
            message = String.format("必需的 %s 参数 %s 不存在,", e.getParameterType(), e.getParameterName());
        }
        // 请求体为空
        else if (exception instanceof MultipartException) {
            message = "当前请求不是 multipart 请求";
        }
        // 请求体为空
        else if (exception instanceof HttpMessageNotReadableException) {
            message = "请求体为空";
        }
        // 数据完整性
        else if (exception instanceof DataIntegrityViolationException) {
            message = "无法执行操作";
        }
        // 参数校验
        else if (exception instanceof BindException) {
            StringBuilder builder = new StringBuilder();
            BindException e = (BindException) exception;
            List<ObjectError> allErrors = e.getAllErrors();
            Iterator<ObjectError> iterator = allErrors.iterator();
            while (iterator.hasNext()) {
                ObjectError objectError = iterator.next();
                builder.append(objectError.getDefaultMessage());
                if (iterator.hasNext()) {
                    builder.append(", ");
                }
            }
            message = builder.toString();
        }
        // 不允许访问
        else if (exception instanceof AccessDeniedException) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            message = "不允许访问";
        }
        // add more known exception at here

        if (Objects.isNull(message)) {
            Throwable cause = exception.getCause();
            if (Objects.nonNull(cause)) {
                message = resolveException(cause, request, response);
            }
        }

        return message;
    }

}
