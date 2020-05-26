package com.c2olshare.registry.web.exception;

import com.c2olshare.registry.common.constant.ErrorCode;
import com.c2olshare.registry.common.dto.HttpResponse;
import com.c2olshare.registry.common.exception.ExceptionMessage;
import com.c2olshare.registry.common.util.HttpUtils;
import com.c2olshare.registry.web.constant.ApiPrefix;
import com.c2olshare.registry.web.constant.UrlPrefix;
import com.c2olshare.registry.web.constant.Variables;
import com.c2olshare.registry.web.util.RequestUriHolder;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author MaJing
 */
@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
public class GlobalErrorController extends AbstractErrorController {

    private static final String API_PREFIX = "api";

    private final ErrorProperties errorProperties;

    private final BasicErrorController defaultController;

    public GlobalErrorController(ErrorAttributes errorAttributes,
                                 ServerProperties serverProperties) {
        super(errorAttributes, Collections.emptyList());
        this.errorProperties = serverProperties.getError();
        this.defaultController = new BasicErrorController(errorAttributes, this.errorProperties);
    }

    @Override
    public String getErrorPath() {
        return this.errorProperties.getPath();
    }

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        // 支持Vue路由
        // 404的GET请求
        if (HttpStatus.NOT_FOUND.equals(status) && HttpMethod.GET.name().equalsIgnoreCase(request.getMethod())) {
            String requestUri = getOriginRequestUri(request);
            // 不是后端服务地址, 不是API接口地址
            if (!requestUri.startsWith(UrlPrefix.PREFIX) && !requestUri.startsWith(ApiPrefix.PREFIX)) {
                try {
                    // 返回首页资源，即index.html
                    response.setStatus(HttpServletResponse.SC_OK);
                    request.getRequestDispatcher("/").forward(request, response);
                    return null;
                } catch (IOException | ServletException e) {
                    // ignore
                }
            }
        }
        return defaultController.errorHtml(request, response);
    }

    @RequestMapping
    public ResponseEntity<?> error(HttpServletRequest request, HttpServletResponse response) {
        String requestUri = getOriginRequestUri(request);

        ExceptionMessage error = new ExceptionMessage()
                .setOrigin(HttpUtils.getRequestIp(request))
                .setPath(requestUri)
                .setTimestamp(new Date());

        ResponseEntity<Map<String, Object>> responseEntity = defaultController.error(request);
        Map<String, Object> objectMap = responseEntity.getBody();
        if (Objects.nonNull(objectMap)) {
            String message = String.valueOf(objectMap.getOrDefault("message", Variables.DEFAULT_UNKNOWN_MESSAGE));
            error.setMessage(message);
        }

        if (requestUri.contains(API_PREFIX)) {
            HttpResponse<ExceptionMessage> body = HttpResponse.build(ErrorCode.ACCESS_EXCEPTION, null, error);
            return new ResponseEntity<>(body, responseEntity.getStatusCode());
        } else {
            return new ResponseEntity<>(error, responseEntity.getStatusCode());
        }
    }

    /**
     * 获取原始的请求地址
     * @param request 请求
     * @return string
     */
    public String getOriginRequestUri(HttpServletRequest request) {
        String requestUri = RequestUriHolder.getRequestUri();
        if (Objects.isNull(requestUri)) {
            Map<String, Object> model = Collections.unmodifiableMap(this.getErrorAttributes(request, this.isIncludeStackTrace(request)));
            requestUri = String.valueOf(model.getOrDefault("path", request.getRequestURI()));
        }

        return requestUri;
    }

    private boolean isIncludeStackTrace(HttpServletRequest request) {
        ErrorProperties.IncludeStacktrace include = getErrorProperties().getIncludeStacktrace();
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
            return getTraceParameter(request);
        }
        return false;
    }

    private ErrorProperties getErrorProperties() {
        return this.errorProperties;
    }
}
