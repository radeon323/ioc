package com.olshevchenko.ioc.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Oleksandr Shevchenko
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Bean {
    private String id;
    private Object value;
}
