package com.wmm.elasticsearch.response;

import com.wmm.elasticsearch.excepiton.BaseException;
import lombok.*;
import org.springframework.http.HttpStatus;

/**
 * @author wangmingming160328
 * @Description
 * @date @2020/6/22 15:42
 */
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ResponseBean {
    /**
     * HTTP状态码
     */
    @NonNull
    private Integer code;

    private String message;
    /**
     * 返回的数据
     */
    private Object data;
}
