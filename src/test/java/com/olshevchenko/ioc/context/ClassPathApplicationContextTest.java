package com.olshevchenko.ioc.context;

import com.olshevchenko.ioc.exception.MultipleBeansForClassException;
import com.olshevchenko.ioc.reader.xmlreader.XMLDomBeanDefinitionReader;
import com.olshevchenko.ioc.service.MailService;
import com.olshevchenko.ioc.service.PaymentService;
import com.olshevchenko.ioc.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oleksandr Shevchenko
 */
class ClassPathApplicationContextTest {

    private final String path = "src/main/webapp/WEB-INF/context.xml";
    private UserService userService;
    private MailService mailService;
    private PaymentService paymentService;
    private PaymentService paymentServiceWithMaxAmount;
    private ApplicationContext applicationContext;

    @BeforeEach
    void init() {
        mailService = new MailService(3000,"POP3");
        userService = new UserService(mailService);
        paymentService = new PaymentService(mailService);
        paymentServiceWithMaxAmount = new PaymentService(mailService, 5000);
        applicationContext = new ClassPathApplicationContext(path);
    }

    @Test
    void testApplicationContextInstantiation() {
        ClassPathApplicationContext applicationContextSetReader = new ClassPathApplicationContext();
        applicationContextSetReader.setBeanDefinitionReader(new XMLDomBeanDefinitionReader(path));
        applicationContextSetReader.start();
        assertEquals(applicationContext.getBean(UserService.class),applicationContextSetReader.getBean(UserService.class));
        assertEquals(applicationContext.getBean("mailService", MailService.class),applicationContextSetReader.getBean("mailService", MailService.class));
        assertEquals(applicationContext.getBean("paymentWithMaxService"),applicationContextSetReader.getBean("paymentWithMaxService"));
    }

    @Test
    void testGetBeanByClass() {
        assertEquals(userService,applicationContext.getBean(UserService.class));
        assertEquals(mailService,applicationContext.getBean(MailService.class));
    }

    @Test
    void testGetBeanByClassException() {
        assertThrows(MultipleBeansForClassException.class, () -> {
            applicationContext.getBean(PaymentService.class);
        });
    }

    @Test
    void testGetBeanByNameAndClass() {
        assertEquals(userService,applicationContext.getBean("userService", UserService.class));
        assertEquals(mailService,applicationContext.getBean("mailService", MailService.class));
        assertEquals(paymentService,applicationContext.getBean("paymentService", PaymentService.class));
        assertEquals(paymentServiceWithMaxAmount,applicationContext.getBean("paymentWithMaxService", PaymentService.class));
    }

    @Test
    void testGetBeanByName() {
        assertEquals(userService,applicationContext.getBean("userService"));
        assertEquals(mailService,applicationContext.getBean("mailService"));
        assertEquals(paymentService,applicationContext.getBean("paymentService"));
        assertEquals(paymentServiceWithMaxAmount,applicationContext.getBean("paymentWithMaxService"));
    }
}
