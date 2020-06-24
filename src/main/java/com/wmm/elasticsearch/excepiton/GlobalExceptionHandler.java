package com.wmm.elasticsearch.excepiton;

import com.wmm.elasticsearch.response.ResponseBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * @author wangmingming160328
 * @Description Controller异常处理类
 * @date @2020/6/22 18:27
 */
@ControllerAdvice(value = "com.wmm.elasticsearch.controller")
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseBean> handleControllerException(Exception e) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ResponseBean(500, e.getMessage(), null));
    }
}
