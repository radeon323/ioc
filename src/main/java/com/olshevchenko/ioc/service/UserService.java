package com.olshevchenko.ioc.service;

import lombok.*;

/**
 * @author Oleksandr Shevchenko
 */
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserService {
    private MailService mailService;

    public void sendEmailWithUsersCount() {
        int numberOfUsersInSystem = getUsersCount();
        mailService.sendEmail("tech@project.com", "there are " + numberOfUsersInSystem + "users in system!");
    }

    private int getUsersCount() {
        return (int) (Math.random() * 1000);
    }
}
