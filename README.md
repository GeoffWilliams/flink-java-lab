# Flink Java Hello World Lab

* Confluent Cloud
* Flink Java running locally
* WINDOWS + WSL2! (docs are for windows - Linux users you know what to do)

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

## Flink setup

* All commands run inside WSL2 Ubuntu

```shell
./install_flink.sh
```

To make Flink available from Windows Edit the file `./flink*/conf/flink-conf.yaml`, set:

```
jobmanager.bind-host: 0.0.0.0
taskmanager.bind-host: 0.0.0.0
rest.bind-address: 0.0.0.0
```

## Start Flink!

Cluster:

```shell
cd flink*/bin
./start-cluster.sh
```

Job Manager (x3):

```shell
./taskmanager.sh start
./taskmanager.sh start
./taskmanager.sh start
```

Flink UI will be available shortly: [http://localhost:8081/](http://localhost:8081/#/overview)

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


## Run
```shell
./flink*/bin/flink run target/flink-java-lab-0.1.jar
```

## Wheres my output?

look in `./flink*/logs`, you will see two files for each job:
1. Captured `STDOUT`: `*.out`
2. Logs from the job itself: `*.log`

Inspect the files with `cat` or text editor.

## Eplore the Flink UI:
* Start/stop/analyse jobs (jobs menu)
* View the output (task managers -> pick one -> stdout tab)
* Where is my job running? Good question. Each Task Manager only has one slot so your job will be running on one of the Task Managers with no free slots. Look around until you find it
* **Important**: Job output (`STDOUT`) is displayed at the _bottom_ of the output, so you will have to scroll all 
  the way to the bottom to see your output. This makes sense since `System.out.println()` causes output where the 
  JVM runs



## Redeployment

* Repeat Compile and Redeployment as necessary

## Cleanup

Reboot! Or stop the Java processes:

```shell
# Stop all Task Managers
./taskmanager.sh stop-all

# Stop the Job Manager
./stop-cluster.sh 
```