package com.jamesnetherton.tests.cdi.ear;


import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jamesnetherton.tests.cdi.ear.bean.Bootstrap;

@RunWith(Arquillian.class)
public class CdiEarWithAllDependenciesTest {

    @Deployment
    public static EnterpriseArchive createEarDeployment() {
        return ShrinkWrap.create(EnterpriseArchive.class, "camel-ejb-ear.ear")
            .addAsLibraries(
                Maven.resolver()
                    .loadPomFromFile("pom.xml")
                    .resolve("org.apache.camel:camel-cdi")
                    .withTransitivity()
                    .as(JavaArchive.class)
            )
            .addAsModule(
                ShrinkWrap.create(JavaArchive.class, "camel-ejb-jar.jar")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addPackage(Bootstrap.class.getPackage())
                .addPackage(CdiEarWithAllDependenciesTest.class.getPackage())
            );
    }

    @Test
    public void testResolveHelloBean() throws Exception {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("org.apache.camel:context=camel-2,type=context,name=\"camel-2\"");
        String methodSignature[] = {
            String.class.getName(),
            Object.class.getName()
        };
        Object methodParams[] = {
            "direct:start",
            null
        };

        // Get Camel to return the BeanManagers that are in use
        Map<String, BeanManager> beanMangers = (Map<String, BeanManager>) server.invoke(objectName, "requestBody", methodParams, methodSignature);

        BeanManager subModuleBeanManager = beanMangers.get("subModule");
        BeanManager camelBeanManager = beanMangers.get("camel");

        System.err.println(">>>>>> [Sub Module BeanManager] " + subModuleBeanManager);
        System.err.println(">>>>>> [Camel BeanManager]" + camelBeanManager);

        // Assert that the @Named HelloBean can be resolved
        Assert.assertTrue("Sub module bean manager could not resolve helloBean",
            beanManagerCanResolveBean(subModuleBeanManager, "helloBean"));

        Assert.assertTrue("Camel bean manager could not resolve helloBean",
            beanManagerCanResolveBean(camelBeanManager, "helloBean"));
    }

    private boolean beanManagerCanResolveBean(BeanManager beanManager, String name) {
        return beanManager.getBeans(name).iterator().hasNext();
    }
}
