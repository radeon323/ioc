package com.olshevchenko.ioc;

import com.olshevchenko.ioc.context.ApplicationContext;
import com.olshevchenko.ioc.context.ClassPathApplicationContext;
import com.olshevchenko.ioc.service.UserService;

/**
 * @author Oleksandr Shevchenko
 */
public class Starter {
    public static void main(String[] args) throws InterruptedException {
        String path = "src/main/webapp/WEB-INF/context.xml";
        ApplicationContext applicationContext = new ClassPathApplicationContext(path);
        UserService userService = applicationContext.getBean(UserService.class);

        userService.sendEmailWithUsersCount();
//        while (true) {
//            Thread.sleep(1000);
//            userService.sendEmailWithUsersCount();
//        }
    }


}
