package com.humane.admin.etms.filter;

import com.humane.util.filter.RequestWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

@Slf4j
/**
 * 소스 참조
 * http://brantiffy.axisj.com/archives/451
 * https://github.com/brant-hwang/spring-logback-slack-notification-example
 */
public class LoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RequestWrapper requestWrapper = RequestWrapper.of(request);

        String uri = requestWrapper.getRequestUri();
        String[] paths = uri.split("/");
        if (!paths[1].matches("images|css|js|tpl|components") && !paths[paths.length - 1].endsWith(".js"))
            log.info("uri : {}, parameter : {}, body : {}",
                    requestWrapper.getRequestUri(),
                    //requestWrapper.headerMap(),
                    requestWrapper.parameterMap(),
                    requestWrapper.getBody());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}