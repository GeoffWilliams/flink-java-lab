# Lab setup and operation - docker compose

## Start Flink lab

```shell
docker compose up -d
```

## Get a shell

Get a shell:

```shell
# job manager
docker compose exec jobmanager bash

# task manager (random)
docker compose exec taskmanager bash

# To target a specific taskmanager container, look up its ID and exec without compose, eg:
# docker exec -ti 43be5f13190f bash
```

Deploy jar to cluster:

```shell
docker compose exec jobmanager flink run /project/target/flink-java-lab-0.1.jar 
```


### Process based
```shell
./flink*/bin/flink run target/flink-java-lab-0.1.jar
```

## Wheres my output?

* `docker compose logs` to view `STDOUT` 
* `STDOUT` is not logged in the Flink UI Docker mode
* Job logs in `/opt/flink/log/`

## Cleanup

```shell
docker compose down
```
