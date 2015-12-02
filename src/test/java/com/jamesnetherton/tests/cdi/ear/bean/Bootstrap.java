package com.jamesnetherton.tests.cdi.ear.bean;

import java.lang.reflect.Field;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.CdiCamelContext;

@Startup
@Singleton
public class Bootstrap {

    @Inject
    CamelContext context;

    @Inject
    BeanManager beanManager;

    @PostConstruct
    public void init() {
        try {
            // Hacky route to track the BeanManagers in use
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("timer:foo?period=500")
                    .process(new Processor() {
                        @Override
                        public void process(Exchange exchange) throws Exception {
                            Field field = CdiCamelContext.class.getDeclaredField("beanManager");
                            field.setAccessible(true);

                            BeanManager camelBeanManager = (BeanManager) field.get(exchange.getContext());
                            exchange.getOut().setBody(beanManager.toString() + "\n" + camelBeanManager.toString());
                        }
                    })
                    .to("file:target?fileName=beanmanagers.txt");
                }
            });
            context.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void shutdown() {
        try {
            context.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
