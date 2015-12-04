# Camel CDI EAR Test

Runs a set of Apache Camel CDI deployment test scenarios on WildFly.

### Scenario 1

Deploys an EAR with Camel CDI dependencies contained within the EAR lib directory and a simple Camel application as a JAR sub-module.

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
            
### Scenario 3

WAR deployment with Camel dependencies modularized in module `org.apache.camel.cdi`.

```
- my-application.war
  |--- WEB-INF
       |--- beans.xml
       |--- classes
            |--- Bootstrap.class
            |--- HelloBean.class            
```

### Scenario 4

JAR deployment with Camel dependencies modularized in module `org.apache.camel.cdi`.

```
- my-application.jar
  |--- META-INF
       |--- beans.xml
  |--- com.myapplication
       |--- Bootstrap.class
       |--- HelloBean.class
```
            
### Running tests

Clone this project and run:

```
mvn clean install
```

Or run individual scenarios:

```
mvn clean install -pl scenario-4
```
