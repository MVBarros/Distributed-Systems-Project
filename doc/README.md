# Demonstracao


## Authors

Group T08

Miguel Veloso Barros 87691 MVBarros

Carolina Carreira 87641 CarolinaCC

## Getting Started

Begin by creating three Points Servers in three terminal windows

```
pts-ws$mvn compile exec:java

pts-ws$mvn compile exec:java -Dws.i=2

pts-ws$mvn compile exec:java -Dws.i=3
```

In a new terminal window create a Hub Server

```
hub-ws$mvn compile exec:java

```

Finally in a new terminal window go to hub-ws-cli and run the comand

```
hub-ws-cli $mvn compile exec:java

```

Follow the instructions and have fun :)
