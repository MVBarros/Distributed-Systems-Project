# ForkExec

**Fork**: delicious menus for **Exec**utives

Distributed Systems 2018-2019, 2nd semester project

## Authors

Group T08

Miguel Veloso Barros 87691 MVBarros 

Carolina Carreira 87641 CarolinaCC 

## Getting Started

The overall system is composed of multiple services and clients.
The main service is the _hub_ service that is aided by the _pts_ service. 
There are also multiple _rst_ services, one for each participating restaurant.

See the project statement for a full description of the domain and the system.



### Prerequisites

Java Developer Kit 8 is required running on Linux, Windows or Mac.
Maven 3 is also required.

To confirm that you have them installed, open a terminal and type:

```
javac -version

mvn -version
```


### Installing

To compile and install all modules:

```
mvn clean install -DskipTests
```

The tests are skipped because they require each server to be running.

###Run with tests

launch two instances of restaurants

```
rst-ws/$mvn compile exec:java
```

```
rst-ws/$mvn compile exec:java -Dws.i=2
```

launch one instance of points

```
pts-ws$mvn compile exec:java
```

launch one instance of hub

```
hub-ws$mvn compile exec:java
```

run tests in root project directory

```
$mvn install
```


Note: There may be some problems with the UDDI server that can cause test failures, recomended to run again after reiniciating all tests

Recommended before running tests installing without running tests

## Built With

* [Maven](https://maven.apache.org/) - Build Tool and Dependency Management
* [JAX-WS](https://javaee.github.io/metro-jax-ws/) - SOAP Web Services implementation for Java



## Versioning

We use [SemVer](http://semver.org/) for versioning. 



