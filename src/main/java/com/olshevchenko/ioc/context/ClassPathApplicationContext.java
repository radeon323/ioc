package com.olshevchenko.ioc.context;

import com.olshevchenko.ioc.entity.Bean;
import com.olshevchenko.ioc.entity.BeanDefinition;
import com.olshevchenko.ioc.exception.BeanInstantiationException;
import com.olshevchenko.ioc.exception.MultipleBeansForClassException;
import com.olshevchenko.ioc.reader.BeanDefinitionReader;
import com.olshevchenko.ioc.reader.xmlreader.XMLDomBeanDefinitionReader;
import com.olshevchenko.ioc.utils.JavaNumberTypeCast;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Oleksandr Shevchenko
 */
@NoArgsConstructor
public class ClassPathApplicationContext implements ApplicationContext {
    private static final String SETTER_PREFIX = "set";
    private BeanDefinitionReader beanDefinitionReader;
    private Map<String, Bean> beans;

    public ClassPathApplicationContext(String path) {
        setBeanDefinitionReader(new XMLDomBeanDefinitionReader(path));
        start();
    }

    public void start() {
        beans = new HashMap<>();
        List<BeanDefinition> beanDefinitions = beanDefinitionReader.readBeanDefinitions();
        createBeans(beanDefinitions);
        injectValueDependencies(beanDefinitions);
        injectRefDependencies(beanDefinitions);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        List<Object> listOfBeans = beans.values().stream().map(Bean::getValue).filter(bean -> clazz.equals(bean.getClass())).toList();
        if (listOfBeans.size() > 1) {
            throw new MultipleBeansForClassException("More than one bean found for class " + clazz.getName());
        }
        return (T) listOfBeans.get(0);
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        T bean = (T) getBean(name);
        if (!bean.getClass().isAssignableFrom(clazz)) {
            throw new BeanInstantiationException("Could not create bean of class " + clazz.getName());
        }
        return bean;
    }

    @Override
    public Object getBean(String name) {
        return beans.get(name).getValue();
    }

    @Override
    public List<String> getBeanNames() {
        return beans.keySet().stream().toList();
    }

    @Override
    public void setBeanDefinitionReader(BeanDefinitionReader beanDefinitionReader) {
        this.beanDefinitionReader = beanDefinitionReader;
    }

    private void createBeans(List<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            try {
                Object object = Class.forName(beanDefinition.getBeanClassName()).getConstructor().newInstance();
                String id = beanDefinition.getId();

                Bean bean = new Bean();
                bean.setId(id);
                bean.setValue(object);

                beans.put(id, bean);
            } catch (ClassNotFoundException e) {
                throw new BeanInstantiationException("Could not find class for name: " + beanDefinition.getBeanClassName(), e);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                throw new BeanInstantiationException("Could not create bean", e);
            }
        }
    }

    private void injectValueDependencies(List<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Bean bean = beans.get(beanDefinition.getId());
            Map<String, String> valueDependencies = beanDefinition.getDependencies();

            if (valueDependencies != null) {
                for (Map.Entry<String, String> entry : valueDependencies.entrySet()) {
                    try {
                        String setterName = getSetterName(entry.getKey());
                        Object beanValue = bean.getValue();
                        Class<?> clazz = beanValue.getClass();
                        Field field = clazz.getDeclaredField(entry.getKey());
                        Class<?> type = field.getType();

                        Object fieldValue = entry.getValue();
                        if (type.isPrimitive()) {
                            fieldValue = JavaNumberTypeCast.castPrimitive(entry.getValue(), type);
                        }
                        if (fieldValue != null) {
                            Method method = clazz.getMethod(setterName, type);
                            method.invoke(beanValue, fieldValue);
                        }
                    } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        throw new BeanInstantiationException("Unable to inject dependency into the bean " + bean.getId(), e);
                    }
                }
            }
        }
    }

    private void injectRefDependencies(List<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Bean bean = beans.get(beanDefinition.getId());
            Map<String, String> refDependencies = beanDefinition.getRefDependencies();

            if (refDependencies != null) {
                for (Map.Entry<String, String> entry : refDependencies.entrySet()) {
                    try {
                        String setterName = getSetterName(entry.getKey());
                        Object beanValue = bean.getValue();
                        Class<?> clazz = beanValue.getClass();
                        Field field = clazz.getDeclaredField(entry.getValue());
                        Class<?> type = field.getType();

                        Object fieldValue = beans.get(entry.getKey()).getValue();
                        if (fieldValue != null) {
                            Method method = clazz.getMethod(setterName, type);
                            method.invoke(beanValue, fieldValue);
                        }
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException |
                             NoSuchFieldException e) {
                        throw new BeanInstantiationException("Unable to inject dependency into the bean " + bean.getId(), e);
                    }
                }
            }
        }
    }

    private String getSetterName(String propertyName) {
        return SETTER_PREFIX + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }


}
