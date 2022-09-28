package com.olshevchenko.ioc.service;

import lombok.*;

/**
 * @author Oleksandr Shevchenko
 */
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
