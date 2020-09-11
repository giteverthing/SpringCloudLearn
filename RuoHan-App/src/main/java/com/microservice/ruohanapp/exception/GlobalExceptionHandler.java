package com.microservice.ruohanapp.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public void handlerRuntimeException(RuntimeException e) {
        try {
            getResponse().setHeader("httpError", "error");
            getResponse().getWriter().write(e.getMessage());
            getResponse().getWriter().close();
        } catch (IOException ioException) {
        }
    }

    private HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    private ServletRequestAttributes getRequestAttributes() {
        return (ServletRequestAttributes) (RequestContextHolder.getRequestAttributes());
    }

    private HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }
}
