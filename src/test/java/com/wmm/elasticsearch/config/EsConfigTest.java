package com.wmm.elasticsearch.config;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class EsConfigTest {
    @Autowired
    private EsConfig esConfig;

    @Test
    public void test() {
        Assert.assertEquals("192.168.119.25", esConfig.getHostName());
        Assert.assertEquals(9200, (int) esConfig.getPort());
    }
}