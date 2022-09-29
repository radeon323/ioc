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
public class PaymentService {
    private MailService mailService;
    private int maxAmount;

    public PaymentService(MailService mailService) {
        this.mailService = mailService;
    }

    public void pay(String from, String to, double amount) {

        mailService.sendEmail("from", "payment successful");
        mailService.sendEmail("to", "payment successful");
    }
}
