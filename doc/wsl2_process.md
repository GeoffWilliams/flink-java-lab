# Lab setup and operation - WSL2 process

## Start Flink lab

* All commands run inside WSL2 Ubuntu from `flink*/bin` directory

```shell
./install_flink.sh
```

To make Flink available from Windows Edit the file `./flink*/conf/flink-conf.yaml`, set:

```
jobmanager.bind-host: 0.0.0.0
taskmanager.bind-host: 0.0.0.0
rest.bind-address: 0.0.0.0
```

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

## Run

```shell
./flink*/bin/flink run target/flink-java-lab-0.1.jar
```

## Wheres my output?

look in `./flink*/logs`, you will see two files for each job:
1. Captured `STDOUT`: `*.out`
2. Logs from the job itself: `*.log`

## Cleanup

Reboot! Or stop the Java processes:

```shell
# Stop all Task Managers
./taskmanager.sh stop-all

# Stop the Job Manager
./stop-cluster.sh 
```