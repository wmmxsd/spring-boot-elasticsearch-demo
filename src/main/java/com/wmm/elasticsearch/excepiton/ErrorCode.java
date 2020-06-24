package com.wmm.elasticsearch.excepiton;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author wangmingming160328
 * @Description 异常码
 * @date @2020/5/8 11:05
 */
@RequiredArgsConstructor
public enum ErrorCode {
    /**
     * 未在服务器找到该资源
     */
    RESOURCE_NOT_FOUND(1001, HttpStatus.NOT_FOUND, "未找到该资源"),
    /**
     * 数据校验错误
     */
    REQUEST_VALIDATION_FAIL(1002, HttpStatus.BAD_REQUEST, "请求数据格式验证失败");

    @NonNull
    @Getter
    private final int code;

    @NonNull
    @Getter
    private final HttpStatus status;

    @NonNull
    @Getter
    private final String message;

    @Override
    public String toString() {
        return "ErrorCode{" +
                "code=" + code +
                ", status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
