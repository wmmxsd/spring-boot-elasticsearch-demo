package com.wmm.elasticsearch.config.impl;

import lombok.SneakyThrows;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Properties;

/**
 * a implementation for {@link PropertySourceFactory}
 * wrapping every yaml file in a {@link ResourcePropertySource
 * @author wangmingming160328
 * @date @2020/6/22 14:43
 */
public class YamlPropertySourceFactory implements PropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) {
        Properties properties = loadYamlIntoProperties(resource);
        String sourceName = name != null ? name : resource.getResource().getFilename();
        Assert.hasText(sourceName, "[Assertion failed] - sourceName must have text; it must not be null, empty, or blank");
        return new PropertiesPropertySource(Objects.requireNonNull(sourceName), properties);
    }

    private Properties loadYamlIntoProperties(EncodedResource resource) {
            YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
            yamlPropertiesFactoryBean.setResources(resource.getResource());
            yamlPropertiesFactoryBean.afterPropertiesSet();
            return yamlPropertiesFactoryBean.getObject();
    }
}
