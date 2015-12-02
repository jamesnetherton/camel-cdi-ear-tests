package com.jamesnetherton.tests.cdi.ear;

import java.io.File;
import java.util.Scanner;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jamesnetherton.tests.cdi.ear.bean.Bootstrap;

@RunWith(Arquillian.class)
public class CDIEarTest {

    @Deployment(name = "camel-ejb-ear.ear", managed = true, testable = false)
    public static EnterpriseArchive createEarDeployment() {
        EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "camel-ejb-ear.ear");
        ear.addAsModule(
            ShrinkWrap.create(JavaArchive.class, "camel-ejb-jar.jar")
            .addAsManifestResource(new StringAsset("Dependencies: org.apache.camel services export"), "MANIFEST.MF")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .addPackage(Bootstrap.class.getPackage())
            .addPackage(CDIEarTest.class.getPackage())
        );
        return ear;
    }

    @Test
    public void testEjbJarDeployment() throws Exception {
        // Wait for camel to record which bean managers are in use
        Thread.sleep(1000);

        File file = new File("target/beanmanagers.txt");
        Assert.assertTrue(file.exists());

        String fileContent = new Scanner(file).useDelimiter("\\A").next();
        String[] beanManagers = fileContent.split("\\n");

        Assert.assertTrue(beanManagers.length == 2);
        String applicationBeanManager = beanManagers[0];
        String camelBeanManager = beanManagers[1];

        System.err.println(">>>>> Application BeanManager: " + applicationBeanManager);
        System.err.println(">>>>> CamelBeanManager:        " + camelBeanManager);

        Assert.assertEquals(applicationBeanManager, camelBeanManager);

    }
}
