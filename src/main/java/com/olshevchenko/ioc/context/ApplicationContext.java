package com.olshevchenko.ioc.context;


import com.olshevchenko.ioc.reader.BeanDefinitionReader;

import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
public interface ApplicationContext {

    <T> T getBean(Class<T> clazz);

    <T> T getBean(String name, Class<T> clazz);

    <T> T getBean(String name);

    List<String> getBeanNames();

    void setBeanDefinitionReader(BeanDefinitionReader beanDefinitionReader);
}
