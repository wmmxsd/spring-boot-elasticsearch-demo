package com.wmm.elasticsearch.excepiton;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.Instant;
import java.util.HashMap;

/**
 * @author wangmingming160328
 * @Description 异常包装类
 * @date @2020/5/7 18:49
 */
@AllArgsConstructor
public class ErrorResponse {
    @NonNull @Getter @Setter private int code;
    @NonNull @Getter @Setter private int status;
    @NonNull @Getter @Setter private String message;
    @NonNull @Getter @Setter private String path;
    @NonNull @Getter @Setter private Instant timestamp;
    @Getter @Setter private HashMap<String, Object> data;

    public ErrorResponse(BaseException exception, String url) {
        this(exception.getError().getCode(), exception.getError().getStatus().value(), exception.getError().getMessage(), url, Instant.now(), exception.getData());
    }

    @Override
    public String toString() {
        return "ErrorReponse{" + "code=" + code + ", status=" + status + ", message='" + message + '\'' + ", path='" + path + '\'' + ", timestamp=" + timestamp + ", data=" + data + '}';
    }
}
