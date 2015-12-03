package com.jamesnetherton.tests.cdi.ear.bean;

import javax.inject.Named;

import org.apache.camel.Body;

@Named
public class HelloBean {

    public String sayHello(@Body String message) {
        return "Hello " + message;
    }
}
