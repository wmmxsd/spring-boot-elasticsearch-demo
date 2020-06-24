package com.wmm.elasticsearch.excepiton;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangmingming160328
 * @Description 项目父异常
 * @date @2020/5/8 11:10
 */
public abstract class BaseException extends RuntimeException{
    @NonNull
    @Getter
    private final ErrorCode error;
    @Getter
    private final HashMap<String, Object> data = new HashMap<>();


    public BaseException(ErrorCode error, Map<String, Object> data) {
        super(error.getMessage());
        this.error = error;
        if (!CollectionUtils.isEmpty(data)) {
         this.data.putAll(data);
        }
    }

    public BaseException(Throwable cause, ErrorCode error, Map<String, Object> data) {
        super(error.getMessage(), cause);
        this.error = error;
        if (!CollectionUtils.isEmpty(data)) {
            this.data.putAll(data);
        }
    }
}
