package com.c2olshare.registry.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author MaJing
 */
public class HttpUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    private static final String URI_SEPARATOR = "/";

    private HttpUtils() {
    }

    public static void responseAsJson(HttpServletResponse response, int status, Object data) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(data);
            writer.write(json);
            writer.flush();
        }
    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    /**
     * 获取用户真实IP地址
     *
     * @return string
     */
    public static String getRequestIp(HttpServletRequest request) {
        String unknown = "unknown";
        String ip = request.getHeader("X-FORWARDED-FOR");
        LOGGER.trace("X-FORWARDED-FOR: {}", ip);
        if (ip != null && ip.length() != 0 && !unknown.equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            String separator = ",";
            if (ip.contains(separator)) {
                ip = ip.split(separator)[0];
            }
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            LOGGER.trace("Proxy-Client-IP: {}", ip);
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            LOGGER.trace("WL-Proxy-Client-IP: {}", ip);
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            LOGGER.trace("HTTP_CLIENT_IP: {}", ip);
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            LOGGER.trace("HTTP_X_FORWARDED_FOR: {}", ip);
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
            LOGGER.trace("X-Real-IP: {}", ip);
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            LOGGER.trace("RemoteAddr: {}", ip);
        }

        LOGGER.debug("Get RequestRealIP: {}", ip);
        return ip;
    }

    /**
     * 获取服务地址
     *
     * @return string
     */
    public static String getHostAddress(HttpServletRequest request) {
        // 协议://域名:端口
        return String.format("%s://%s:%s", request.getScheme(), request.getServerName(), request.getServerPort());
    }


    /**
     * 删除多余斜杠
     * @param spec url
     * @return uri
     */
    public static String sanitizeUrl(String spec) {
        try {
            URL url = new URL(spec);
            String oldPath = url.getPath();
            String newPath = oldPath.replaceAll("/{2,}", "/");
            if (newPath.startsWith(URI_SEPARATOR)) {
                newPath = newPath.substring(1);
            }
            if (newPath.endsWith(URI_SEPARATOR)) {
                newPath = newPath.substring(0, newPath.length() - 1);
            }
            return String.format("%s://%s:%s/%s?%s", url.getProtocol(), url.getHost(), url.getPort(), newPath, url.getQuery());
        } catch (MalformedURLException e) {
            throw new RuntimeException("格式化URL异常", e);
        }
    }
}