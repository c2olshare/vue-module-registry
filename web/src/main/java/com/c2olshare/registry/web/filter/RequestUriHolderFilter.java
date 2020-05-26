package com.c2olshare.registry.web.filter;

import com.c2olshare.registry.web.util.RequestUriHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author MaJing
 */
public class RequestUriHolderFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestUriHolderFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();

        try {
            LOGGER.trace("Bound request uri {} to thread", requestUri);
            RequestUriHolder.putRequestUri(requestUri);
            filterChain.doFilter(request, response);
        } finally {
            LOGGER.trace("Cleared thread-bound request uri: {}", requestUri);
            RequestUriHolder.clearRequestUri();
        }
    }
}
