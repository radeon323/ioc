package com.olshevchenko.ioc.reader.xmlreader;

import com.olshevchenko.ioc.entity.BeanDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Oleksandr Shevchenko
 */
class XMLDomBeanDefinitionReaderTest {

    private final String path = "src/main/webapp/WEB-INF/context.xml";
    private final XMLDomBeanDefinitionReader reader = new XMLDomBeanDefinitionReader(path);
    private final List<BeanDefinition> beanDefinitionsExpected = new ArrayList<>();

    @BeforeEach
    void init() {
        BeanDefinition mailService = new BeanDefinition();
        mailService.setId("mailService");
        mailService.setBeanClassName("com.olshevchenko.ioc.service.MailService");
        mailService.setDependencies(Map.of("protocol","POP3", "port","3000"));
        mailService.setRefDependencies(Map.of());
        beanDefinitionsExpected.add(mailService);

        BeanDefinition userService = new BeanDefinition();
        userService.setId("userService");
        userService.setBeanClassName("com.olshevchenko.ioc.service.UserService");
        userService.setDependencies(Map.of());
        userService.setRefDependencies(Map.of("mailService","mailService"));
        beanDefinitionsExpected.add(userService);

        BeanDefinition paymentService = new BeanDefinition();
        paymentService.setId("paymentService");
        paymentService.setBeanClassName("com.olshevchenko.ioc.service.PaymentService");
        paymentService.setDependencies(Map.of());
        paymentService.setRefDependencies(Map.of("mailService","mailService"));
        beanDefinitionsExpected.add(paymentService);

        BeanDefinition paymentWithMaxService = new BeanDefinition();
        paymentWithMaxService.setId("paymentWithMaxService");
        paymentWithMaxService.setBeanClassName("com.olshevchenko.ioc.service.PaymentService");
        paymentWithMaxService.setDependencies(Map.of("maxAmount","5000"));
        paymentWithMaxService.setRefDependencies(Map.of("mailService","mailService"));
        beanDefinitionsExpected.add(paymentWithMaxService);
    }

    @Test
    void testGetBean() {
        List<BeanDefinition> beanDefinitionsActual = reader.readBeanDefinitions();
        assertEquals(beanDefinitionsExpected.size(), beanDefinitionsActual.size());
        assertEquals(beanDefinitionsExpected, beanDefinitionsActual);
    }
}