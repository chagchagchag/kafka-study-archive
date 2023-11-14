
### 포트 사용 정보
- 2181 : zookeeper
- 8081 : schema registry
    - depends_on : broker1, broker2, broker3
- 19092 : broker1
- 29092 : broker2
- 39092 : broker3
- 9000 : kafka-manager

<br>

### 수정 필요한 내용들
#### 2023.09.28
- `init_kafka` : 토픽 초기화 부분 작성을 새로 처음부터 다시 해야 함. 지금 작성된 토픽 초기화 코드는 예제용도.

### 카프카 구동
- 주키퍼 실행
- 카프카 클러스터 실행
- 카프카 토픽 생성 작업 (init_kafka.yml)
<br>

> 공통적인 정보들은 `common.yml` 에 정의해둠

<br>

#### 주키퍼 실행

zookeeper.yml 이 있는 디렉터리에서 아래 명령을 실행

```bash
docker-compose -f common.yml -f zookeeper.yml up -d
```

<br>



주키퍼 상태 확인

```bash
$ echo ruok | nc localhost 2181
```

<br>



또는 telnet으로 아래와 같이 수행

```bash
$ telnet localhost 2181

# ruok 을 타이핑한다.

imok

```

<br>



`echo ruok | nc localhost 2181`  명령으로 상태를 확인하는 것에 대해서는 https://zookeeper.apache.org/doc/r3.8.1/zookeeperAdmin.html#sc_zkCommands 에서 자세한 명령어의 옵션 등을 확인할 수 있다.

<br>



#### 카프카 클러스터 실행

kafka\_cluster.yml 파일이 있는 디렉터리에서 아래의 명령을 수행

```bash
$ docker-compose -f common.yml -f kafka_cluster.yml up -d
```

<br>



#### kafka 토픽 생성 작업 (init_kafka.yml)

init\_kafka.yml 파일이 있는 디렉터리로 이동해서 아래의 명령을 수행

```bash
$ docker-compose -f common.yml -f init_kafka.yml up -d
```

<br>



### 클러스터 확인

#### localhost:9000

브라우저에 http://localhost:9000/ 을 입력해 이동하면 아래와 같은 화면이 나타난다.

![](./img/DOCKER/4.png)

Cluster 메뉴를 클릭하고 Add Cluster 버튼을 클릭한다.

![](./img/DOCKER/5.png)

그리고 아래와 같이 클러스터 명과 Zookeeper host, port 를 입력해준다.

![](./img/DOCKER/6.png)

<br>



이제 Cluster 들의 상태를 확인하기 위해 Go to cluster view 링크를 클릭해본다.

![](./img/DOCKER/7.png)

<br>



아래 그림 처럼 토픽들이 나타나는 것을 볼수 있다. 지금까지의 과정은 init_kafka.yml 로 토픽을 추가해줬지만, 카프카 설치 초기에는 토픽이 있더라도 클러스터가 아직은 생성되지 않았기에 클러스터를 새로 생성 후에 토픽이 제대로 존재하는지 확인하는 과정이었다.

![](./img/DOCKER/8.png)

