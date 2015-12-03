# Camel CDI EAR Test

Runs two CDI Ear deployment test scenarios.

### Scenario 1

Deploys an Ear with Camel CDI dependencies contained within the Ear lib directory and a simple Camel application as a Jar sub-module.

```
- my-application.ear
  |--- lib
       |--- camel-cdi-2.16.1.jar
       |--- camel-core-2.16.1.jar
       |--- deltaspike-core-api-1.5.1.jar
       |--- deltaspike-core-impl-1.5.1.jar       
  |--- my-module.jar
       |--- META-INF
            |--- beans.xml
       |--- com.myapplication
            |--- Bootstrap.class
            |--- HelloBean.class
```

### Scenario 2

As per scenario 1 but the Camel dependencies are modularized in module `org.apache.camel.cdi`.

```
- my-application.ear
  |--- my-module.jar
       |--- META-INF
            |--- beans.xml
       |--- com.myapplication
            |--- Bootstrap.class
            |--- HelloBean.class
            

- module: org.apache.camel.cdi:main
|-- camel-cdi-2.16.1.jar
|-- camel-core-2.16.1.jar
|-- deltaspike-core-api-1.5.1.jar
|-- deltaspike-core-impl-1.5.1.jar
```
            
### Running tests

Clone this project and run:

```
mvn clean install
```
