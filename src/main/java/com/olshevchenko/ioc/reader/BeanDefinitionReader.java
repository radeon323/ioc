package com.olshevchenko.ioc.reader;


import com.olshevchenko.ioc.entity.BeanDefinition;

import java.util.List;

public interface BeanDefinitionReader {
    List<BeanDefinition> readBeanDefinitions();
}
