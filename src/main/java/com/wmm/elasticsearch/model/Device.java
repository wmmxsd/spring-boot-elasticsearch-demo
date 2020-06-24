package com.wmm.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author wangmingming160328
 * @Description
 * @date @2020/6/22 18:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    @NonNull
    private long id;
    @NonNull
    private String name;
    @NonNull
    private String ip;
    @NonNull
    private String mac;
    @NonNull
    private String userName;
    private String unit;
    private String department;
}
