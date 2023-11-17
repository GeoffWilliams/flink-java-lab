# Flink Java Shoe Store

* Confluent Cloud
* Flink Java running locally
* WINDOWS + WSL2! (docs are for windows - Linux users you know what to do)

## Requirements

* [Windows 11, WSL2, Ubuntu, Port Forwarding, Intellij - all setup already](https://github.com/GeoffWilliams/windowsnow)
* Flink - we will just unpack the tarball and run from inside WSL2
- [A Confluent Cloud account](https://confluent.cloud)

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
```

Argument `mainFlinkClass` sets the `main` class in the maven build at compile time and therefore the produced jar manifest.


## Run
```shell
./flink*/bin/flink run target/flink-java-lab-0.1.jar
```

## Redeployment

* Repeat Compile and Redeployment as necessary

## Cleanup

Reboot! (or kill the java processes)