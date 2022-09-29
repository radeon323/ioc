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
public class MailService {
    private int port;
    private String protocol;

    public void sendEmail(String emailTo, String content) {
        System.out.println("Sending email to: " + emailTo);
        System.out.println("With content: " + content);
    }
}
