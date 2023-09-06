# Apache Flink 로컬 개발환경 셋업 (Docker Compose)

<br>



### 참고자료

- https://hub.docker.com/_/flink
- https://nightlies.apache.org/flink/flink-docs-master/docs/deployment/resource-providers/standalone/docker/
- [Flink With Docker Compose](https://nightlies.apache.org/flink/flink-docs-master/docs/deployment/resource-providers/standalone/docker/#flink-with-docker-compose)

<br>



### docker-compose (session mode)

참고자료에 명시한 [Flink With Docker Compose](https://nightlies.apache.org/flink/flink-docs-master/docs/deployment/resource-providers/standalone/docker/#flink-with-docker-compose) 에는 Application Mode, Session Mode, Sql Client with Sessiom Mode 가 있는데, 오늘 정리할 내용은 그 중 Session Mode 다.

```yaml
version: "2.2"
services:
  jobmanager:
    image: flink:latest
    ports:
      - "8081:8081"
    command: jobmanager
    environment:
      - |
        FLINK_PROPERTIES=
        jobmanager.rpc.address: jobmanager        

  taskmanager:
    image: flink:latest
    depends_on:
      - jobmanager
    command: taskmanager
    scale: 1
    environment:
      - |
        FLINK_PROPERTIES=
        jobmanager.rpc.address: jobmanager
        taskmanager.numberOfTaskSlots: 2        
  sql-client:
    image: flink:latest
    command: bin/sql-client.sh
    depends_on:
      - jobmanager
    environment:
      - |
        FLINK_PROPERTIES=
        jobmanager.rpc.address: jobmanager
        rest.address: jobmanager        
```

<br>



### docker script 

또는 아래 명령어로 docker container 를 구동할 수 있다. 그런데 조금 귀찮긴 하다.

jobmanager

```bash
$ docker container run -d --name flink-jobmanager -e JOB_MANAGER_RPC_ADDRESS=jobmanager -p 8081:8081 flink:latest jobmanager
```

<br>



task manager

```bash
$ docker container run -d --name flink-taskmanager-1 --link flink-jobmanager:jobmanager -e JOB_MANAGER_RPC_ADDRESS=jobmanager flink:latest taskmanager
```







