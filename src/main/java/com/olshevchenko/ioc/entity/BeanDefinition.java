package com.olshevchenko.ioc.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * @author Oleksandr Shevchenko
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BeanDefinition {
    private String id;
    private String beanClassName;
    private Map<String, String> dependencies;
    private Map<String, String> refDependencies;
}
