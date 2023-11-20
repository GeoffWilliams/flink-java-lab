# Flink Java Hello World Lab

* Confluent Cloud
* Flink Java running locally
* Docker or WSL2 process
* Docs are for windows - Linux users you know what to do

## Ready to use examples

* `NotAJob` - this is not a flink job! its an example of programmer misunderstanding
* `HelloWorldJob` - Print some numbers, then exit
* `KafkaConsumer` - Connect to Kafka, then print out the topic data
* `KafkaKafkaFlatMap` - Connect to two Kafka topics and `FlatMap` them together, then print the results
* `KafkaKafkaJoin` - Same idea as `KafkaKafkaFlatMap` but using windows and joins

## Requirements

* [Windows 11, WSL2, Ubuntu, Port Forwarding, Intellij - all setup already](https://github.com/GeoffWilliams/windowsnow)
* Flink - we will just unpack the tarball and run from inside WSL2
* [Confluent Cloud account](https://confluent.cloud)
* Docker (optional)

## Lab operation and setup:

* [Docker compose](./doc/docker_compose.md)
* [Local process](./doc/wsl2_process.md)

## Lab Flink UI

Flink UI is available at: [http://localhost:8081/](http://localhost:8081/#/overview)

## What job to run?

The lab compiles a jar with a single main class. Each example Job is its own class to avoid chaotic commenting, so just Create/edit classes as you wish.

## Compile

```shell
# Pick one or edit your own
mvn clean package -DmainFlinkClass=com.example.NotAJob
mvn clean package -DmainFlinkClass=com.example.HelloWorldJob
mvn clean package -DmainFlinkClass=com.example.KafkaConsumerJob
mvn clean package -DmainFlinkClass=com.example.KafkaKafkaFlatMapJob
mvn clean package -DmainFlinkClass=com.example.KafkaKafkaJoinJob
```
Argument `mainFlinkClass` sets the `main` class in the maven build at compile time and therefore the produced jar manifest.

## Redeployment

* Repeat Compile and Redeployment as necessary

## Eplore the Flink UI:
* Start/stop/analyse jobs (jobs menu)
* View the output (not for docker!) task managers -> pick one -> stdout tab
* Where is my job running? Good question. Each Task Manager only has one slot so your job will be running on one of the Task Managers with no free slots. Look around until you find it
* **Important**: Job output (`STDOUT`) is displayed at the _bottom_ of the output, so you will have to scroll all 
  the way to the bottom to see your output. This makes sense since `System.out.println()` causes output where the 
  JVM runs
