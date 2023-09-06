# 로컬 개발 용도 싱글브로커,멀티브로커 카프카 docker-compose 로 설치



### docker

#### 싱글브로커

docker-compose.yml

```yaml
version: "3.5"
services:
  zk:
    image: confluentinc/cp-zookeeper:5.5.1
    restart: always
    hostname: zk
    container_name: zk
    ports:
      - "2181:2181"
    environment:
      - ZOOKEEPER_SERVER_ID=1
      - ZOOKEEPER_CLIENT_PORT=2181
      - ZOOKEEPER_TICK_TIME=2000
      - ZOOKEEPER_INIT_LIMIT=5
      - ZOOKEEPER_SYNC_LIMIT=2
      - ZOOKEEPER_SERVERS=zk:2888:3888

  kafka:
    image: confluentinc/cp-kafka:5.5.1
    restart: always
    hostname: kafka
    container_name: kafka
    ports:
      - "9092:9092"
      - "9999:9999"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zk:2181
      KAFKA_LISTENERS: INTERNAL://kafka:9092
      KAFKA_ADVERTISED_LISTENERS: INTERNAL://kafka:9092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INTERNAL:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: INTERNAL
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
      KAFKA_JMX_PORT: 9999

  kafka_manager:
    image: hlebalbau/kafka-manager:stable
    container_name: cmak
    ports:
      - "9000:9000"
    environment:
      ZK_HOSTS: "zk:2181"
      APPLICATION_SECRET: "random-secret"
    command: -Dpidfile.path=/dev/null
```



##### docker-compose 구동

터미널에서 아래 명령어 실행

```bash
# 실행
$ docker-compose up -d

# 종료
$ docker-compose down
```



##### bash 접속 후 kafak-topics 를 통해 현재 등록된 토픽 출력해보기

설명은 주석에 추가해줬다.

```bash
### container 정보 출력
$ docker container ls
CONTAINER ID   IMAGE                             ...   STATUS       PORTS 						
3f40ebb6ba83   confluentinc/cp-kafka:7.0.9       ...   Up 3 hours   0.0.0.0:9092->9092/tcp, 0.0.0.0:9999->9999/tcp
93f5e96dd175   confluentinc/cp-zookeeper:7.0.9   ...   Up 3 hours   2888/tcp, 0.0.0.0:2181->2181/tcp, 3888/tcp    
0e0871ed5d5c   hlebalbau/kafka-manager:stable    ...   Up 3 hours   0.0.0.0:9000->9000/tcp                   


### 현재 kafka 의 container id 는 `3f40ebb6ba83` 이다.
### 컨테이너 3f40ebb6ba83 에 bash 로 접속해보자.
$ docker exec -it 3f40ebb6ba83 bash
[appuser@kafka ~]$


### /usr/bin 내에 kafka 관련 cli 들이 있기에 /usr/bin 으로 이동
$ cd usr/bin


### 토픽 생성
$ kafka-topics --create --topic hello-message --bootstrap-server kafka:9092 --replication-factor 1 --partitions 3
Created topic hello-message.


### 현재 등록된 토픽들을 출력해본다.
$ kafka-topics --bootstrap-server kafka:9092 --list
hello-message

### 1)
### 새로운 터미널에서 컨테이너로 접속 후 /usr/bin 으로 이동한다.
### 토픽을 생산하는 kafka-console-producer.sh 를 실행한다.
$ kafka-console-producer --topic hello-message --broker-list kafka:9092

### 아래의 2) 를 수행해서 컨슈머를 띄워둔 후 아래와 같이 메시지를 입력한다.
> 안녕하세요


### 2)
### 새로운 터미널에서 컨테이너로 접속 후 /usr/bin 으로 이동한다.
### 토픽을 소비하는 kafka-console-consumer.sh 를 실행한다.
$ kafka-console-consumer --topic hello-message --broker-list kafka:9092

## 1) 에서 메시지를 입력후 이곳에 출력되는지 확인한다.
> 안녕하세요
```

<br>



#### 멀티브로커

- 주키퍼 : 3기
- 브로커 : 3기
- Kafka-manager: 1기

```yaml
version: "3.5"
services:
  zk1:
    image: confluentinc/cp-zookeeper:5.5.1
    restart: always
    hostname: zk1
    container_name: zk1
    ports:
      - "2181:2181"
    environment:
      - ZOOKEEPER_SERVER_ID=1
      - ZOOKEEPER_CLIENT_PORT=2181
      - ZOOKEEPER_TICK_TIME=2000
      - ZOOKEEPER_INIT_LIMIT=5
      - ZOOKEEPER_SYNC_LIMIT=2
      - ZOOKEEPER_SERVERS=zk1:2888:3888;zk2:2888:3888;zk3:2888:3888
  zk2:
    image: confluentinc/cp-zookeeper:5.5.1
    restart: always
    hostname: zk2
    container_name: zk2
    ports:
      - "2182:2182"
    environment:
      - ZOOKEEPER_SERVER_ID=2
      - ZOOKEEPER_CLIENT_PORT=2182
      - ZOOKEEPER_TICK_TIME=2000
      - ZOOKEEPER_INIT_LIMIT=5
      - ZOOKEEPER_SYNC_LIMIT=2
      - ZOOKEEPER_SERVERS=zk1:2888:3888;zk2:2888:3888;zk3:2888:3888
  zk3:
    image: confluentinc/cp-zookeeper:5.5.1
    restart: always
    hostname: zk3
    container_name: zk3
    ports:
      - "2183:2183"
    environment:
      - ZOOKEEPER_SERVER_ID=3
      - ZOOKEEPER_CLIENT_PORT=2183
      - ZOOKEEPER_TICK_TIME=2000
      - ZOOKEEPER_INIT_LIMIT=5
      - ZOOKEEPER_SYNC_LIMIT=2
      - ZOOKEEPER_SERVERS=zk1:2888:3888;zk2:2888:3888;zk3:2888:3888

  kafka1:
    image: confluentinc/cp-kafka:5.5.1
    restart: always
    hostname: kafka1
    container_name: kafka1
    ports:
      - "9091:9091"
      - "9991:9991"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zk1:2181,zk2:2182,zk3:2183
      KAFKA_LISTENERS: INTERNAL://kafka1:9091
      KAFKA_ADVERTISED_LISTENERS: INTERNAL://kafka1:9091
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INTERNAL:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: INTERNAL
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 3
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 2
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 3
      KAFKA_JMX_PORT: 9991
  kafka2:
    image: confluentinc/cp-kafka:5.5.1
    restart: always
    hostname: kafka2
    container_name: kafka2
    ports:
      - "9092:9092"
      - "9992:9992"
    environment:
      KAFKA_BROKER_ID: 2
      KAFKA_ZOOKEEPER_CONNECT: zk1:2181,zk2:2182,zk3:2183
      KAFKA_LISTENERS: INTERNAL://kafka2:9092
      KAFKA_ADVERTISED_LISTENERS: INTERNAL://kafka2:9092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INTERNAL:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: INTERNAL
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 3
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 2
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 3
      KAFKA_JMX_PORT: 9992
  kafka3:
    image: confluentinc/cp-kafka:5.5.1
    restart: always
    hostname: kafka3
    container_name: kafka3
    ports:
      - "9093:9093"
      - "9993:9993"
    environment:
      KAFKA_BROKER_ID: 3
      KAFKA_ZOOKEEPER_CONNECT: zk1:2181,zk2:2182,zk3:2183
      KAFKA_LISTENERS: INTERNAL://kafka3:9093
      KAFKA_ADVERTISED_LISTENERS: INTERNAL://kafka3:9093
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INTERNAL:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: INTERNAL
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 3
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 2
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 3
      KAFKA_JMX_PORT: 9993

  kafka_manager:
    image: hlebalbau/kafka-manager:stable
    container_name: cmak
    ports:
      - "9000:9000"
    environment:
      ZK_HOSTS: "zk1:2181,zk2:2182,zk3:2183"
      APPLICATION_SECRET: "random-secret"
    command: -Dpidfile.path=/dev/null
```

<br>



터미널에서 아래 명령어를 실행

```bash
# 실행
$ docker-compose up -d

# 종료
$ docker-compose down
```

<br>



