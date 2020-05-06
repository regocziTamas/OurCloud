package com.thomaster.ourcloud.controllers;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class MaxUploadSizeFilter extends OncePerRequestFilter {

    @Value("${spring.servlet.multipart.max-request-size}")
    private String maxUploadSize;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (ServletException e) {
            Throwable rootCause = e.getRootCause();
            if(rootCause instanceof MaxUploadSizeExceededException) {
                response.getWriter().write("Size limit exceeded, the file is too big, must be less than " + maxUploadSize + "!");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().flush();
                response.getWriter().close();
            } else {
                throw e;
            }
        }
    }
}
